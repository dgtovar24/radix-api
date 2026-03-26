package com.proyecto.radix.Model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "Alerta_Metge")
public class AlertaMedico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "treatment_id")
    private Integer treatmentId;

    private String missatge;
    private String tipusAlerta;
    private Boolean resolta;

    @Column(name = "creada_el")
    private LocalDateTime creadaEl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "treatment_id", insertable = false, updatable = false)
    private Tratamiento tratamiento;

    public AlertaMedico() {}

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public Integer getTreatmentId() { return treatmentId; }
    public void setTreatmentId(Integer treatmentId) { this.treatmentId = treatmentId; }
    public String getMissatge() { return missatge; }
    public void setMissatge(String missatge) { this.missatge = missatge; }
    public String getTipusAlerta() { return tipusAlerta; }
    public void setTipusAlerta(String tipusAlerta) { this.tipusAlerta = tipusAlerta; }
    public Boolean getResolta() { return resolta; }
    public void setResolta(Boolean resolta) { this.resolta = resolta; }
    public LocalDateTime getCreadaEl() { return creadaEl; }
    public void setCreadaEl(LocalDateTime creadaEl) { this.creadaEl = creadaEl; }
    public Tratamiento getTratamiento() { return tratamiento; }
    public void setTratamiento(Tratamiento tratamiento) { this.tratamiento = tratamiento; }
}
