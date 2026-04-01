package pl.telly.cursor.orlen;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ProductDto(
        int id,
        String name,
        String nameEn,
        String symbol
) {}

