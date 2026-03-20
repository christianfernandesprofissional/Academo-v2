package com.academo.model;

import com.academo.model.enums.PlanType;
import com.academo.model.enums.UserRole;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcType;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.dialect.PostgreSQLEnumJdbcType;

import java.time.LocalDateTime;

@Entity
@Table(name = "tb_users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "email", unique = true, nullable = false)
    private String email;

    @Column(name = "created_at", updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @Column(name = "is_account_activated", columnDefinition = "boolean default false", nullable = false)
    private Boolean isAccountActivated = Boolean.FALSE;

    @Column(name = "activation_account_token_expiration")
    private LocalDateTime activationAccountTokenExpiration;

    @Column(name = "storage_usage")
    private Long storageUsage = 0L;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Profile profile;

    @Enumerated(EnumType.STRING)
    @Column(name = "plan_type", columnDefinition = "plan_type")
    @JdbcType(PostgreSQLEnumJdbcType.class)
    private PlanType planType;

    @Enumerated(EnumType.STRING)
    @Column(name = "user_role", columnDefinition = "user_role")
    @JdbcType(PostgreSQLEnumJdbcType.class)
    private UserRole role;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() { return email; }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Boolean getAccountActivated() {
        return isAccountActivated;
    }

    public void setAccountActivated(Boolean accountActivated) {
        isAccountActivated = accountActivated;
    }

    public LocalDateTime getActivationAccountTokenExpiration() {
        return activationAccountTokenExpiration;
    }

    public void setActivationAccountTokenExpiration(LocalDateTime activationAccountTokenExpiration) {
        this.activationAccountTokenExpiration = activationAccountTokenExpiration;
    }

    public Long getStorageUsage() {
        return this.storageUsage;
    }

    public void setStorageUsage(Long storageUsage) {
        this.storageUsage = storageUsage;
    }

    public void increaseStorageUsage(Long size){
        this.storageUsage += size;
    }

    public void decreaseStorageUsage(Long size) {
        this.storageUsage -= size;
    }

    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }

    public PlanType getPlanType() {
        return planType;
    }

    public void setPlanType(PlanType planType) {
        this.planType = planType;
    }

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
