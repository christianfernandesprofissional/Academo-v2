package com.academo.service.period;

import com.academo.controller.dtos.activityType.SaveActivityTypeDTO;
import com.academo.controller.dtos.period.SavePeriodDTO;
import com.academo.controller.dtos.period.PeriodDTO;
import com.academo.controller.dtos.group.UpdateGroupDTO;

import java.util.List;

public interface IPeriodService {

    List<PeriodDTO> findAll(Integer userId, Integer subjectId);
    PeriodDTO findById(Integer userId, Integer periodId);
    PeriodDTO create(Integer userId, SavePeriodDTO periodDTO);
    PeriodDTO update(Integer userId, Integer groupId, SavePeriodDTO periodDTO);
    PeriodDTO addActivityType(Integer userId, SaveActivityTypeDTO activityTypeDTO);

}
