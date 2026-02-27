package com.academo.service.activityType;

import com.academo.controller.dtos.activity.SaveActivityDTO;
import com.academo.controller.dtos.activityType.ActivityTypeDTO;
import com.academo.controller.dtos.activityType.SaveActivityTypeDTO;
import com.academo.model.ActivityType;
import com.academo.repository.ActivityTypeRepository;
import com.academo.service.user.IUserService;
import com.academo.util.exceptions.NotAllowedInsertionException;
import com.academo.util.exceptions.activityType.ActivityTypeExistsException;
import com.academo.util.exceptions.activityType.ActivityTypeNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ActivityTypeServiceImpl implements IActivityTypeService {

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
                        t.getDescription()
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
        return new ActivityTypeDTO(activityType.getId(), activityType.getName(), activityType.getDescription());
    }

    @Override
    public ActivityTypeDTO create(Integer userId, SaveActivityTypeDTO activityTypeDTO) {

        if(repository.existsByNameAndUserId(activityTypeDTO.name(), userId)) throw new ActivityTypeExistsException();

        ActivityType newActivityType = new ActivityType();
        newActivityType.setName(activityTypeDTO.name());
        newActivityType.setDescription(activityTypeDTO.description());
        newActivityType.setUser(userService.findById(userId));
        newActivityType.setId(repository.save(newActivityType).getId());

        return new ActivityTypeDTO(newActivityType.getId(), newActivityType.getName(), newActivityType.getDescription());
    }

    @Override
    public ActivityTypeDTO update(Integer userId, Integer id, SaveActivityDTO activityTypeDTO) {
        ActivityType inDb = repository.findByIdAndUserId(id, userId).orElseThrow(ActivityTypeNotFoundException::new);
        if (!inDb.getUser().getId().equals(userId)) throw new NotAllowedInsertionException();

        ActivityType updated = new ActivityType();
        updated.setId(id);
        updated.setUser(inDb.getUser());
        updated.setName(activityTypeDTO.name());
        updated.setDescription(activityTypeDTO.description());
        repository.save(updated);

        return new ActivityTypeDTO(updated.getId(), updated.getName(), updated.getDescription());
    }

    @Override
    public void delete(Integer userId, Integer activityId){
        ActivityType inDb = repository.findByIdAndUserId(activityId, userId).orElseThrow(ActivityTypeNotFoundException::new);
        if (!inDb.getUser().getId().equals(userId)) throw new NotAllowedInsertionException("Deleção inválida!");
        repository.deleteById(activityId);
    }

}
