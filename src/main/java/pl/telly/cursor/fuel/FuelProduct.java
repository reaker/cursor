package pl.telly.cursor.fuel;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "fuel_product")
public class FuelProduct {
    @Id
    private Integer id;

    @Column(name = "name_pl", nullable = false)
    private String namePl;

    @Column(name = "name_en", nullable = false)
    private String nameEn;

    @Column(name = "symbol", nullable = false)
    private String symbol;

    protected FuelProduct() {}

    public FuelProduct(Integer id, String namePl, String nameEn, String symbol) {
        this.id = id;
        this.namePl = namePl;
        this.nameEn = nameEn;
        this.symbol = symbol;
    }

    public Integer getId() {
        return id;
    }

    public String getNamePl() {
        return namePl;
    }

    public String getNameEn() {
        return nameEn;
    }

    public String getSymbol() {
        return symbol;
    }
}

