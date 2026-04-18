package com.academo.service.activity;

import com.academo.controller.dtos.activity.ActivityDTO;
import com.academo.controller.dtos.activity.SaveActivityDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IActivityService {
    Page<ActivityDTO> findAll(Integer userId, Pageable pageable);
    ActivityDTO findById(Integer userId, Integer activityId);
    ActivityDTO create(Integer userId, SaveActivityDTO activityDTO);
    ActivityDTO update(Integer userId, Integer activityId, SaveActivityDTO activityDTO);
    void delete(Integer userId,Integer activityId);
    Boolean existsByName(String activityName);
    // Isso pode ser substituído por um -> if(repository.findById(id).isPresent() throw new... -> Basta transformar o retorno de findById em Optional
    Boolean existsById(Integer id);
    Page<ActivityDTO> findAllBySubjectId(Integer userId, Integer subjectId, Pageable pageable);
}
