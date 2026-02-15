package com.academo.service.activityType;

import com.academo.controller.dtos.activityType.ActivityTypeDTO;
import com.academo.model.ActivityType;
import com.academo.repository.ActivityTypeRepository;
import com.academo.service.user.IUserService;
import com.academo.util.exceptions.NotAllowedInsertionException;
import com.academo.util.exceptions.activityType.ActivityTypeExistsException;
import com.academo.util.exceptions.activityType.ActivityTypeNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ActivityTypeServiceImpl implements IActivityTypeService {

    @Autowired
    private ActivityTypeRepository repository;


    private final IUserService userService;

    public ActivityTypeServiceImpl(IUserService userService) {
        this.userService = userService;
    }

    @Override
    public List<ActivityTypeDTO> findAll(Integer  userId) {
        return repository.findAllByUserId(userId).stream()
                .map(t -> new ActivityTypeDTO(
                        t.getId(),
                        t.getName(),
                        t.getDescription()
                )).toList();
    }

    @Override
    public ActivityType findByIdAndUserId(Integer ActivityTypeId, Integer userId) {
        return repository.findByIdAndUserId(ActivityTypeId, userId).orElseThrow(ActivityTypeNotFoundException::new);
    }

    // Método criado devido a necessidade do retorno de uma entidade em ActivityService no método fillActivity()
    @Override
    public ActivityTypeDTO getActivityTypeDTO(Integer ActivityTypeId, Integer userId) {
        ActivityType activityType =  repository.findByIdAndUserId(ActivityTypeId, userId).orElseThrow(ActivityTypeNotFoundException::new);
        return new ActivityTypeDTO(activityType.getId(), activityType.getName(), activityType.getDescription());
    }

    @Override
    public ActivityTypeDTO createActivityType(Integer userId, String name, String description) {

        if(repository.existsByNameAndUserId(name, userId)) throw new ActivityTypeExistsException();

        ActivityType newActivityType = new ActivityType();
        newActivityType.setName(name);
        newActivityType.setDescription(description);
        newActivityType.setUser(userService.findById(userId));
        newActivityType.setId(repository.save(newActivityType).getId());

        return new ActivityTypeDTO(newActivityType.getId(), newActivityType.getName(), newActivityType.getDescription());
    }

    @Override
    public ActivityTypeDTO update(Integer userId,Integer activityTypeId, String name, String description) {
        ActivityType inDb = repository.findByIdAndUserId(activityTypeId, userId).orElseThrow(ActivityTypeNotFoundException::new);
        if (!inDb.getUser().getId().equals(userId)) throw new NotAllowedInsertionException();

        ActivityType updated = new ActivityType();
        updated.setId(activityTypeId);
        updated.setUser(inDb.getUser());
        updated.setName(name);
        updated.setDescription(description);
        repository.save(updated);

        return new ActivityTypeDTO(updated.getId(), updated.getName(), updated.getDescription());
    }

    @Override
    public void deleteActivityType(Integer userId, Integer activityId){
        ActivityType inDb = repository.findByIdAndUserId(activityId, userId).orElseThrow(ActivityTypeNotFoundException::new);
        if (!inDb.getUser().getId().equals(userId)) throw new NotAllowedInsertionException("Deleção inválida!");
        repository.deleteById(activityId);
    }

}
