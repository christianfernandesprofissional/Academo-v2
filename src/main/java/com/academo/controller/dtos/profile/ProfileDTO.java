package com.academo.controller.dtos.profile;

import com.academo.model.Profile;

public record ProfileDTO(
        Profile profile,
        Long userUseStorage
) {
}
