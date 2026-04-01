package pl.telly.cursor.api;

import pl.telly.cursor.fuel.FuelPrice;
import pl.telly.cursor.fuel.FuelPriceRepository;
import pl.telly.cursor.fuel.FuelProductRepository;
import pl.telly.cursor.orlen.OrlenImportService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

import static org.springframework.format.annotation.DateTimeFormat.ISO;

@RestController
@RequestMapping("/api/fuel-prices")
public class FuelPriceController {
    private final FuelProductRepository productRepository;
    private final FuelPriceRepository priceRepository;
    private final OrlenImportService importService;

    public FuelPriceController(
            FuelProductRepository productRepository,
            FuelPriceRepository priceRepository,
            OrlenImportService importService
    ) {
        this.productRepository = productRepository;
        this.priceRepository = priceRepository;
        this.importService = importService;
    }

    @GetMapping
    public List<FuelPriceResponse> prices(
            @RequestParam int productId,
            @RequestParam @DateTimeFormat(iso = ISO.DATE) LocalDate from,
            @RequestParam @DateTimeFormat(iso = ISO.DATE) LocalDate to
    ) {
        ensureProductKnown(productId);

        if (to.isBefore(from)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "`to` must be >= `from`");
        }

        return priceRepository.findAllByProduct_IdAndEffectiveDateBetweenOrderByEffectiveDateAsc(productId, from, to)
                .stream()
                .map(FuelPriceController::toResponse)
                .toList();
    }

    @GetMapping("/average")
    public AveragePriceResponse averageForMonth(
            @RequestParam int productId,
            @RequestParam String month
    ) {
        ensureProductKnown(productId);

        YearMonth ym;
        try {
            ym = YearMonth.parse(month);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "`month` must be in format YYYY-MM");
        }

        LocalDate from = ym.atDay(1);
        LocalDate to = ym.atEndOfMonth();

        BigDecimal avg = priceRepository.averageValue(productId, from, to).orElse(null);
        long count = priceRepository.countByProduct_IdAndEffectiveDateBetween(productId, from, to);

        return new AveragePriceResponse(productId, month, avg, count);
    }

    private void ensureProductKnown(int productId) {
        if (!productRepository.existsById(productId)) {
            importService.syncProducts();
            if (!productRepository.existsById(productId)) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Unknown productId=" + productId);
            }
        }
    }

    private static FuelPriceResponse toResponse(FuelPrice fp) {
        return new FuelPriceResponse(
                fp.getProduct().getId(),
                fp.getProductSymbol(),
                fp.getEffectiveDate(),
                fp.getValue(),
                fp.getUnit(),
                fp.getLocationName(),
                fp.getLocationSymbol()
        );
    }
}

