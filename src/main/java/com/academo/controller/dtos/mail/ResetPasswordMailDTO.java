package com.academo.controller.dtos.mail;

public record ResetPasswordMailDTO(
        String name,
        String email,
        String resetPasswordToken
) {
}
