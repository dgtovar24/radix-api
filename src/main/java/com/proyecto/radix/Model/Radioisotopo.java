package com.proyecto.radix.Model;

import jakarta.persistence.*;

@Entity
@Table(name = "Radioisotope")
public class Radioisotopo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String name;

    private String symbol;
    private String type;
    private Double halfLife;

    public Radioisotopo() {}

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
}
