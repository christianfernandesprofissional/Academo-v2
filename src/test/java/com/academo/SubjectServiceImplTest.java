package com.academo;

import com.academo.controller.dtos.period.SavePeriodDTO;
import com.academo.controller.dtos.subject.CreateSubjectDTO;
import com.academo.controller.dtos.subject.SubjectDTO;
import com.academo.controller.dtos.subject.UpdateSubjectDTO;
import com.academo.model.Group;
import com.academo.model.Subject;
import com.academo.model.User;
import com.academo.repository.GroupRepository;
import com.academo.repository.SubjectRepository;
import com.academo.service.calculation.ICalculationService;
import com.academo.service.period.IPeriodService;
import com.academo.service.subject.SubjectServiceImpl;
import com.academo.service.user.IUserService;
import com.academo.util.exceptions.NotAllowedInsertionException;
import com.academo.util.exceptions.subject.SubjectNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
class SubjectServiceImplTest {

    @Mock
    private SubjectRepository subjectRepository;

    @Mock
    private GroupRepository groupRepository;

    @Mock
    private IUserService userService;

    @Mock
    private IPeriodService periodService;

    @Mock
    private ICalculationService calculationService;

    @InjectMocks
    private SubjectServiceImpl subjectService;

    private User user;
    private Subject subject;

    @BeforeEach
    void setup() {
        user = new User();
        user.setId(1);

        subject = new Subject();
        subject.setId(10);
        subject.setName("Math");
        subject.setUser(user);
    }

    @Test
    void shouldFindAllSubjects() {
        when(subjectRepository.findAllByUserId(1)).thenReturn(List.of(subject));

        List<SubjectDTO> result = subjectService.findAll(1);

        assertEquals(1, result.size());
        verify(subjectRepository).findAllByUserId(1);
    }

    @Test
    void shouldFindById() {
        when(subjectRepository.findByIdAndUserId(10, 1))
                .thenReturn(Optional.of(subject));

        SubjectDTO result = subjectService.findById(10, 1);

        assertNotNull(result);
        verify(subjectRepository).findByIdAndUserId(10, 1);
    }

    @Test
    void shouldThrowWhenSubjectNotFound() {
        when(subjectRepository.findByIdAndUserId(10, 1))
                .thenReturn(Optional.empty());

        assertThrows(SubjectNotFoundException.class,
                () -> subjectService.findById(10, 1));
    }

    @Test
    void shouldCreateSubjectWithDefaultPeriods() {
        CreateSubjectDTO dto = new CreateSubjectDTO("Math", "Desc");

        when(userService.findById(1)).thenReturn(user);
        when(subjectRepository.save(any())).thenAnswer(inv -> {
            Subject s = inv.getArgument(0);
            s.setId(10);
            return s;
        });

        SubjectDTO result = subjectService.create(1, dto);

        assertNotNull(result);
        verify(periodService, times(2)).create(eq(1), any(SavePeriodDTO.class));
        verify(subjectRepository).save(any());
    }

    @Test
    void shouldUpdateSubject() {
        UpdateSubjectDTO dto = new UpdateSubjectDTO(
                "New Name",
                "New Desc",
                new BigDecimal("6"),
                "MEDIA_ARITMETICA", // ✅ corrigido
                true
        );

        when(subjectRepository.findById(10)).thenReturn(Optional.of(subject));
        when(subjectRepository.save(any())).thenReturn(subject);

        SubjectDTO result = subjectService.update(1, 10, dto);

        assertNotNull(result);
        verify(calculationService).updateSubjectAverage(10);
        verify(subjectRepository).save(subject);
    }

    @Test
    void shouldThrowWhenUpdatingOtherUserSubject() {
        User otherUser = new User();
        otherUser.setId(2);
        subject.setUser(otherUser);

        UpdateSubjectDTO dto = new UpdateSubjectDTO(
                "Name",
                "Desc",
                new BigDecimal("6"),
                "MEDIA_ARITMETICA",
                true
        );

        when(subjectRepository.findById(10)).thenReturn(Optional.of(subject));

        assertThrows(NotAllowedInsertionException.class,
                () -> subjectService.update(1, 10, dto));
    }

    @Test
    void shouldDeleteSubject() {
        Group group = new Group();

        // ✅ HashSet mutável (igual ao seu model)
        Set<Subject> subjects = new HashSet<>();
        subjects.add(subject);

        group.setSubjects(subjects);

        subject.setUser(user);

        ArrayList<Group> groups = new ArrayList<>();
        groups.add(group);

        subject.setGroups(groups);

        when(subjectRepository.findById(10)).thenReturn(Optional.of(subject));

        subjectService.delete(1, 10);

        assertFalse(group.getSubjects().contains(subject)); // valida remoção
        verify(groupRepository).save(group);
        verify(subjectRepository).deleteById(10);
    }

    @Test
    void shouldThrowWhenDeletingOtherUserSubject() {
        User otherUser = new User();
        otherUser.setId(2);
        subject.setUser(otherUser);

        when(subjectRepository.findById(10)).thenReturn(Optional.of(subject));

        assertThrows(NotAllowedInsertionException.class,
                () -> subjectService.delete(1, 10));
    }
}