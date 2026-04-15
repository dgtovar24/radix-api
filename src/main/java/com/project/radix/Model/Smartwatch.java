package com.project.radix.Model;

import jakarta.persistence.*;

@Entity
@Table(name = "smartwatches")
public class Smartwatch {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "fk_patient_id")
    private Integer fkPatientId;

    @Column(unique = true, nullable = false)
    private String imei;

    @Column(name = "mac_address", unique = true, nullable = false)
    private String macAddress;

    private String model;

    @Column(name = "is_active")
    private Boolean isActive = true;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_patient_id", insertable = false, updatable = false)
    private Patient patient;

    public Smartwatch() {}

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public Integer getFkPatientId() { return fkPatientId; }
    public void setFkPatientId(Integer fkPatientId) { this.fkPatientId = fkPatientId; }
    public String getImei() { return imei; }
    public void setImei(String imei) { this.imei = imei; }
    public String getMacAddress() { return macAddress; }
    public void setMacAddress(String macAddress) { this.macAddress = macAddress; }
    public String getModel() { return model; }
    public void setModel(String model) { this.model = model; }
    public Boolean getIsActive() { return isActive; }
    public void setIsActive(Boolean isActive) { this.isActive = isActive; }
    public Patient getPatient() { return patient; }
    public void setPatient(Patient patient) { this.patient = patient; }
}