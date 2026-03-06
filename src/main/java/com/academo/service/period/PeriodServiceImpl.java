package com.academo.service.period;

import com.academo.controller.dtos.activityType.SaveActivityTypeDTO;
import com.academo.controller.dtos.period.PeriodDTO;
import com.academo.controller.dtos.period.SavePeriodDTO;

import java.util.List;

public class PeriodServiceImpl implements IPeriodService{
    @Override
    public List<PeriodDTO> findAll(Integer userId, Integer subjectId) {
        return List.of();
    }

    @Override
    public PeriodDTO findById(Integer userId, Integer subjectId) {
        return null;
    }

    @Override
    public PeriodDTO create(Integer userId, SavePeriodDTO periodDTO) {
        return null;
    }

    @Override
    public PeriodDTO update(Integer userId, Integer groupId, SavePeriodDTO periodDTO) {
        return null;
    }

    @Override
    public PeriodDTO addActivityType(Integer userId, SaveActivityTypeDTO activityTypeDTO) {
        return null;
    }
}
