package com.academo.model;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;


@Entity
@Table(name = "periods")
public class Period {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "subject_id", nullable = false)
    private Subject subject;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "name")
    private String name;

    @Column(name = "grade")
    private BigDecimal grade = new BigDecimal("0");

    @Column(name = "weight")
    private BigDecimal weight = new BigDecimal("1");

    @OneToMany(mappedBy = "period",
                cascade = CascadeType.REMOVE)
    private Set<ActivityType> activityTypeList = new HashSet<>();

    public Period(){}

    public Period(Integer id, Subject subject, User user, String name, BigDecimal grade, BigDecimal weight) {
        this.id = id;
        this.subject = subject;
        this.user = user;
        this.name = name;
        this.grade = grade;
        this.weight = weight;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getGrade() {
        return grade;
    }

    public void setGrade(BigDecimal grade) {
        this.grade = grade;
    }

    public BigDecimal getWeight() {
        return weight;
    }

    public void setWeight(BigDecimal weight) {
        this.weight = weight;
    }

    public Set<ActivityType> getActivityTypeList() {
        return activityTypeList;
    }

    public void setActivityTypeList(HashSet<ActivityType> activityTypeList) {
        this.activityTypeList = activityTypeList;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Period period = (Period) o;
        return name.equals(period.name);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(name);
    }

}
