package com.academo;

import com.academo.controller.dtos.group.AssociateSubjectsDTO;
import com.academo.controller.dtos.group.CreateGroupDTO;
import com.academo.controller.dtos.group.GroupDTO;
import com.academo.controller.dtos.group.UpdateGroupDTO;
import com.academo.controller.dtos.subject.SubjectDTO;
import com.academo.model.Group;
import com.academo.model.Subject;
import com.academo.model.User;
import com.academo.repository.GroupRepository;
import com.academo.service.group.GroupServiceImpl;
import com.academo.service.subject.ISubjectService;
import com.academo.service.user.IUserService;
import com.academo.util.exceptions.NotAllowedInsertionException;
import com.academo.util.exceptions.group.GroupNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GroupServiceImplTest {

    @Mock
    private GroupRepository groupRepository;

    @Mock
    private IUserService userService;

    @Mock
    private ISubjectService subjectService;

    @InjectMocks
    private GroupServiceImpl service;

    private User user;
    private Group group;

    @BeforeEach
    void setup() {
        user = new User();
        user.setId(1);
        user.setName("Fabio");

        group = new Group();
        group.setId(10);
        group.setName("Grupo");
        group.setDescription("Desc");
        group.setUser(user);
        group.setSubjects(new HashSet<>());
    }

    @Test
    void shouldFindAll() {
        PageRequest pageable = PageRequest.of(0, 10);
        when(groupRepository.findAllByUserId(1, pageable)).thenReturn(new PageImpl<>(List.of(group), pageable, 1));

        Page<GroupDTO> page = service.findAll(1, pageable);

        assertEquals(1, page.getTotalElements());
    }

    @Test
    void shouldFindById() {
        when(groupRepository.findByIdAndUserId(10, 1)).thenReturn(Optional.of(group));

        GroupDTO dto = service.findById(1, 10);

        assertNotNull(dto);
        assertEquals(10, dto.id());
    }

    @Test
    void shouldThrowWhenGroupNotFound() {
        when(groupRepository.findByIdAndUserId(10, 1)).thenReturn(Optional.empty());

        assertThrows(GroupNotFoundException.class, () -> service.findById(1, 10));
    }

    @Test
    void shouldCreateGroup() {
        CreateGroupDTO dto = new CreateGroupDTO("Nome", "Desc");

        when(userService.findById(1)).thenReturn(user);
        when(groupRepository.save(any())).thenAnswer(inv -> {
            Group g = inv.getArgument(0);
            g.setId(10);
            return g;
        });

        GroupDTO created = service.create(1, dto);

        assertNotNull(created);
        verify(groupRepository).save(any(Group.class));
    }

    @Test
    void shouldUpdateGroup() {
        UpdateGroupDTO dto = new UpdateGroupDTO("Novo", "Nova desc", true);

        when(groupRepository.findByIdAndUserId(10, 1)).thenReturn(Optional.of(group));
        when(groupRepository.save(any())).thenReturn(group);

        GroupDTO updated = service.update(1, 10, dto);

        assertNotNull(updated);
        verify(groupRepository).save(group);
    }

    @Test
    void shouldThrowWhenUpdatingOtherUserGroup() {
        User other = new User();
        other.setId(2);
        group.setUser(other);

        UpdateGroupDTO dto = new UpdateGroupDTO("Novo", "Desc", true);

        when(groupRepository.findByIdAndUserId(10, 1)).thenReturn(Optional.of(group));

        assertThrows(NotAllowedInsertionException.class, () -> service.update(1, 10, dto));

        verify(groupRepository, never()).save(any());
    }

    @Test
    void shouldDeleteGroup() {
        when(groupRepository.findById(10)).thenReturn(Optional.of(group));

        service.delete(1, 10);

        verify(groupRepository).deleteById(10);
    }

    @Test
    void shouldThrowWhenDeletingOtherUserGroup() {
        User other = new User();
        other.setId(2);
        group.setUser(other);

        when(groupRepository.findById(10)).thenReturn(Optional.of(group));

        assertThrows(NotAllowedInsertionException.class, () -> service.delete(1, 10));

        verify(groupRepository, never()).deleteById(any());
    }

    @Test
    void shouldAddSubject() {
        SubjectDTO subjectDTO = new SubjectDTO(
                1,
                "Math",
                "Desc",
                true,
                "MEDIA_ARITMETICA",
                new BigDecimal("6.0"),
                new BigDecimal("0.0"),
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        when(groupRepository.findByIdAndUserId(10, 1)).thenReturn(Optional.of(group));
        when(subjectService.findById(1, 1)).thenReturn(subjectDTO);
        when(groupRepository.save(any())).thenReturn(group);

        GroupDTO dto = service.addSubject(1, 10, 1);

        assertNotNull(dto);
        assertEquals(1, group.getSubjects().size());
        verify(groupRepository).save(group);
    }

    @Test
    void shouldAssociateSubjects() {
        SubjectDTO subject1 = new SubjectDTO(
                1,
                "Math",
                "Desc",
                true,
                "MEDIA_ARITMETICA",
                new BigDecimal("6.0"),
                new BigDecimal("0.0"),
                LocalDateTime.now(),
                LocalDateTime.now()
        );
        SubjectDTO subject2 = new SubjectDTO(
                2,
                "Physics",
                "Desc",
                true,
                "MEDIA_ARITMETICA",
                new BigDecimal("6.0"),
                new BigDecimal("0.0"),
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        when(groupRepository.findById(10)).thenReturn(Optional.of(group));
        when(subjectService.findById(1, 1)).thenReturn(subject1);
        when(subjectService.findById(2, 1)).thenReturn(subject2);
        when(groupRepository.save(any())).thenReturn(group);

        GroupDTO dto = service.associateSubjects(1, 10, new AssociateSubjectsDTO(List.of(1, 2)));

        assertNotNull(dto);
        assertEquals(2, group.getSubjects().size());
    }
}
