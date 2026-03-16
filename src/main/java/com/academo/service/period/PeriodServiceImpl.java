package com.academo.service.period;

import com.academo.controller.dtos.activityType.SaveActivityTypeDTO;
import com.academo.controller.dtos.period.PeriodDTO;
import com.academo.controller.dtos.period.SavePeriodDTO;
import com.academo.controller.dtos.period.UpdatePeriodDTO;
import com.academo.model.Period;
import com.academo.model.Subject;
import com.academo.model.User;
import com.academo.model.enums.PeriodName;
import com.academo.repository.PeriodRepository;
import com.academo.util.exceptions.period.PeriodNotFoundException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
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
    public PeriodDTO findById(Integer userId, Integer periodId) {
        return PeriodDTO.fromPeriod(repository.findByPeriodIdAndUserId(userId,periodId).orElseThrow(PeriodNotFoundException::new));
    }

    @Override
    public PeriodDTO create(Integer userId, SavePeriodDTO periodDTO) {
        Period newPeriod = new Period();
        newPeriod.setName(PeriodName.valueOf(periodDTO.name()).name());
        newPeriod.setGrade(new BigDecimal(periodDTO.grade()));
        newPeriod.setWeight(new BigDecimal(periodDTO.weight()));
        newPeriod.setSubject(new Subject());
        newPeriod.getSubject().setId(periodDTO.subjectId());
        newPeriod.setUser(new User());
        newPeriod.getUser().setId(userId);
         return PeriodDTO.fromPeriod(repository.save(newPeriod));
    }

    @Override
    public PeriodDTO update(Integer userId, UpdatePeriodDTO periodDTO) {
        Period inDB = repository.findByPeriodIdAndUserId(periodDTO.id(),userId).orElseThrow(PeriodNotFoundException::new);
        inDB.setName(PeriodName.valueOf(periodDTO.name()).name());
        inDB.setWeight(new BigDecimal(periodDTO.weight()));
        inDB.setGrade(new BigDecimal(periodDTO.grade()));
        repository.save(inDB);
        return PeriodDTO.fromPeriod(inDB);
    }

    @Override
    public void delete(Integer userId,Integer subjectId, Integer periodId){
        repository.deleteByPeriodIdAndSubjectIdAndUserId(periodId, subjectId, userId);
    }

    @Override
    public PeriodDTO addActivityType(Integer userId, SaveActivityTypeDTO activityTypeDTO) {

        return null;
    }
}
