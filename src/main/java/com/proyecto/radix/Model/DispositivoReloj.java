package com.proyecto.radix.Model;

import jakarta.persistence.*;

@Entity
@Table(name = "Dispositiu_Rellotge")
public class DispositivoReloj {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, unique = true)
    private String macAddress;

    private String model;
    private String estat;
    private Double nivellBateria;

    public DispositivoReloj() {}

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getMacAddress() { return macAddress; }
    public void setMacAddress(String macAddress) { this.macAddress = macAddress; }
    public String getModel() { return model; }
    public void setModel(String model) { this.model = model; }
    public String getEstat() { return estat; }
    public void setEstat(String estat) { this.estat = estat; }
    public Double getNivellBateria() { return nivellBateria; }
    public void setNivellBateria(Double nivellBateria) { this.nivellBateria = nivellBateria; }
}
