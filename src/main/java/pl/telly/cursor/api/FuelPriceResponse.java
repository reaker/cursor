package pl.telly.cursor.api;

import java.math.BigDecimal;
import java.time.LocalDate;

public record FuelPriceResponse(
        int productId,
        String productSymbol,
        LocalDate effectiveDate,
        BigDecimal value,
        String unit,
        String locationName,
        String locationSymbol
) {}

