package com.academo.model;

import com.academo.model.enums.PaymentStatus;
import com.academo.model.enums.PlanType;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcType;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.dialect.PostgreSQLEnumJdbcType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "payment_history")
public class PaymentHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "payment_id", nullable = false, length = 255, unique = true)
    private String paymentId;

    @Column(name = "url", nullable = false, length = 255)
    private String url;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", columnDefinition = "payment_status", nullable = false)
    @JdbcType(PostgreSQLEnumJdbcType.class)
    private PaymentStatus status;

    @Column(name = "value", nullable = false)
    private BigDecimal value;

    @Enumerated(EnumType.STRING)
    @Column(name = "plan_type", columnDefinition = "plan_type", nullable = false)
    @JdbcType(PostgreSQLEnumJdbcType.class)
    private PlanType planType;

    @Column(name="created_at", updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(name="updated_at")
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }

    public PaymentStatus getStatus() {
        return status;
    }

    public void setStatus(PaymentStatus status) {
        this.status = status;
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public PlanType getPlanType() {
        return planType;
    }

    public void setPlanType(PlanType planType) {
        this.planType = planType;
    }
}
