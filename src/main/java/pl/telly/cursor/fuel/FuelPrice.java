package pl.telly.cursor.fuel;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;

@Entity
@Table(
        name = "fuel_price",
        uniqueConstraints = @UniqueConstraint(name = "uq_fuel_price_product_date", columnNames = {"product_id", "effective_date"})
)
public class FuelPrice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "product_id", nullable = false)
    private FuelProduct product;

    @Column(name = "product_symbol", nullable = false)
    private String productSymbol;

    @Column(name = "effective_date", nullable = false)
    private LocalDate effectiveDate;

    @Column(name = "publish_from", nullable = false)
    private Instant publishFrom;

    @Column(name = "price_value", nullable = false, precision = 12, scale = 4)
    private BigDecimal value;

    @Column(name = "location_name")
    private String locationName;

    @Column(name = "location_symbol")
    private String locationSymbol;

    @Column(name = "unit")
    private String unit;

    protected FuelPrice() {}

    public FuelPrice(
            FuelProduct product,
            String productSymbol,
            LocalDate effectiveDate,
            Instant publishFrom,
            BigDecimal value,
            String locationName,
            String locationSymbol,
            String unit
    ) {
        this.product = product;
        this.productSymbol = productSymbol;
        this.effectiveDate = effectiveDate;
        this.publishFrom = publishFrom;
        this.value = value;
        this.locationName = locationName;
        this.locationSymbol = locationSymbol;
        this.unit = unit;
    }

    public Long getId() {
        return id;
    }

    public FuelProduct getProduct() {
        return product;
    }

    public String getProductSymbol() {
        return productSymbol;
    }

    public LocalDate getEffectiveDate() {
        return effectiveDate;
    }

    public Instant getPublishFrom() {
        return publishFrom;
    }

    public BigDecimal getValue() {
        return value;
    }

    public String getLocationName() {
        return locationName;
    }

    public String getLocationSymbol() {
        return locationSymbol;
    }

    public String getUnit() {
        return unit;
    }
}

