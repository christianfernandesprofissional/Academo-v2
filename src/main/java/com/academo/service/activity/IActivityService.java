package com.academo.service.activity;

import com.academo.controller.dtos.activity.ActivityDTO;
import com.academo.controller.dtos.activity.SaveActivityDTO;

import java.util.List;

public interface IActivityService {
    List<ActivityDTO> findAll(Integer userId);
    ActivityDTO findById(Integer userId, Integer activityId);
    ActivityDTO create(Integer userId, SaveActivityDTO activityDTO);
    ActivityDTO update(Integer userId, Integer activityId, SaveActivityDTO activityDTO);
    void delete(Integer userId,Integer activityId);
    Boolean existsByName(String activityName);
    // Isso pode ser substituído por um -> if(repository.findById(id).isPresent() throw new... -> Basta transformar o retorno de findById em Optional
    Boolean existsById(Integer id);
    List<ActivityDTO> findBySubjectId(Integer subjectId);
}
