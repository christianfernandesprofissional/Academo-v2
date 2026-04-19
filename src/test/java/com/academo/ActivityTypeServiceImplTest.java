package com.academo;

import com.academo.controller.dtos.activityType.ActivityTypeDTO;
import com.academo.controller.dtos.activityType.SaveActivityTypeDTO;
import com.academo.controller.dtos.activityType.UpdateActivityTypeDTO;
import com.academo.model.ActivityType;
import com.academo.model.Period;
import com.academo.model.User;
import com.academo.repository.ActivityTypeRepository;
import com.academo.service.activityType.ActivityTypeServiceImpl;
import com.academo.service.calculation.ICalculationService;
import com.academo.service.period.IPeriodService;
import com.academo.service.user.IUserService;
import com.academo.util.exceptions.NotAllowedInsertionException;
import com.academo.util.exceptions.activityType.ActivityTypeExistsException;
import com.academo.util.exceptions.activityType.ActivityTypeNotFoundException;
import com.academo.util.exceptions.period.PeriodNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
class ActivityTypeServiceImplTest {

    @Mock
    private ActivityTypeRepository repository;

    @Mock
    private IUserService userService;

    @Mock
    private IPeriodService periodService;

    @Mock
    private ICalculationService calculationService;

    @InjectMocks
    private ActivityTypeServiceImpl service;

    private ActivityType activityType;
    private User user;

    @BeforeEach
    void setup() {
        user = new User();
        user.setId(1);

        activityType = new ActivityType();
        activityType.setId(10);
        activityType.setName("Prova");
        activityType.setUser(user);

        Period period = new Period();
        period.setId(1);
        activityType.setPeriod(period);
    }

    @Test
    void shouldFindAll() {
        PageRequest pageable = PageRequest.of(0, 10);

        when(repository.findAllByPeriodIdAndUserId(1, 1, pageable))
                .thenReturn(new PageImpl<>(List.of(activityType), pageable, 1));

        Page<ActivityTypeDTO> result = service.findAll(1, 1, pageable);

        assertEquals(1, result.getTotalElements());
        verify(repository).findAllByPeriodIdAndUserId(1, 1, pageable);
    }

    @Test
    void shouldFindById() {
        when(repository.findByIdAndUserId(10, 1))
                .thenReturn(Optional.of(activityType));

        ActivityType result = service.findById(10, 1);

        assertNotNull(result);
    }

    @Test
    void shouldThrowWhenNotFound() {
        when(repository.findByIdAndUserId(10, 1))
                .thenReturn(Optional.empty());

        assertThrows(ActivityTypeNotFoundException.class,
                () -> service.findById(10, 1));
    }

    @Test
    void shouldFindDTO() {
        when(repository.findByIdAndUserId(10, 1))
                .thenReturn(Optional.of(activityType));

        ActivityTypeDTO result = service.findDTO(10, 1);

        assertNotNull(result);
    }

    @Test
    void shouldCreateActivityType() {
        SaveActivityTypeDTO dto = new SaveActivityTypeDTO(
                "Prova",
                "Desc",
                1
        );

        when(periodService.existsById(1)).thenReturn(true);
        when(repository.existsByNameAndUserIdAndPeriodId("Prova", 1, 1))
                .thenReturn(false);
        when(userService.findById(1)).thenReturn(user);

        when(repository.save(any())).thenAnswer(inv -> {
            ActivityType at = inv.getArgument(0);
            at.setId(10);
            return at;
        });

        when(repository.findByIdAndUserId(10, 1))
                .thenReturn(Optional.of(activityType));

        ActivityTypeDTO result = service.create(1, dto);

        assertNotNull(result);
        verify(repository).save(any());
    }

    @Test
    void shouldThrowWhenPeriodNotFound() {
        SaveActivityTypeDTO dto = new SaveActivityTypeDTO("Prova", "Desc", 1);

        when(periodService.existsById(1)).thenReturn(false);

        assertThrows(PeriodNotFoundException.class,
                () -> service.create(1, dto));
    }

    @Test
    void shouldThrowWhenActivityTypeAlreadyExists() {
        SaveActivityTypeDTO dto = new SaveActivityTypeDTO("Prova", "Desc", 1);

        when(periodService.existsById(1)).thenReturn(true);
        when(repository.existsByNameAndUserIdAndPeriodId("Prova", 1, 1))
                .thenReturn(true);

        assertThrows(ActivityTypeExistsException.class,
                () -> service.create(1, dto));
    }

    @Test
    void shouldUpdateActivityType() {
        UpdateActivityTypeDTO dto = new UpdateActivityTypeDTO(
                "Novo Nome",
                "Nova Desc",
                50,
                1
        );

        when(repository.findByIdAndUserId(10, 1))
                .thenReturn(Optional.of(activityType));

        when(calculationService.sumWeights(any()))
                .thenReturn(new BigDecimal("0.5"));

        when(repository.save(any())).thenReturn(activityType);

        ActivityTypeDTO result = service.update(1, 10, dto);

        assertNotNull(result);
        verify(repository).save(activityType);
    }

    @Test
    void shouldThrowWhenWeightExceedsOne() {
        UpdateActivityTypeDTO dto = new UpdateActivityTypeDTO(
                "Nome",
                "Desc",
                80,
                1
        );

        when(repository.findByIdAndUserId(10, 1))
                .thenReturn(Optional.of(activityType));

        when(calculationService.sumWeights(any()))
                .thenReturn(new BigDecimal("1.5"));

        assertThrows(NotAllowedInsertionException.class,
                () -> service.update(1, 10, dto));
    }

    @Test
    void shouldThrowWhenUpdatingOtherUser() {
        User other = new User();
        other.setId(2);
        activityType.setUser(other);

        UpdateActivityTypeDTO dto = new UpdateActivityTypeDTO(
                "Nome",
                "Desc",
                50,
                1
        );

        when(repository.findByIdAndUserId(10, 1))
                .thenReturn(Optional.of(activityType));

        assertThrows(NotAllowedInsertionException.class,
                () -> service.update(1, 10, dto));
    }

    @Test
    void shouldDeleteActivityType() {
        when(repository.findByIdAndUserId(10, 1))
                .thenReturn(Optional.of(activityType));

        service.delete(1, 10);

        verify(repository).deleteById(10);
    }

    @Test
    void shouldThrowWhenDeletingOtherUser() {
        User other = new User();
        other.setId(2);
        activityType.setUser(other);

        when(repository.findByIdAndUserId(10, 1))
                .thenReturn(Optional.of(activityType));

        assertThrows(NotAllowedInsertionException.class,
                () -> service.delete(1, 10));
    }
}