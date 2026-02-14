package com.academo.model;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name="tb_profiles")
public class Profile {

    @Id
    @Column(name="id")
    private int id;

    @OneToOne
    @MapsId
    @JoinColumn(name = "id")
    private User user;

    @Column(name="name")
    private String fullName;

    @Column(name = "birth_date")
    private LocalDate birthDate;

    @Column(name = "gender", length = 90)
    private Character gender;

    @Column(name = "institution")
    private String institution;

    @Column(name="created_at", updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(name="updated_at")
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    // @Transient -> O dado não deve ser persistido no Banco de Dados
    @Transient
    private Long usageStorage;

//    public Profile() {
//    }
//
//    public Profile(int id, String fullName) {
//        this.id = id;
//        this.fullName = fullName;
//        this.createdAt = LocalDateTime.now();
//        this.updatedAt = LocalDateTime.now();
//    }
//
//    public Profile(ProfilePutDTO profilePutDTO) {
//        fullName = profilePutDTO.fullName();
//        birthDate = profilePutDTO.birthDate();
//        gender = profilePutDTO.gender();
//        institution = profilePutDTO.institution();
//    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public Character getGender() {
        return gender;
    }

    public void setGender(Character gender) {
        this.gender = gender;
    }

    public String getInstitution() {
        return institution;
    }

    public void setInstitution(String institution) {
        this.institution = institution;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Long getUsageStorage() { return usageStorage; }

    public void setUsageStorage(Long usageStorage) { this.usageStorage = usageStorage; }

    public User getUser() {
        return user;
    }
}
