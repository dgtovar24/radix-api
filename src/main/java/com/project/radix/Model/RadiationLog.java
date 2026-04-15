package com.project.radix.Model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "radiation_logs")
public class RadiationLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "fk_patient_id")
    private Integer fkPatientId;

    @Column(name = "fk_treatment_id")
    private Integer fkTreatmentId;

    @Column(name = "radiation_level")
    private Double radiationLevel;

    @Column(name = "timestamp")
    private LocalDateTime timestamp = LocalDateTime.now();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_patient_id", insertable = false, updatable = false)
    private Patient patient;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_treatment_id", insertable = false, updatable = false)
    private Treatment treatment;

    public RadiationLog() {}

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public Integer getFkPatientId() { return fkPatientId; }
    public void setFkPatientId(Integer fkPatientId) { this.fkPatientId = fkPatientId; }
    public Integer getFkTreatmentId() { return fkTreatmentId; }
    public void setFkTreatmentId(Integer fkTreatmentId) { this.fkTreatmentId = fkTreatmentId; }
    public Double getRadiationLevel() { return radiationLevel; }
    public void setRadiationLevel(Double radiationLevel) { this.radiationLevel = radiationLevel; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
    public Patient getPatient() { return patient; }
    public void setPatient(Patient patient) { this.patient = patient; }
    public Treatment getTreatment() { return treatment; }
    public void setTreatment(Treatment treatment) { this.treatment = treatment; }
}