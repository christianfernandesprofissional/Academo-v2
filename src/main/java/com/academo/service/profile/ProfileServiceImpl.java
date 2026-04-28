package com.academo.service.profile;

import com.academo.controller.dtos.profile.ProfileDTO;
import com.academo.controller.dtos.profile.UpdateProfileDTO;
import com.academo.model.Profile;
import com.academo.model.User;
import com.academo.repository.ProfileRepository;
import com.academo.util.exceptions.profile.ProfileNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class ProfileServiceImpl implements IProfileService {

    private final ProfileRepository profileRepository;

    public ProfileServiceImpl(ProfileRepository profileRepository) {
        this.profileRepository = profileRepository;
    }

    @Override
    public ProfileDTO findById(Integer id) {
        // Conferir se o User será carregado corretamente, e por consequência, o StorageUsage
        Profile profile = profileRepository.findById(id).orElseThrow(ProfileNotFoundException::new);
        return ProfileDTO.fromProfile(profile, profile.getUser().getStorageUsage(), profile.getUser().getPlanType());
    }

    @Override
    public ProfileDTO create(User user) {
        Profile profile = new Profile();
        profile.setId(user.getId());
        Profile createdProfile =  profileRepository.save(profile);
        return ProfileDTO.fromProfile(createdProfile, user.getStorageUsage(), user.getPlanType());
    }

    @Override
    public ProfileDTO update(Integer userId, UpdateProfileDTO profileDto) {
        Profile profile = profileRepository.findById(userId).orElseThrow(ProfileNotFoundException::new);
        profile.setFullName(profileDto.fullName());
        profile.setGender(profileDto.gender().charAt(0));
        profile.setBirthDate(profileDto.birthDate());
        Profile updatedProfile = profileRepository.save(profile);
        return ProfileDTO.fromProfile(updatedProfile, profile.getUser().getStorageUsage(), profile.getUser().getPlanType());
    }
}
