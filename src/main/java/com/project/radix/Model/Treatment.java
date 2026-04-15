package com.project.radix.Model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "treatments")
public class Treatment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "fk_patient_id")
    private Integer fkPatientId;

    @Column(name = "fk_doctor_id")
    private Integer fkDoctorId;

    @Column(name = "fk_radioisotope_id")
    private Integer fkRadioisotopeId;

    @Column(name = "fk_smartwatch_id")
    private Integer fkSmartwatchId;

    @Column(name = "fk_unit_id")
    private Integer fkUnitId;

    private Integer room;

    @Column(name = "initial_dose")
    private Double initialDose;

    @Column(name = "safety_threshold")
    private Double safetyThreshold;

    @Column(name = "isolation_days")
    private Integer isolationDays;

    @Column(name = "start_date")
    private LocalDateTime startDate;

    @Column(name = "end_date")
    private LocalDateTime endDate;

    @Column(name = "is_active")
    private Boolean isActive = true;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_patient_id", insertable = false, updatable = false)
    private Patient patient;

    public Treatment() {}

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public Integer getFkPatientId() { return fkPatientId; }
    public void setFkPatientId(Integer fkPatientId) { this.fkPatientId = fkPatientId; }
    public Integer getFkDoctorId() { return fkDoctorId; }
    public void setFkDoctorId(Integer fkDoctorId) { this.fkDoctorId = fkDoctorId; }
    public Integer getFkRadioisotopeId() { return fkRadioisotopeId; }
    public void setFkRadioisotopeId(Integer fkRadioisotopeId) { this.fkRadioisotopeId = fkRadioisotopeId; }
    public Integer getFkSmartwatchId() { return fkSmartwatchId; }
    public void setFkSmartwatchId(Integer fkSmartwatchId) { this.fkSmartwatchId = fkSmartwatchId; }
    public Integer getFkUnitId() { return fkUnitId; }
    public void setFkUnitId(Integer fkUnitId) { this.fkUnitId = fkUnitId; }
    public Integer getRoom() { return room; }
    public void setRoom(Integer room) { this.room = room; }
    public Double getInitialDose() { return initialDose; }
    public void setInitialDose(Double initialDose) { this.initialDose = initialDose; }
    public Double getSafetyThreshold() { return safetyThreshold; }
    public void setSafetyThreshold(Double safetyThreshold) { this.safetyThreshold = safetyThreshold; }
    public Integer getIsolationDays() { return isolationDays; }
    public void setIsolationDays(Integer isolationDays) { this.isolationDays = isolationDays; }
    public LocalDateTime getStartDate() { return startDate; }
    public void setStartDate(LocalDateTime startDate) { this.startDate = startDate; }
    public LocalDateTime getEndDate() { return endDate; }
    public void setEndDate(LocalDateTime endDate) { this.endDate = endDate; }
    public Boolean getIsActive() { return isActive; }
    public void setIsActive(Boolean isActive) { this.isActive = isActive; }
    public Patient getPatient() { return patient; }
    public void setPatient(Patient patient) { this.patient = patient; }
}