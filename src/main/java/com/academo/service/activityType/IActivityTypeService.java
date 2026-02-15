package com.academo.service.activityType;

import com.academo.controller.dtos.activityType.ActivityTypeDTO;
import com.academo.model.ActivityType;

import java.util.List;

public interface IActivityTypeService {

    public List<ActivityTypeDTO> findAll(Integer userId);
    public ActivityType findByIdAndUserId(Integer ActivityTypeId, Integer userId);
    public ActivityTypeDTO getActivityTypeDTO(Integer ActivityTypeId, Integer userId);
    public ActivityTypeDTO createActivityType(Integer userId, String name, String description);
    public ActivityTypeDTO update(Integer userId, Integer activityTypeId, String name, String description);
    public void deleteActivityType(Integer userId, Integer activityId);

}
