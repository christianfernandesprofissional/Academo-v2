package com.academo.service.profile;

import com.academo.controller.dtos.profile.ProfileDTO;
import com.academo.controller.dtos.profile.UpdateProfileDTO;
import com.academo.model.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface IProfileService  {

    ProfileDTO findById(Integer id);
    ProfileDTO create(User user);
    ProfileDTO update(Integer userId, UpdateProfileDTO profileDTO);
}
