package com.academo.service.profile;

import com.academo.controller.dtos.profile.UpdateProfileDTO;
import com.academo.model.Profile;
import com.academo.model.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface IProfileService  {

    Profile create(User user);
    Profile update(Integer userId, UpdateProfileDTO profileDTO);
}
