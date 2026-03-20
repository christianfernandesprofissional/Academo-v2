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
import com.academo.repository.SubjectRepository;
import com.academo.util.exceptions.period.PeriodAlreadyExistsException;
import com.academo.util.exceptions.period.PeriodLimitException;
import com.academo.util.exceptions.period.PeriodNotFoundException;
import com.academo.util.exceptions.subject.SubjectNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class PeriodServiceImpl implements IPeriodService{

    private final PeriodRepository repository;
    private final SubjectRepository subjectRepository;

    public PeriodServiceImpl(PeriodRepository repository, SubjectRepository subjectRepository){
        this.repository = repository;
        this.subjectRepository = subjectRepository;
    }

    @Override
    public List<PeriodDTO> findAll(Integer userId, Integer subjectId) {
        return repository.findAllByUserIdAndSubjectId(userId, subjectId).stream().map(PeriodDTO::fromPeriod).toList();
    }

    @Override
    public PeriodDTO findById(Integer userId, Integer periodId) {
        return PeriodDTO.fromPeriod(repository.findByIdAndUserId(userId,periodId).orElseThrow(PeriodNotFoundException::new));
    }

    @Override
    public PeriodDTO create(Integer userId, SavePeriodDTO periodDTO) {
        if(!subjectRepository.existsById(periodDTO.subjectId())) throw new SubjectNotFoundException();

        List<PeriodDTO> periods = findAll(userId, periodDTO.subjectId());
        if(periods.size() == 3) throw new PeriodLimitException();
        periods.forEach(p -> {
            if(p.name().equals(periodDTO.name())) throw new PeriodAlreadyExistsException();
        });

        verifyCreatedPeriodsFromSubject(periodDTO.subjectId(), userId);

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

    private void verifyCreatedPeriodsFromSubject(Integer subjectId, Integer userId) {

    }

    @Override
    public PeriodDTO update(Integer userId, UpdatePeriodDTO periodDTO) {
        Period inDB = repository.findByIdAndUserId(periodDTO.id(),userId).orElseThrow(PeriodNotFoundException::new);
        inDB.setName(PeriodName.valueOf(periodDTO.name()).name());
        inDB.setWeight(new BigDecimal(periodDTO.weight()));
        inDB.setGrade(new BigDecimal(periodDTO.grade()));
        repository.save(inDB);
        return PeriodDTO.fromPeriod(inDB);
    }

    @Override
    @Transactional //Anotação necessária porque para queries customizadas de Delete não abrem transação automaticamente, então o JPA bloqueia sem para garantir consistência
    public void delete(Integer userId,Integer subjectId, Integer periodId){
        repository.deleteByIdAndSubjectIdAndUserId(periodId, subjectId, userId);
    }

    @Override
    public boolean existsById(Integer periodId){
        return repository.existsById(periodId);
    }

    @Override
    public PeriodDTO addActivityType(Integer userId, SaveActivityTypeDTO activityTypeDTO) {

        return null;
    }
}
