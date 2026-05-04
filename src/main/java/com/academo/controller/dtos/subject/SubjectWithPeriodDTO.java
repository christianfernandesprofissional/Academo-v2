package com.academo.controller.dtos.subject;

import com.academo.controller.dtos.period.PeriodDTO;

import java.util.List;

public record SubjectWithPeriodDTO(
        SubjectDTO subjectDTO,
        List<PeriodDTO> periodsDTO
) {
}
