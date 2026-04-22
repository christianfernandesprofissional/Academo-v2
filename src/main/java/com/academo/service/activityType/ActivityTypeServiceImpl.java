package com.academo.service.activityType;

import com.academo.controller.dtos.activityType.ActivityTypeDTO;
import com.academo.controller.dtos.activityType.SaveActivityTypeDTO;
import com.academo.controller.dtos.activityType.UpdateActivityTypeDTO;
import com.academo.model.ActivityType;
import com.academo.model.Period;
import com.academo.repository.ActivityTypeRepository;
import com.academo.service.calculation.ICalculationService;
import com.academo.service.period.IPeriodService;
import com.academo.service.user.IUserService;
import com.academo.util.exceptions.NotAllowedInsertionException;
import com.academo.util.exceptions.activityType.ActivityTypeExistsException;
import com.academo.util.exceptions.activityType.ActivityTypeNotFoundException;
import com.academo.util.exceptions.period.PeriodNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class ActivityTypeServiceImpl implements IActivityTypeService {

    private static final Logger logger = LoggerFactory.getLogger(ActivityTypeServiceImpl.class);

    private final ActivityTypeRepository repository;
    private final IUserService userService;
    private final IPeriodService periodService;
    private final ICalculationService calculationService;

    public ActivityTypeServiceImpl(IUserService userService, ActivityTypeRepository repository, IPeriodService periodService, ICalculationService calculationService) {
        this.userService = userService;
        this.repository = repository;
        this.periodService = periodService;
        this.calculationService = calculationService;
    }

    @Override
    public Page<ActivityTypeDTO> findAll(Integer userId, Integer periodId, Pageable pageable) {
        return repository.findAllByPeriodIdAndUserId(periodId, userId, pageable).map(ActivityTypeDTO::fromActivityType);
    }

    @Override
    public ActivityType findById(Integer ActivityTypeId, Integer userId) {
        return repository.findByIdAndUserId(ActivityTypeId, userId).orElseThrow(ActivityTypeNotFoundException::new);
    }

    // Método criado devido a necessidade do retorno de uma entidade em ActivityService no método fillActivity()
    @Override
    public ActivityTypeDTO findDTO(Integer ActivityTypeId, Integer userId) {
        ActivityType activityType =  repository.findByIdAndUserId(ActivityTypeId, userId).orElseThrow(ActivityTypeNotFoundException::new);
        return ActivityTypeDTO.fromActivityType(activityType);
    }

    @Override
    public ActivityTypeDTO create(Integer userId, SaveActivityTypeDTO activityTypeDTO) {
        if(!periodService.existsById(activityTypeDTO.periodId())) throw new PeriodNotFoundException();
        if(repository.existsByNameAndUserIdAndPeriodId(activityTypeDTO.name(), userId, activityTypeDTO.periodId())) throw new ActivityTypeExistsException();

        ActivityType newActivityType = new ActivityType();
        newActivityType.setName(activityTypeDTO.name());
        newActivityType.setDescription(activityTypeDTO.description());
        Period p = new Period();
        p.setId(activityTypeDTO.periodId());
        newActivityType.setPeriod(p);
        newActivityType.setUser(userService.findById(userId));
        newActivityType.setId(repository.save(newActivityType).getId());

        return findDTO(newActivityType.getId(), userId);
    }

    @Override
    public ActivityTypeDTO update(Integer userId, Integer id, UpdateActivityTypeDTO activityTypeDTO) {
        ActivityType inDb = repository.findByIdAndUserId(id, userId).orElseThrow(ActivityTypeNotFoundException::new);
        if (!inDb.getUser().getId().equals(userId)) throw new NotAllowedInsertionException();


        BigDecimal normalizedWeight = BigDecimal.valueOf(activityTypeDTO.weight()).movePointLeft(2);
        //Verificação se os pesos dos tipos de atividade não ultrapassam 1
        List<BigDecimal> weights = new ArrayList<>();
        List<ActivityTypeDTO> periods = repository.findAllByPeriodIdAndUserId(activityTypeDTO.periodId(), userId).stream().map(ActivityTypeDTO::fromActivityType).toList();
        for(ActivityTypeDTO atDTO : periods){
            if(!Objects.equals(atDTO.id(), id)){
                weights.add(atDTO.weight());
            }
        }
        weights.add(normalizedWeight);
        BigDecimal weightsSum = calculationService.sumWeights(weights);
        if(weightsSum.compareTo(BigDecimal.ONE) > 0){
            throw new NotAllowedInsertionException("Os pesos dos tipos de atividade ultrapassam 1");
        }
        // -----------------------------------------------------------------

        inDb.setName(activityTypeDTO.name());
        inDb.setDescription(activityTypeDTO.description());
        inDb.setWeight(normalizedWeight);
        ActivityType updated = repository.save(inDb);
        logger.info("[DEBUG] ActivityType updated - createdAt: {}", updated.getCreatedAt());

        return ActivityTypeDTO.fromActivityType(inDb);
    }

    @Override
    public void delete(Integer userId, Integer activityId){
        ActivityType inDb = repository.findByIdAndUserId(activityId, userId).orElseThrow(ActivityTypeNotFoundException::new);
        if (!inDb.getUser().getId().equals(userId)) throw new NotAllowedInsertionException("Deleção inválida!");
        repository.deleteById(activityId);
    }

}
