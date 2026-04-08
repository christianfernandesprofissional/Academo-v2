package com.academo;

import com.academo.model.*;
import com.academo.model.enums.CalculationType;
import com.academo.model.enums.PeriodName;
import com.academo.repository.PeriodRepository;
import com.academo.repository.SubjectRepository;
import com.academo.service.calculation.CalculationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CalculationServiceTest {

    @Mock
    private SubjectRepository subjectRepository;

    @Mock
    private PeriodRepository periodRepository;

    @InjectMocks
    private CalculationService service;

    // =========================
    // SUBJECT - MÉDIA PONDERADA
    // =========================

    @Test
    void shouldCalculateWeightedAverageWithoutExam() {
        Subject subject = new Subject();
        subject.setCalculationType(CalculationType.MEDIA_PONDERADA);

        Period p1 = createPeriod("P1", "7", "0.5");
        Period p2 = createPeriod("P2", "9", "0.5");

        subject.getPeriods().addAll(List.of(p1, p2));

        when(subjectRepository.findById(1)).thenReturn(Optional.of(subject));

        service.updateSubjectAverage(1);

        assertEquals(new BigDecimal("8.00"), subject.getFinalGrade());
        verify(subjectRepository).save(subject);
    }

    @Test
    void shouldCalculateWeightedAverageWithExam() {
        Subject subject = new Subject();
        subject.setCalculationType(CalculationType.MEDIA_PONDERADA);

        Period p1 = createPeriod("P1", "6", "0.5");
        Period p2 = createPeriod("P2", "8", "0.5");
        Period exam = createPeriod("EXAME", "10", "0");

        subject.getPeriods().addAll(List.of(p1, p2, exam));

        when(subjectRepository.findById(1)).thenReturn(Optional.of(subject));

        service.updateSubjectAverage(1);

        assertEquals(new BigDecimal("8.50"), subject.getFinalGrade());
    }

    // =========================
    // SUBJECT - MÉDIA ARITMÉTICA
    // =========================

    @Test
    void shouldCalculateArithmeticAverage() {
        Subject subject = new Subject();
        subject.setCalculationType(CalculationType.MEDIA_ARITMETICA);

        Period p1 = createPeriod("P1", "6", "0");
        Period p2 = createPeriod("P2", "8", "0");

        subject.setPeriods(Set.of(p1, p2));

        when(subjectRepository.findById(1)).thenReturn(Optional.of(subject));

        service.updateSubjectAverage(1);

        assertEquals(new BigDecimal("7.00"), subject.getFinalGrade());
    }

    @Test
    void shouldRemoveLowestGradeWhenHasExam() {
        Subject subject = new Subject();
        subject.setCalculationType(CalculationType.MEDIA_ARITMETICA);

        Period p1 = createPeriod("P1", "5", "0");
        Period p2 = createPeriod("P2", "9", "0");
        Period exam = createPeriod("EXAME", "10", "0");

        subject.setPeriods(Set.of(p1, p2, exam));

        when(subjectRepository.findById(1)).thenReturn(Optional.of(subject));

        service.updateSubjectAverage(1);

        assertEquals(new BigDecimal("9.50"), subject.getFinalGrade());
    }

    // =========================
    // PERIOD
    // =========================

    @Test
    void shouldCalculatePeriodAverage() {
        Period period = new Period();
        period.setName("P1");

        Activity a1 = createActivity("8");
        Activity a2 = createActivity("6");

        ActivityType type = new ActivityType();
        type.setWeight(new BigDecimal("1"));
        type.getActivities().addAll(List.of(a1, a2));

        period.getActivityTypeList().add(type);

        when(periodRepository.findById(1)).thenReturn(Optional.of(period));

        service.updatePeriodAverage(1);

        assertEquals(new BigDecimal("7.00"), period.getGrade());
        verify(periodRepository).save(period);
    }

    @Test
    void shouldNotCalculateWhenPeriodIsExam() {
        Period period = new Period();
        period.setName("EXAME");

        when(periodRepository.findById(1)).thenReturn(Optional.of(period));

        service.updatePeriodAverage(1);

        verify(periodRepository, never()).save(period);
    }

    // =========================
    // SUM WEIGHTS
    // =========================

    @Test
    void shouldSumWeightsIgnoringNulls() {
        List<BigDecimal> pesos = Arrays.asList(
                new BigDecimal("0.5"),
                null,
                new BigDecimal("0.3")
        );

        BigDecimal result = service.sumWeights(pesos);

        assertEquals(new BigDecimal("0.8"), result);
    }

    // =========================
    // HELPERS
    // =========================

    private Period createPeriod(String name, String grade, String weight) {
        Period p = new Period();
        p.setName(name);
        p.setGrade(new BigDecimal(grade));
        p.setWeight(new BigDecimal(weight));
        return p;
    }

    private Activity createActivity(String grade) {
        Activity a = new Activity();
        a.setGrade(new BigDecimal(grade));
        return a;
    }
}