package com.academo.controller.dtos.period;

import com.academo.controller.dtos.activityType.ActivityTypeDTO;
import com.academo.controller.dtos.subject.SubjectDTO;
import com.academo.model.Period;
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

    public static PeriodDTO fromPeriod(Period period){
        return new PeriodDTO(
                period.getPeriodId(),
                SubjectDTO.fromSubject(period.getSubject()),
                period.getName(),
                period.getGrade(),
                period.getWeight(),
                period.getActivityTypeList().stream().map(ActivityTypeDTO::fromActivityType).toList()
        );
    }
}
