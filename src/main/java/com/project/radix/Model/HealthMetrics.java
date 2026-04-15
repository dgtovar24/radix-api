package com.project.radix.Model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "health_metrics")
public class HealthMetrics {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "fk_treatment_id")
    private Integer fkTreatmentId;

    @Column(name = "fk_patient_id")
    private Integer fkPatientId;

    private Integer bpm;
    private Integer steps;
    private Double distance;
    private Double currentRadiation;

    @Column(name = "recorded_at")
    private LocalDateTime recordedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_treatment_id", insertable = false, updatable = false)
    private Treatment treatment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_patient_id", insertable = false, updatable = false)
    private Patient patient;

    public HealthMetrics() {}

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public Integer getFkTreatmentId() { return fkTreatmentId; }
    public void setFkTreatmentId(Integer fkTreatmentId) { this.fkTreatmentId = fkTreatmentId; }
    public Integer getFkPatientId() { return fkPatientId; }
    public void setFkPatientId(Integer fkPatientId) { this.fkPatientId = fkPatientId; }
    public Integer getBpm() { return bpm; }
    public void setBpm(Integer bpm) { this.bpm = bpm; }
    public Integer getSteps() { return steps; }
    public void setSteps(Integer steps) { this.steps = steps; }
    public Double getDistance() { return distance; }
    public void setDistance(Double distance) { this.distance = distance; }
    public Double getCurrentRadiation() { return currentRadiation; }
    public void setCurrentRadiation(Double currentRadiation) { this.currentRadiation = currentRadiation; }
    public LocalDateTime getRecordedAt() { return recordedAt; }
    public void setRecordedAt(LocalDateTime recordedAt) { this.recordedAt = recordedAt; }
    public Treatment getTreatment() { return treatment; }
    public void setTreatment(Treatment treatment) { this.treatment = treatment; }
    public Patient getPatient() { return patient; }
    public void setPatient(Patient patient) { this.patient = patient; }
}