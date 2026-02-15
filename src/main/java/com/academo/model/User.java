package com.academo.model;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

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

    @Column(name = "password")
    private String password;

    @Column(name = "email")
    private String email;

    @Column(name = "created_at", updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @Column(name = "is_account_activated", columnDefinition = "default false", nullable = false)
    private Boolean isAccountActivated;

    @Column(name = "activation_account_token_expiration")
    private LocalDateTime activationAccountTokenExpiration;

    @Column(name = "storage_usage")
    private Long storageUsage;


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

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
