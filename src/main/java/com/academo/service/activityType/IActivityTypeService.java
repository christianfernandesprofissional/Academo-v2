package com.academo.service.activityType;

import com.academo.controller.dtos.activity.SaveActivityDTO;
import com.academo.controller.dtos.activityType.ActivityTypeDTO;
import com.academo.controller.dtos.activityType.SaveActivityTypeDTO;
import com.academo.model.ActivityType;

import java.util.List;

public interface IActivityTypeService {

    List<ActivityTypeDTO> findAll(Integer userId);
    ActivityType findById(Integer ActivityTypeId, Integer userId);
    ActivityTypeDTO findDTO(Integer ActivityTypeId, Integer userId);
    ActivityTypeDTO create(Integer userId, SaveActivityTypeDTO activityTypeDTO);
    ActivityTypeDTO update(Integer userId, Integer id, SaveActivityTypeDTO activityTypeDTO);
    void delete(Integer userId, Integer activityId);

}
