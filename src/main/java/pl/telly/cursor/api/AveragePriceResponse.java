package pl.telly.cursor.api;

import java.math.BigDecimal;

public record AveragePriceResponse(
        int productId,
        String month,
        BigDecimal average,
        long samples
) {}

