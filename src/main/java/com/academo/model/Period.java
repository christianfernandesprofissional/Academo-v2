package com.academo.model;

import com.academo.model.enums.PeriodName;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

public class Period {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "subject_id")
    private Subject subject;

    @Column(name = "user_id")
    private User user;

    @Column(name = "name")
    private PeriodName name;

    @Column(name = "grade")
    private Double grade;

    @Column(name = "weight")
    private Double weight;

    public Period(){}

    public Period(Integer id, Subject subject, User user, PeriodName name, Double grade, Double weight) {
        this.id = id;
        this.subject = subject;
        this.user = user;
        this.name = name;
        this.grade = grade;
        this.weight = weight;
    }
}
