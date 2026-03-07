package com.academo.service.period;

import com.academo.controller.dtos.activityType.SaveActivityTypeDTO;
import com.academo.controller.dtos.period.PeriodDTO;
import com.academo.controller.dtos.period.SavePeriodDTO;
import com.academo.repository.PeriodRepository;

import java.util.List;

public class PeriodServiceImpl implements IPeriodService{

    private final PeriodRepository repository;

    public PeriodServiceImpl(PeriodRepository repository){
        this.repository = repository;
    }

    @Override
    public List<PeriodDTO> findAll(Integer userId, Integer subjectId) {
        return repository.findAllByUserIdAndSubjectId(userId, subjectId).stream().map(PeriodDTO::fromPeriod).toList();
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
