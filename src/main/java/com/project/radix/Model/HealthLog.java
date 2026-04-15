package com.project.radix.Model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "health_logs")
public class HealthLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "fk_patient_id")
    private Integer fkPatientId;

    private Integer bpm;
    private Integer steps;
    private Double distance;

    @Column(name = "timestamp")
    private LocalDateTime timestamp = LocalDateTime.now();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_patient_id", insertable = false, updatable = false)
    private Patient patient;

    public HealthLog() {}

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public Integer getFkPatientId() { return fkPatientId; }
    public void setFkPatientId(Integer fkPatientId) { this.fkPatientId = fkPatientId; }
    public Integer getBpm() { return bpm; }
    public void setBpm(Integer bpm) { this.bpm = bpm; }
    public Integer getSteps() { return steps; }
    public void setSteps(Integer steps) { this.steps = steps; }
    public Double getDistance() { return distance; }
    public void setDistance(Double distance) { this.distance = distance; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
    public Patient getPatient() { return patient; }
    public void setPatient(Patient patient) { this.patient = patient; }
}