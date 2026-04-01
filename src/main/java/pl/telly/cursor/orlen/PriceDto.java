package pl.telly.cursor.orlen;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@JsonIgnoreProperties(ignoreUnknown = true)
public record PriceDto(
        String productName,
        LocalDateTime effectiveDate,
        LocalDateTime publishFrom,
        BigDecimal value,
        String locationName,
        String locationSymbol,
        String unit
) {}

