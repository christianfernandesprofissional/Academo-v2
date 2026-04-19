package com.academo;

import com.academo.controller.dtos.period.PeriodDTO;
import com.academo.controller.dtos.period.SavePeriodDTO;
import com.academo.controller.dtos.period.UpdatePeriodDTO;
import com.academo.model.Period;
import com.academo.model.Subject;
import com.academo.model.User;
import com.academo.model.enums.period.PeriodName;
import com.academo.repository.PeriodRepository;
import com.academo.repository.SubjectRepository;
import com.academo.service.calculation.ICalculationService;
import com.academo.service.period.PeriodServiceImpl;
import com.academo.util.exceptions.NotAllowedInsertionException;
import com.academo.util.exceptions.period.PeriodAlreadyExistsException;
import com.academo.util.exceptions.period.PeriodNotFoundException;
import com.academo.util.exceptions.subject.SubjectNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
class PeriodServiceImplTest {

    @Mock
    private PeriodRepository repository;

    @Mock
    private SubjectRepository subjectRepository;

    @Mock
    private ICalculationService calculationService;

    @InjectMocks
    private PeriodServiceImpl periodService;

    private Subject subject;
    private Period period;

    @BeforeEach
    void setup() {
        subject = new Subject();
        subject.setId(1);
        subject.setPeriods(new HashSet<>());

        period = new Period();
        period.setId(10);
        period.setName(PeriodName.P1.name());
        period.setSubject(subject);

        User user = new User();
        user.setId(1);
        period.setUser(user);
    }

    @Test
    void shouldFindAll() {
        PageRequest pageable = PageRequest.of(0, 10);

        when(repository.findAllByUserIdAndSubjectId(1, 1, pageable))
                .thenReturn(new PageImpl<>(List.of(period), pageable, 1));

        Page<PeriodDTO> result = periodService.findAll(1, 1, pageable);

        assertEquals(1, result.getTotalElements());
        verify(repository).findAllByUserIdAndSubjectId(1, 1, pageable);
    }

    @Test
    void shouldFindById() {
        when(repository.findByIdAndUserId(10, 1))
                .thenReturn(Optional.of(period));

        PeriodDTO result = periodService.findById(1, 10);

        assertNotNull(result);
        verify(repository).findByIdAndUserId(10, 1);
    }

    @Test
    void shouldThrowWhenPeriodNotFound() {
        when(repository.findByIdAndUserId(10, 1))
                .thenReturn(Optional.empty());

        assertThrows(PeriodNotFoundException.class,
                () -> periodService.findById(1, 10));
    }

    @Test
    void shouldCreatePeriod() {
        SavePeriodDTO dto = new SavePeriodDTO(1, "EXAME", new BigDecimal("5"), 50);

        when(subjectRepository.findById(1)).thenReturn(Optional.of(subject));
        when(repository.save(any())).thenAnswer(inv -> {
            Period p = inv.getArgument(0);
            p.setId(10);
            return p;
        });

        PeriodDTO result = periodService.create(1, dto);

        assertNotNull(result);
        verify(repository).save(any());
        verify(calculationService).updateSubjectAverage(1);
    }

    @Test
    void shouldThrowWhenInvalidPeriodName() {
        SavePeriodDTO dto = new SavePeriodDTO(1, "INVALID", new BigDecimal("5"), 50);

        assertThrows(NotAllowedInsertionException.class,
                () -> periodService.create(1, dto));
    }

    @Test
    void shouldThrowWhenSubjectNotFound() {
        SavePeriodDTO dto = new SavePeriodDTO(1, "EXAME", new BigDecimal("5"), 50);

        when(subjectRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(SubjectNotFoundException.class,
                () -> periodService.create(1, dto));
    }

    @Test
    void shouldThrowWhenPeriodAlreadyExists() {
        Period existing = new Period();
        existing.setName(PeriodName.EXAME.name());

        subject.setPeriods(new HashSet<>(Set.of(existing)));

        SavePeriodDTO dto = new SavePeriodDTO(1, "EXAME", new BigDecimal("5"), 50);

        when(subjectRepository.findById(1)).thenReturn(Optional.of(subject));

        assertThrows(PeriodAlreadyExistsException.class,
                () -> periodService.create(1, dto));
    }

    @Test
    void shouldUpdatePeriod() {
        UpdatePeriodDTO dto = new UpdatePeriodDTO(1,"P1", new BigDecimal("6"), 50);

        when(repository.findByIdAndUserId(10, 1)).thenReturn(Optional.of(period));
        when(subjectRepository.existsById(1)).thenReturn(true);
        when(repository.findById(10)).thenReturn(Optional.of(period));
        when(calculationService.sumWeights(any())).thenReturn(new BigDecimal("0.5"));

        PeriodDTO result = periodService.update(1, 10, dto);

        assertNotNull(result);
        verify(repository).save(period);
        verify(calculationService).updatePeriodAverage(10);
        verify(calculationService).updateSubjectAverage(1);
    }

    @Test
    void shouldThrowWhenUpdatingWithInvalidSubject() {
        UpdatePeriodDTO dto = new UpdatePeriodDTO(2,"P1", new BigDecimal("6"), 50);

        when(repository.findByIdAndUserId(10, 1)).thenReturn(Optional.of(period));

        assertThrows(NotAllowedInsertionException.class,
                () -> periodService.update(1, 10, dto));
    }

    @Test
    void shouldThrowWhenWeightsExceedOne() {
        UpdatePeriodDTO dto = new UpdatePeriodDTO(1,"P1", new BigDecimal("6"), 80);

        when(repository.findByIdAndUserId(10, 1)).thenReturn(Optional.of(period));
        when(subjectRepository.existsById(1)).thenReturn(true);
        when(calculationService.sumWeights(any())).thenReturn(new BigDecimal("1.5"));

        assertThrows(NotAllowedInsertionException.class,
                () -> periodService.update(1, 10, dto));
    }

    @Test
    void shouldDeleteExamPeriod() {
        Period exam = new Period();
        exam.setId(20);
        exam.setName(PeriodName.EXAME.name());
        exam.setSubject(subject);

        when(repository.findByIdAndUserId(20, 1)).thenReturn(Optional.of(exam));

        periodService.delete(1, 1, 20);

        verify(repository).deleteByIdAndSubjectIdAndUserId(20, 1, 1);
        verify(calculationService).updateSubjectAverage(1);
    }

    @Test
    void shouldThrowWhenDeletingNonExamPeriod() {
        when(repository.findByIdAndUserId(10, 1)).thenReturn(Optional.of(period));

        assertThrows(NotAllowedInsertionException.class,
                () -> periodService.delete(1, 1, 10));
    }

    @Test
    void shouldCheckIfExists() {
        when(repository.existsById(10)).thenReturn(true);

        boolean result = periodService.existsById(10);

        assertTrue(result);
        verify(repository).existsById(10);
    }
}
