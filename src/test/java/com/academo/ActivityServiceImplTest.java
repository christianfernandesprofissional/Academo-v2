package com.academo;

import com.academo.controller.dtos.activity.ActivityDTO;
import com.academo.controller.dtos.activity.SaveActivityDTO;
import com.academo.controller.dtos.subject.SubjectDTO;
import com.academo.model.*;
import com.academo.repository.ActivityRepository;
import com.academo.service.activity.ActivityServiceImpl;
import com.academo.service.activityType.IActivityTypeService;
import com.academo.service.calculation.ICalculationService;
import com.academo.service.subject.ISubjectService;
import com.academo.service.user.IUserService;
import com.academo.util.exceptions.activity.ActivityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
class ActivityServiceImplTest {

    @Mock
    private ActivityRepository activityRepository;

    @Mock
    private IUserService userService;

    @Mock
    private ISubjectService subjectService;

    @Mock
    private IActivityTypeService activityTypeService;

    @Mock
    private ICalculationService calculationService;

    @InjectMocks
    private ActivityServiceImpl service;

    private Activity activity;
    private User user;
    private ActivityType activityType;
    private Subject subject;

    @BeforeEach
    void setup() {
        user = new User();
        user.setId(1);

        subject = new Subject();
        subject.setId(1);

        activityType = new ActivityType();
        activityType.setId(1);

        Period period = new Period();
        period.setId(1);
        activityType.setPeriod(period);

        activity = new Activity();
        activity.setId(10);
        activity.setUser(user);
        activity.setSubject(subject);
        activity.setActivityType(activityType);
    }

    @Test
    void shouldFindAll() {
        PageRequest pageable = PageRequest.of(0, 10);

        when(activityRepository.findAllByUserId(1, pageable))
                .thenReturn(new PageImpl<>(List.of(activity), pageable, 1));

        Page<ActivityDTO> result = service.findAll(1, pageable);

        assertEquals(1, result.getTotalElements());
        verify(activityRepository).findAllByUserId(1, pageable);
    }

    @Test
    void shouldFindById() {
        when(activityRepository.findByIdAndUserId(10, 1))
                .thenReturn(Optional.of(activity));

        ActivityDTO result = service.findById(1, 10);

        assertNotNull(result);
    }

    @Test
    void shouldThrowWhenNotFound() {
        when(activityRepository.findByIdAndUserId(10, 1))
                .thenReturn(Optional.empty());

        assertThrows(ActivityNotFoundException.class,
                () -> service.findById(1, 10));
    }

    @Test
    void shouldCreateActivity() {
        SaveActivityDTO dto = new SaveActivityDTO(
                LocalDate.now(),
                "Atividade",
                "Desc",
                new BigDecimal("10.0"),
                1,
                1
        );

        SubjectDTO subjectDTO = new SubjectDTO(
                1,
                "Math",
                "Desc",
                true,
                "MEDIA_ARITMETICA",
                new BigDecimal("6.0"),
                new BigDecimal("0.0"),
                null,
                null
        );

        when(userService.findById(1)).thenReturn(user);
        when(activityTypeService.findById(1, 1)).thenReturn(activityType);
        when(subjectService.findById(1, 1)).thenReturn(subjectDTO);

        when(activityRepository.save(any())).thenAnswer(inv -> {
            Activity a = inv.getArgument(0);
            a.setId(10);
            return a;
        });

        ActivityDTO result = service.create(1, dto);

        assertNotNull(result);
        verify(calculationService).updatePeriodAverage(1);
        verify(calculationService).updateSubjectAverage(1);
    }

    @Test
    void shouldUpdateActivity() {
        SaveActivityDTO dto = new SaveActivityDTO(
                LocalDate.now(),
                "Novo Nome",
                "Nova Desc",
                new BigDecimal("9.0"),
                1,
                1
        );

        when(activityRepository.findByIdAndUserId(10, 1))
                .thenReturn(Optional.of(activity));

        when(activityTypeService.findById(1, 1))
                .thenReturn(activityType);

        when(activityRepository.save(any())).thenReturn(activity);

        ActivityDTO result = service.update(1, 10, dto);

        assertNotNull(result);
        verify(activityRepository).save(activity);
        verify(calculationService).updatePeriodAverage(1);
        verify(calculationService).updateSubjectAverage(1);
    }

    @Test
    void shouldThrowWhenUpdatingNotFound() {
        SaveActivityDTO dto = new SaveActivityDTO(
                LocalDate.now(),
                "Nome",
                "Desc",
                new BigDecimal("8.0"),
                1,
                1
        );

        when(activityRepository.findByIdAndUserId(10, 1))
                .thenReturn(Optional.empty());

        assertThrows(ActivityNotFoundException.class,
                () -> service.update(1, 10, dto));
    }

    @Test
    void shouldDeleteActivity() {
        when(activityRepository.findByIdAndUserId(10, 1))
                .thenReturn(Optional.of(activity));

        service.delete(1, 10);

        verify(activityRepository).deleteById(10);
        verify(calculationService).updatePeriodAverage(1);
        verify(calculationService).updateSubjectAverage(1);
    }

    @Test
    void shouldThrowWhenDeletingNotFound() {
        when(activityRepository.findByIdAndUserId(10, 1))
                .thenReturn(Optional.empty());

        assertThrows(ActivityNotFoundException.class,
                () -> service.delete(1, 10));
    }

    @Test
    void shouldCheckIfExistsByName() {
        when(activityRepository.existsActivityByName("Teste"))
                .thenReturn(true);

        boolean result = service.existsByName("Teste");

        assertTrue(result);
    }

    @Test
    void shouldCheckIfExistsById() {
        when(activityRepository.existsById(10))
                .thenReturn(true);

        boolean result = service.existsById(10);

        assertTrue(result);
        verify(activityRepository).existsById(10);
    }

    @Test
    void shouldFindAllBySubjectId() {
        PageRequest pageable = PageRequest.of(0, 10);

        when(subjectService.findById(1, 1))
                .thenReturn(mock(SubjectDTO.class));
        when(activityRepository.findAllBySubjectId(1, pageable))
                .thenReturn(new PageImpl<>(List.of(activity), pageable, 1));

        Page<ActivityDTO> result = service.findAllBySubjectId(1, 1, pageable);

        assertEquals(1, result.getTotalElements());
        verify(activityRepository).findAllBySubjectId(1, pageable);
    }
}