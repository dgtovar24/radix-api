package com.project.radix.Model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "isotope_catalogs")
public class IsotopeCatalog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String name;

    private String symbol;

    private String type;

    @Column(name = "half_life")
    private Double halfLife;

    @Column(name = "half_life_unit")
    private String halfLifeUnit;

    public IsotopeCatalog() {}

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getSymbol() { return symbol; }
    public void setSymbol(String symbol) { this.symbol = symbol; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public Double getHalfLife() { return halfLife; }
    public void setHalfLife(Double halfLife) { this.halfLife = halfLife; }
    public String getHalfLifeUnit() { return halfLifeUnit; }
    public void setHalfLifeUnit(String halfLifeUnit) { this.halfLifeUnit = halfLifeUnit; }
}