package com.academo.controller.dtos.flashcard;

import jakarta.validation.constraints.NotEmpty;

public record UpdateLevelDTO (
        @NotEmpty(message = "O nível é obrigatório")
        String level
){

}
