package com.academo.controller.dtos.validation;

import java.util.Map;

public record ValidationErrors(
        Map<String, String> errors
) {
}
