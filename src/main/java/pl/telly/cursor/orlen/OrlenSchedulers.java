package pl.telly.cursor.orlen;

import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class OrlenSchedulers {
    private final OrlenProperties properties;
    private final OrlenImportService importService;

    public OrlenSchedulers(OrlenProperties properties, OrlenImportService importService) {
        this.properties = properties;
        this.importService = importService;
    }

    @Scheduled(cron = "${orlen.schedule.cron}", zone = "${orlen.schedule.zone}")
    public void dailyImportAtSix() {
        var products = importService.syncProducts();

        int backfillDays = properties.bootstrap().backfillDays();
        LocalDate to = LocalDate.now();
        LocalDate from = to.minusDays(Math.max(0, backfillDays));

        products.forEach(p -> importService.importPrices(p.getId(), from, to));
    }

    @Async
    @EventListener(ApplicationReadyEvent.class)
    public void bootstrapAfterStartup() throws InterruptedException {
        if (!properties.bootstrap().enabled()) {
            return;
        }

        Thread.sleep(properties.bootstrap().initialDelay().toMillis());

        var products = importService.syncProducts();
        products.forEach(p -> importService.importPrices(p.getId(), null, null));
    }
}

