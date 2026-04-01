package pl.telly.cursor.orlen;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;
import java.time.LocalDate;

@ConfigurationProperties(prefix = "orlen")
public record OrlenProperties(
        String baseUrl,
        Schedule schedule,
        Bootstrap bootstrap
) {
    public record Schedule(String cron, String zone) {}

    public record Bootstrap(
            boolean enabled,
            Duration initialDelay,
            LocalDate from,
            int backfillDays
    ) {}
}

