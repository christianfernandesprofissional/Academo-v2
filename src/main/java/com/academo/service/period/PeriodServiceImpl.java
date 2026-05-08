package com.academo.service.period;

import com.academo.controller.dtos.activityType.SaveActivityTypeDTO;
import com.academo.controller.dtos.period.*;
import com.academo.model.Period;
import com.academo.model.Subject;
import com.academo.model.User;
import com.academo.model.enums.period.PeriodName;
import com.academo.repository.PeriodRepository;
import com.academo.repository.SubjectRepository;
import com.academo.service.calculation.ICalculationService;
import com.academo.util.exceptions.NotAllowedInsertionException;
import com.academo.util.exceptions.period.PeriodAlreadyExistsException;
import com.academo.util.exceptions.period.PeriodLimitException;
import com.academo.util.exceptions.period.PeriodNotFoundException;
import com.academo.util.exceptions.subject.SubjectNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
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
    public Page<PeriodDTO> findAll(Integer userId, Integer subjectId, Pageable pageable) {
        return repository.findAllByUserIdAndSubjectId(userId, subjectId, pageable).map(PeriodDTO::fromPeriod);
    }

    @Override
    public PeriodDTO findById(Integer userId, Integer periodId) {
        return PeriodDTO.fromPeriod(repository.findByIdAndUserId(periodId, userId).orElseThrow(PeriodNotFoundException::new));
    }

    @Transactional
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
        BigDecimal normalizedWeight = BigDecimal.valueOf(periodDTO.weight()).movePointLeft(2);
        Period newPeriod = new Period();
        newPeriod.setName(periodName.name());
        newPeriod.setGrade(periodDTO.grade());
        newPeriod.setWeight(normalizedWeight);
        newPeriod.setSubject(subject);
        User user = new User();
        user.setId(userId);
        newPeriod.setUser(user);
        repository.saveAndFlush(newPeriod);
        PeriodDTO dto = PeriodDTO.fromPeriod(newPeriod);
        calculationService.updateSubjectAverage(dto.subjectId());
        return dto;
    }

    @Override
    public PeriodDTO createExam(Integer userId, CreateExamDTO examDTO) {
        Subject subject = subjectRepository.findById(examDTO.subjectId()).orElseThrow(SubjectNotFoundException::new);

        verifyCreatedPeriodsFromSubject(subject, PeriodName.EXAME);
        Period newPeriod = new Period();
        newPeriod.setName("EXAME");
        newPeriod.setGrade(BigDecimal.valueOf(0.0));
        newPeriod.setSubject(subject);
        User user = new User();
        user.setId(userId);
        newPeriod.setUser(user);
        PeriodDTO dto = PeriodDTO.fromPeriod(repository.save(newPeriod));
        calculationService.updateSubjectAverage(dto.subjectId());
        return dto;
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
    @Transactional
    public PeriodDTO updatePeriodsWeigth(Integer userId, Integer subjectId, UpdateWeightDTO updateWeightDTO) {
        subjectRepository.findByIdAndUserId(subjectId, userId).orElseThrow(SubjectNotFoundException::new);

        List<Period> periods = repository.findAllByUserIdAndSubjectId(userId, subjectId);

        Period p1 = null;
        Period p2 = null;

        for (Period p : periods) {
            PeriodName name = PeriodName.valueOf(p.getName());
            if (name == PeriodName.P1) p1 = p;
            if (name == PeriodName.P2) p2 = p;
        }

        if (p1 == null || p2 == null) {
            throw new NotAllowedInsertionException("É necessário ter P1 e P2 cadastrados para atualizar os pesos");
        }

        BigDecimal normalizedP1 = BigDecimal.valueOf(updateWeightDTO.firstPeriodWeight()).movePointLeft(2);
        BigDecimal normalizedP2 = BigDecimal.valueOf(updateWeightDTO.secondPeriodWeight()).movePointLeft(2);

        p1.setWeight(normalizedP1);
        p2.setWeight(normalizedP2);

        repository.saveAll(List.of(p1, p2));
        calculationService.updateSubjectAverage(subjectId);

        return PeriodDTO.fromPeriod(repository.findById(p1.getId()).orElseThrow(PeriodNotFoundException::new));
    }

    @Override
    @Transactional
    public PeriodDTO update(Integer userId, Integer periodId, UpdatePeriodDTO periodDTO) {
        Period inDB = repository.findByIdAndUserId(periodId,userId).orElseThrow(PeriodNotFoundException::new);
         if (!(inDB.getSubject().getId() == periodDTO.subjectId())) {
            throw new NotAllowedInsertionException("Período não pertence à matéria informada");

        }
        if (!subjectRepository.existsById(periodDTO.subjectId())) {
            throw new SubjectNotFoundException();
        }
        BigDecimal normalizedWeight = BigDecimal.valueOf(periodDTO.weight()).movePointLeft(2);
        //Verificação se os pesos dos periodos P1 e P2 não ultrapassam 1
        List<BigDecimal> weights = new ArrayList<>();
        List<PeriodDTO> periods = repository.findAllByUserIdAndSubjectId(userId, periodDTO.subjectId()).stream().map(PeriodDTO::fromPeriod).toList();
        for(PeriodDTO p : periods){
            PeriodName current = PeriodName.valueOf(p.name());
            PeriodName updating = PeriodName.valueOf(inDB.getName());

            if(current != PeriodName.EXAME && current != updating){
                weights.add(p.weight());
            }
        }
        weights.add(normalizedWeight);
        BigDecimal weightsSum = calculationService.sumWeights(weights);
        if(!(PeriodName.valueOf(inDB.getName()) == PeriodName.EXAME) && weightsSum.compareTo(BigDecimal.ONE) > 0){
            throw new NotAllowedInsertionException("Os pesos do período ultrapassam 1");
        }
        // -----------------------------------------------------------------
        inDB.setWeight(normalizedWeight);
        if(PeriodName.valueOf(inDB.getName()) == PeriodName.EXAME){
            inDB.setGrade(periodDTO.grade());
        }
        repository.save(inDB);
        calculationService.updatePeriodAverage(periodId);
        calculationService.updateSubjectAverage(periodDTO.subjectId());
        return PeriodDTO.fromPeriod(repository.findById(inDB.getId()).get());
    }

    @Override
    @Transactional //Anotação necessária porque para queries customizadas de Delete não abrem transação automaticamente, então o JPA bloqueia sem para garantir consistência
    public void delete(Integer userId,Integer subjectId, Integer periodId){
        PeriodDTO periodDTO = findById(userId, periodId);
        if(!PeriodName.valueOf(periodDTO.name()).equals(PeriodName.EXAME)) throw new NotAllowedInsertionException("Só é permitido a deleção de EXAME!");
        repository.deleteByIdAndSubjectIdAndUserId(periodId, subjectId, userId);
        repository.flush();
        calculationService.updateSubjectAverage(periodDTO.subjectId());
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
