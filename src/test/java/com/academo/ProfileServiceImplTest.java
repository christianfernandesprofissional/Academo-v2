package com.academo;

import com.academo.controller.dtos.profile.ProfileDTO;
import com.academo.controller.dtos.profile.UpdateProfileDTO;
import com.academo.model.Profile;
import com.academo.model.User;
import com.academo.repository.ProfileRepository;
import com.academo.service.profile.ProfileServiceImpl;
import com.academo.util.exceptions.profile.ProfileNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProfileServiceImplTest {

    @Mock
    private ProfileRepository profileRepository;

    @InjectMocks
    private ProfileServiceImpl service;

    private User user;
    private Profile profile;

    @BeforeEach
    void setup() {
        user = new User();
        user.setId(1);
        user.setStorageUsage(123L);

        profile = new Profile();
        profile.setId(1);
        profile.setUser(user);
    }

    @Test
    void shouldFindById() {
        when(profileRepository.findById(1)).thenReturn(Optional.of(profile));

        ProfileDTO dto = service.findById(1);

        assertNotNull(dto);
    }

    @Test
    void shouldThrowWhenProfileNotFound() {
        when(profileRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(ProfileNotFoundException.class, () -> service.findById(1));
    }

    @Test
    void shouldCreateProfile() {
        when(profileRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        ProfileDTO dto = service.create(user);

        assertNotNull(dto);
        verify(profileRepository).save(any(Profile.class));
    }

    @Test
    void shouldUpdateProfile() {
        UpdateProfileDTO dto = new UpdateProfileDTO(
                "Full Name",
                LocalDate.of(2000, 1, 1),
                "M"
        );

        when(profileRepository.findById(1)).thenReturn(Optional.of(profile));
        when(profileRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        ProfileDTO updated = service.update(1, dto);

        assertNotNull(updated);
        verify(profileRepository).save(profile);
    }
}
