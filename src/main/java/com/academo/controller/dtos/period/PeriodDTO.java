package com.academo.controller.dtos.period;

import com.academo.controller.dtos.activityType.ActivityTypeDTO;
import com.academo.controller.dtos.subject.SubjectDTO;
import com.academo.model.enums.PeriodName;

import java.math.BigDecimal;
import java.util.List;

public record PeriodDTO(
        Integer id,
        SubjectDTO subject,
        PeriodName name,
        BigDecimal grade,
        BigDecimal weight,
        List<ActivityTypeDTO> activityTypeList
        ) {
}
