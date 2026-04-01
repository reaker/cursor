package pl.telly.cursor.orlen;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.telly.cursor.fuel.FuelPrice;
import pl.telly.cursor.fuel.FuelPriceRepository;
import pl.telly.cursor.fuel.FuelProduct;
import pl.telly.cursor.fuel.FuelProductRepository;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class OrlenImportService {
    private final OrlenApiClient apiClient;
    private final OrlenProperties properties;
    private final FuelProductRepository productRepository;
    private final FuelPriceRepository priceRepository;

    public OrlenImportService(
            OrlenApiClient apiClient,
            OrlenProperties properties,
            FuelProductRepository productRepository,
            FuelPriceRepository priceRepository
    ) {
        this.apiClient = apiClient;
        this.properties = properties;
        this.productRepository = productRepository;
        this.priceRepository = priceRepository;
    }

    @Transactional
    public List<FuelProduct> syncProducts() {
        List<FuelProduct> products = apiClient.products().stream()
                .map(p -> new FuelProduct(p.id(), p.name(), p.nameEn(), p.symbol()))
                .toList();
        return productRepository.saveAll(products);
    }

    @Transactional
    public int importPrices(int productId, LocalDate from, LocalDate to) {
        FuelProduct product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Unknown productId=" + productId));

        ZoneId zoneId = ZoneId.of(properties.schedule().zone());
        List<PriceDto> remote;
        if (from == null) {
            remote = apiClient.fullPricesByProduct(productId);

        } else {
            remote = apiClient.pricesByProduct(productId, from, to);
        }
        if (remote.isEmpty()) {
            return 0;
        }

        Set<LocalDate> candidateDates = remote.stream()
                .map(p -> p.effectiveDate().toLocalDate())
                .collect(Collectors.toSet());

        Set<LocalDate> existingDates = Set.copyOf(priceRepository.findExistingDates(productId, candidateDates));

        List<FuelPrice> toInsert = remote.stream()
                .filter(p -> !existingDates.contains(p.effectiveDate().toLocalDate()))
                .map(p -> new FuelPrice(
                        product,
                        p.productName(),
                        p.effectiveDate().toLocalDate(),
                        p.publishFrom() == null ? null : p.publishFrom().atZone(zoneId).toInstant(),
                        p.value(),
                        emptyToNull(p.locationName()),
                        emptyToNull(p.locationSymbol()),
                        emptyToNull(p.unit())
                ))
                .toList();

        if (toInsert.isEmpty()) {
            return 0;
        }

        priceRepository.saveAll(toInsert);
        return toInsert.size();
    }

    private static String emptyToNull(String s) {
        if (s == null) return null;
        String t = s.trim();
        return t.isEmpty() ? null : t;
    }
}

