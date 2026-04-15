package com.project.radix.Model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "game_sessions")
public class GameSession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "fk_patient_id")
    private Integer fkPatientId;

    private Integer score = 0;

    @Column(name = "level_reached")
    private Integer levelReached = 1;

    @Column(name = "played_at")
    private LocalDateTime playedAt = LocalDateTime.now();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_patient_id", insertable = false, updatable = false)
    private Patient patient;

    public GameSession() {}

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public Integer getFkPatientId() { return fkPatientId; }
    public void setFkPatientId(Integer fkPatientId) { this.fkPatientId = fkPatientId; }
    public Integer getScore() { return score; }
    public void setScore(Integer score) { this.score = score; }
    public Integer getLevelReached() { return levelReached; }
    public void setLevelReached(Integer levelReached) { this.levelReached = levelReached; }
    public LocalDateTime getPlayedAt() { return playedAt; }
    public void setPlayedAt(LocalDateTime playedAt) { this.playedAt = playedAt; }
    public Patient getPatient() { return patient; }
    public void setPatient(Patient patient) { this.patient = patient; }
}