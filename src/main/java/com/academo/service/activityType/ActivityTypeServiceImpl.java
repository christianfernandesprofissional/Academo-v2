package com.academo.service.activityType;

import com.academo.controller.dtos.activityType.ActivityTypeDTO;
import com.academo.controller.dtos.activityType.SaveActivityTypeDTO;
import com.academo.controller.dtos.activityType.UpdateActivityTypeDTO;
import com.academo.model.ActivityType;
import com.academo.model.Period;
import com.academo.repository.ActivityTypeRepository;
import com.academo.service.user.IUserService;
import com.academo.util.exceptions.NotAllowedInsertionException;
import com.academo.util.exceptions.activityType.ActivityTypeExistsException;
import com.academo.util.exceptions.activityType.ActivityTypeNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class ActivityTypeServiceImpl implements IActivityTypeService {

    private static final Logger logger = LoggerFactory.getLogger(ActivityTypeServiceImpl.class);

    private final ActivityTypeRepository repository;
    private final IUserService userService;

    public ActivityTypeServiceImpl(IUserService userService, ActivityTypeRepository repository) {
        this.userService = userService;
        this.repository = repository;
    }

    @Override
    public List<ActivityTypeDTO> findAll(Integer userId, Integer periodId) {
        return repository.findAllByPeriodIdAndUserId(periodId, userId).stream().map(ActivityTypeDTO::fromActivityType).toList();
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

        inDb.setName(activityTypeDTO.name());
        inDb.setDescription(activityTypeDTO.description());
        inDb.setWeight(new BigDecimal(activityTypeDTO.weight()));
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
