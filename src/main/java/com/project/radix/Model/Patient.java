package com.project.radix.Model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "patients")
public class Patient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "full_name", nullable = false)
    private String fullName;

    @Column(name = "phone")
    private String phone;

    @Column(name = "address")
    private String address;

    @Column(name = "is_active")
    private Boolean isActive = true;

    @Column(name = "family_access_code", unique = true)
    private String familyAccessCode;

    @Column(name = "fk_user_id")
    private Integer fkUserId;

    @Column(name = "fk_doctor_id")
    private Integer fkDoctorId;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_user_id", insertable = false, updatable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_doctor_id", insertable = false, updatable = false)
    private User doctor;

    public Patient() {}

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    public Boolean getIsActive() { return isActive; }
    public void setIsActive(Boolean isActive) { this.isActive = isActive; }
    public String getFamilyAccessCode() { return familyAccessCode; }
    public void setFamilyAccessCode(String familyAccessCode) { this.familyAccessCode = familyAccessCode; }
    public Integer getFkUserId() { return fkUserId; }
    public void setFkUserId(Integer fkUserId) { this.fkUserId = fkUserId; }
    public Integer getFkDoctorId() { return fkDoctorId; }
    public void setFkDoctorId(Integer fkDoctorId) { this.fkDoctorId = fkDoctorId; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    public User getDoctor() { return doctor; }
    public void setDoctor(User doctor) { this.doctor = doctor; }
}
