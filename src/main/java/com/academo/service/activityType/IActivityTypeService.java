package com.academo.service.activityType;

import com.academo.controller.dtos.activity.SaveActivityDTO;
import com.academo.controller.dtos.activityType.ActivityTypeDTO;
import com.academo.controller.dtos.activityType.SaveActivityTypeDTO;
import com.academo.controller.dtos.activityType.UpdateActivityTypeDTO;
import com.academo.model.ActivityType;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IActivityTypeService {

    Page<ActivityTypeDTO> findAll(Integer userId, Integer periodId, Pageable pageable);
    ActivityType findById(Integer ActivityTypeId, Integer userId);
    ActivityTypeDTO findDTO(Integer ActivityTypeId, Integer userId);
    ActivityTypeDTO create(Integer userId, SaveActivityTypeDTO activityTypeDTO);
    ActivityTypeDTO update(Integer userId, Integer id, UpdateActivityTypeDTO activityTypeDTO);
    void delete(Integer userId, Integer activityId);

}
