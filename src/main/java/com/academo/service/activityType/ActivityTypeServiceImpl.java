package com.academo.service.activityType;

import com.academo.controller.dtos.activity.ActivityDTO;
import com.academo.controller.dtos.activityType.ActivityTypeDTO;
import com.academo.controller.dtos.activityType.SaveActivityTypeDTO;
import com.academo.model.ActivityType;
import com.academo.repository.ActivityTypeRepository;
import com.academo.service.user.IUserService;
import com.academo.util.exceptions.NotAllowedInsertionException;
import com.academo.util.exceptions.activityType.ActivityTypeExistsException;
import com.academo.util.exceptions.activityType.ActivityTypeNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
    public List<ActivityTypeDTO> findAll(Integer userId) {
        return repository.findAllByUserId(userId).stream()
                .map(t -> new ActivityTypeDTO(
                        t.getId(),
                        t.getName(),
                        t.getDescription(),
                        new ArrayList<ActivityDTO>(),
                        t.getCreatedAt(),
                        t.getUpdatedAt()
                )).toList();
    }

    @Override
    public ActivityType findById(Integer ActivityTypeId, Integer userId) {
        return repository.findByIdAndUserId(ActivityTypeId, userId).orElseThrow(ActivityTypeNotFoundException::new);
    }

    // Método criado devido a necessidade do retorno de uma entidade em ActivityService no método fillActivity()
    @Override
    public ActivityTypeDTO findDTO(Integer ActivityTypeId, Integer userId) {
        ActivityType activityType =  repository.findByIdAndUserId(ActivityTypeId, userId).orElseThrow(ActivityTypeNotFoundException::new);
        return new ActivityTypeDTO(activityType.getId(), activityType.getName(), activityType.getDescription(),new ArrayList<ActivityDTO>(), activityType.getCreatedAt(), activityType.getUpdatedAt());
    }

    @Override
    public ActivityTypeDTO create(Integer userId, SaveActivityTypeDTO activityTypeDTO) {

        if(repository.existsByNameAndUserId(activityTypeDTO.name(), userId)) throw new ActivityTypeExistsException();

        ActivityType newActivityType = new ActivityType();
        newActivityType.setName(activityTypeDTO.name());
        newActivityType.setDescription(activityTypeDTO.description());
        newActivityType.setUser(userService.findById(userId));
        newActivityType.setId(repository.save(newActivityType).getId());

        return new ActivityTypeDTO(newActivityType.getId(), newActivityType.getName(), newActivityType.getDescription(),new ArrayList<ActivityDTO>(), newActivityType.getCreatedAt(), newActivityType.getUpdatedAt());
    }

    @Override
    public ActivityTypeDTO update(Integer userId, Integer id, SaveActivityTypeDTO activityTypeDTO) {
        ActivityType inDb = repository.findByIdAndUserId(id, userId).orElseThrow(ActivityTypeNotFoundException::new);
        if (!inDb.getUser().getId().equals(userId)) throw new NotAllowedInsertionException();

        inDb.setName(activityTypeDTO.name());
        inDb.setDescription(activityTypeDTO.description());
        ActivityType updated = repository.save(inDb);
        logger.info("[DEBUG] ActivityType updated - createdAt: {}", updated.getCreatedAt());

        return new ActivityTypeDTO(updated.getId(), updated.getName(), updated.getDescription(), new ArrayList<ActivityDTO>(),updated.getCreatedAt(), updated.getUpdatedAt());
    }

    @Override
    public void delete(Integer userId, Integer activityId){
        ActivityType inDb = repository.findByIdAndUserId(activityId, userId).orElseThrow(ActivityTypeNotFoundException::new);
        if (!inDb.getUser().getId().equals(userId)) throw new NotAllowedInsertionException("Deleção inválida!");
        repository.deleteById(activityId);
    }

}
