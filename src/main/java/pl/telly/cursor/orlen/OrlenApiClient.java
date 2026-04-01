package pl.telly.cursor.orlen;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.time.LocalDate;
import java.util.List;

@Component
public class OrlenApiClient {
    private final RestClient restClient;

    public OrlenApiClient(OrlenProperties properties, RestClient.Builder builder) {
        this.restClient = builder
                .baseUrl(properties.baseUrl())
                .build();
    }

    public List<ProductDto> products() {
        ProductDto[] body = restClient.get()
                .uri("/api/wholesalefuelprices/Products")
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .body(ProductDto[].class);
        return body == null ? List.of() : List.of(body);
    }

    public List<PriceDto> pricesByProduct(int productId, LocalDate from, LocalDate to) {
        PriceDto[] body = restClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/wholesalefuelprices/ByProduct")
                        .queryParam("productId", productId)
                        .queryParam("from", from)
                        .queryParam("to", to)
                        .build())
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .body(PriceDto[].class);
        return body == null ? List.of() : List.of(body);
    }
    public List<PriceDto> fullPricesByProduct(int productId) {
        PriceDto[] body = restClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/wholesalefuelprices/ByProduct")
                        .queryParam("productId", productId)
                        .build())
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .body(PriceDto[].class);
        return body == null ? List.of() : List.of(body);
    }
}

