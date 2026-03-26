package com.proyecto.radix.Model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "Treatment")
public class Tratamiento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "patient_id")
    private Integer patientId;

    @Column(name = "facultatiu_id")
    private Integer facultatiuId;

    @Column(name = "radioisotope_id")
    private Integer radioisotopeId;

    @Column(name = "rellotge_id")
    private Integer rellotgeId;

    @Column(name = "unit_id")
    private Integer unitId;

    private Integer room;

    @Column(name = "start_date")
    private LocalDateTime startDate;

    @Column(name = "expected_end_date")
    private LocalDateTime expectedEndDate;

    @Column(name = "isolation_days")
    private Integer isolationDays;

    @Column(name = "initial_radiation")
    private Double initialRadiation;

    private String status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", insertable = false, updatable = false)
    private Paciente paciente;

    public Tratamiento() {}

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public Integer getPatientId() { return patientId; }
    public void setPatientId(Integer patientId) { this.patientId = patientId; }
    public Integer getFacultatiuId() { return facultatiuId; }
    public void setFacultatiuId(Integer facultatiuId) { this.facultatiuId = facultatiuId; }
    public Integer getRadioisotopeId() { return radioisotopeId; }
    public void setRadioisotopeId(Integer radioisotopeId) { this.radioisotopeId = radioisotopeId; }
    public Integer getRellotgeId() { return rellotgeId; }
    public void setRellotgeId(Integer rellotgeId) { this.rellotgeId = rellotgeId; }
    public Integer getUnitId() { return unitId; }
    public void setUnitId(Integer unitId) { this.unitId = unitId; }
    public Integer getRoom() { return room; }
    public void setRoom(Integer room) { this.room = room; }
    public LocalDateTime getStartDate() { return startDate; }
    public void setStartDate(LocalDateTime startDate) { this.startDate = startDate; }
    public LocalDateTime getExpectedEndDate() { return expectedEndDate; }
    public void setExpectedEndDate(LocalDateTime expectedEndDate) { this.expectedEndDate = expectedEndDate; }
    public Integer getIsolationDays() { return isolationDays; }
    public void setIsolationDays(Integer isolationDays) { this.isolationDays = isolationDays; }
    public Double getInitialRadiation() { return initialRadiation; }
    public void setInitialRadiation(Double initialRadiation) { this.initialRadiation = initialRadiation; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public Paciente getPaciente() { return paciente; }
    public void setPaciente(Paciente paciente) { this.paciente = paciente; }
}
