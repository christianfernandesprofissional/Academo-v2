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
import com.academo.service.calculation.ICalculationService;
import com.academo.util.exceptions.NotAllowedInsertionException;
import com.academo.util.exceptions.period.PeriodAlreadyExistsException;
import com.academo.util.exceptions.period.PeriodLimitException;
import com.academo.util.exceptions.period.PeriodNotFoundException;
import com.academo.util.exceptions.subject.SubjectNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class PeriodServiceImpl implements IPeriodService{

    private final PeriodRepository repository;
    private final SubjectRepository subjectRepository;
    private final ICalculationService calculationService;

    public PeriodServiceImpl(PeriodRepository repository, SubjectRepository subjectRepository, ICalculationService calculationService){
        this.repository = repository;
        this.subjectRepository = subjectRepository;
        this.calculationService = calculationService;
    }

    @Override
    public List<PeriodDTO> findAll(Integer userId, Integer subjectId) {
        return repository.findAllByUserIdAndSubjectId(userId, subjectId).stream().map(PeriodDTO::fromPeriod).toList();
    }

    @Override
    public PeriodDTO findById(Integer userId, Integer periodId) {
        return PeriodDTO.fromPeriod(repository.findByIdAndUserId(periodId, userId).orElseThrow(PeriodNotFoundException::new));
    }

    @Override
    public PeriodDTO create(Integer userId, SavePeriodDTO periodDTO) {
        PeriodName periodName;
        try{
            periodName = PeriodName.valueOf(periodDTO.name());
        }catch (IllegalArgumentException e){
            throw new NotAllowedInsertionException("Período inválido: " + periodDTO.name());
        }


        Subject subject = subjectRepository.findById(periodDTO.subjectId())
                .orElseThrow(SubjectNotFoundException::new);

        verifyCreatedPeriodsFromSubject(subject, periodName);

        Period newPeriod = new Period();
        newPeriod.setName(periodName.name());
        newPeriod.setGrade(new BigDecimal(periodDTO.grade()));
        newPeriod.setWeight(new BigDecimal(periodDTO.weight()));
        newPeriod.setSubject(subject);
        User user = new User();
        user.setId(userId);
        newPeriod.setUser(user);

        return PeriodDTO.fromPeriod(repository.save(newPeriod));
    }

    private void verifyCreatedPeriodsFromSubject(Subject subject, PeriodName periodName) {

        Set<Period> periods = subject.getPeriods();

        boolean hasPeriods = !periods.isEmpty();
        boolean isExam = periodName == PeriodName.EXAME;

        if (hasPeriods && !isExam) {
            throw new NotAllowedInsertionException("Só é permitido o cadastro de EXAME!");
        }

        if (periods.size() >= 3) {
            throw new PeriodLimitException();
        }

        boolean alreadyExists = periods.stream()
                .anyMatch(p -> PeriodName.valueOf(p.getName()) == periodName);

        if (alreadyExists) {
            throw new PeriodAlreadyExistsException();
        }
    }

    @Override
    public PeriodDTO update(Integer userId, UpdatePeriodDTO periodDTO) {
        Period inDB = repository.findByIdAndUserId(periodDTO.id(),userId).orElseThrow(PeriodNotFoundException::new);
        inDB.setWeight(new BigDecimal(periodDTO.weight()));
        inDB.setGrade(new BigDecimal(periodDTO.grade()));
        repository.save(inDB);
        return PeriodDTO.fromPeriod(inDB);
    }

    @Override
    @Transactional //Anotação necessária porque para queries customizadas de Delete não abrem transação automaticamente, então o JPA bloqueia sem para garantir consistência
    public void delete(Integer userId,Integer subjectId, Integer periodId){
        PeriodDTO periodDTO = findById(userId, periodId);
        if(!PeriodName.valueOf(periodDTO.name()).equals(PeriodName.EXAME)) throw new NotAllowedInsertionException("Só é permitido a deleção de EXAME!");
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
