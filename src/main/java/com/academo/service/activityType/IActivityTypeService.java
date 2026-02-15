package com.academo.service.activityType;

import com.academo.controller.dtos.activityType.ActivityTypeDTO;
import com.academo.controller.dtos.activityType.CreateActivityTypeDTO;
import com.academo.model.ActivityType;

import java.util.List;

public interface IActivityTypeService {

    List<ActivityTypeDTO> findAll(Integer userId);
    ActivityType findById(Integer ActivityTypeId, Integer userId);
    ActivityTypeDTO findDTO(Integer ActivityTypeId, Integer userId);
    ActivityTypeDTO create(Integer userId, CreateActivityTypeDTO activityTypeDTO);
    ActivityTypeDTO update(Integer userId, ActivityTypeDTO activityTypeDTO);
    void delete(Integer userId, Integer activityId);

}
