package com.academo.service.period;

import com.academo.controller.dtos.activityType.SaveActivityTypeDTO;
import com.academo.controller.dtos.period.SavePeriodDTO;
import com.academo.controller.dtos.period.PeriodDTO;
import com.academo.controller.dtos.group.UpdateGroupDTO;
import com.academo.controller.dtos.period.UpdatePeriodDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IPeriodService {

    Page<PeriodDTO> findAll(Integer userId, Integer subjectId, Pageable pageable);
    PeriodDTO findById(Integer userId, Integer periodId);
    PeriodDTO create(Integer userId, SavePeriodDTO periodDTO);
    PeriodDTO update(Integer userId,Integer periodId, UpdatePeriodDTO periodDTO);
    PeriodDTO addActivityType(Integer userId, SaveActivityTypeDTO activityTypeDTO);
    void delete(Integer userId,Integer subjectId, Integer periodId);
    boolean existsById(Integer periodId);

}
