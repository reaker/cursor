package pl.telly.cursor.fuel;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface FuelPriceRepository extends JpaRepository<FuelPrice, Long> {
    List<FuelPrice> findAllByProduct_IdAndEffectiveDateBetweenOrderByEffectiveDateAsc(int productId, LocalDate from, LocalDate to);

    @Query("""
            select fp.effectiveDate
            from FuelPrice fp
            where fp.product.id = :productId and fp.effectiveDate in :dates
            """)
    List<LocalDate> findExistingDates(@Param("productId") int productId, @Param("dates") Collection<LocalDate> dates);

    @Query("""
            select avg(fp.value)
            from FuelPrice fp
            where fp.product.id = :productId
              and fp.effectiveDate >= :from
              and fp.effectiveDate <= :to
            """)
    Optional<BigDecimal> averageValue(@Param("productId") int productId, @Param("from") LocalDate from, @Param("to") LocalDate to);

    long countByProduct_IdAndEffectiveDateBetween(int productId, LocalDate from, LocalDate to);
}

