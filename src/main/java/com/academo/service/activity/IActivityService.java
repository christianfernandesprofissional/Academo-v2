package com.academo.service.activity;

import com.academo.controller.dtos.activity.ActivityDTO;
import com.academo.model.Activity;

import java.util.List;
import java.util.Optional;

public interface IActivityService {
    List<ActivityDTO> findAll(Integer userId);
    ActivityDTO findById(Integer userId, Integer activityId);
    ActivityDTO create(Activity activity, Integer userId, Integer activityTypeId, Integer subjectId);
    ActivityDTO update(Activity activity, Integer userId, Integer activityTypeId, Integer subjectId);
    void delete(Integer userId,Integer activityId);
    Boolean existsActivityByName(String activityName);
    // Isso pode ser substituído por um -> if(repository.findById(id).isPresent() throw new... -> Basta transformar o retorno de findById em Optional
    Boolean existsActivityById(Integer id);
    List<ActivityDTO> getBySubjectId(Integer subjectId);
}
