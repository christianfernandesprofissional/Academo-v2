package com.academo.controller.dtos.period;

public record SavePeriodDTO(
        Integer subjectId,
        String name,
        String grade,
        String weight
) {
}
