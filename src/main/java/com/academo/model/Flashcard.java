package com.academo.model;

import com.academo.model.enums.flashcard.CardLevel;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcType;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.dialect.PostgreSQLEnumJdbcType;

import java.time.LocalDateTime;

@Entity
@Table(name = "flashcards")
public class Flashcard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "subject_id")
    private Subject subject;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "front_part")
    private String frontPart;

    @Column(name = "back_part")
    private String backPart;

    @Enumerated(EnumType.STRING)
    @Column(name = "level", columnDefinition = "card_level")
    @JdbcType(PostgreSQLEnumJdbcType.class)
    private CardLevel level;

    @Column(name = "created_at", updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    public Flashcard(){}

    public Flashcard(Integer id, Subject subject, User user, String frontPart, String backPart, CardLevel level) {
        this.id = id;
        this.subject = subject;
        this.user = user;
        this.frontPart = frontPart;
        this.backPart = backPart;
        this.level = level;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getFrontPart() {
        return frontPart;
    }

    public void setFrontPart(String frontPart) {
        this.frontPart = frontPart;
    }

    public String getBackPart() {
        return backPart;
    }

    public void setBackPart(String backPart) {
        this.backPart = backPart;
    }

    public CardLevel getLevel() {
        return level;
    }

    public void setLevel(CardLevel level) {
        this.level = level;
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
}
