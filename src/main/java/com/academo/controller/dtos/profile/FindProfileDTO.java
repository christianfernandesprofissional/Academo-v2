package com.academo.controller.dtos.profile;

import com.academo.model.Profile;

public record FindProfileDTO(
        Profile profile,
        Long userUseStorage
) {
}
