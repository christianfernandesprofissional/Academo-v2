package com.academo.controller.dtos.mail;

public record ActivateAccountMailDTO(
        String name,
        String email,
        String activationToken
) {
}
