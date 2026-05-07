package com.academo.service.period;

import com.academo.controller.dtos.activityType.SaveActivityTypeDTO;
import com.academo.controller.dtos.period.*;
import com.academo.controller.dtos.group.UpdateGroupDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IPeriodService {

    Page<PeriodDTO> findAll(Integer userId, Integer subjectId, Pageable pageable);
    PeriodDTO findById(Integer userId, Integer periodId);
    PeriodDTO create(Integer userId, SavePeriodDTO periodDTO);
    PeriodDTO createExam(Integer userId, CreateExamDTO examDTO);
    PeriodDTO update(Integer userId,Integer periodId, UpdatePeriodDTO periodDTO);
    PeriodDTO updatePeriodsWeigth(Integer userId, Integer subjectId, UpdateWeightDTO updateWeightDTO);
    PeriodDTO addActivityType(Integer userId, SaveActivityTypeDTO activityTypeDTO);
    void delete(Integer userId,Integer subjectId, Integer periodId);
    boolean existsById(Integer periodId);

}
