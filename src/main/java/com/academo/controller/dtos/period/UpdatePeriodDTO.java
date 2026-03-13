package com.academo.controller.dtos.period;

import com.academo.controller.dtos.activityType.ActivityTypeDTO;
import com.academo.controller.dtos.subject.SubjectDTO;
import com.academo.model.Period;
import com.academo.model.enums.PeriodName;

import java.math.BigDecimal;
import java.util.List;

public record UpdatePeriodDTO(
        Integer id,
        Integer subjectId,
        PeriodName name,
        String grade,
        String weight
        ) {

}
