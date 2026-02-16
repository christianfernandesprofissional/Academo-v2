package com.academo.service.group;

import com.academo.controller.dtos.group.GroupDTO;
import com.academo.model.Group;

import java.util.List;

public interface IGroupService{

    List<GroupDTO> findAll(Integer userId);
    GroupDTO findById(Integer userId, Integer id);
    GroupDTO create(Integer userId, Group group);
    GroupDTO update(Integer userId, Group group);
    void delete(Integer userId, Integer groupId);
    GroupDTO addSubject(Integer userId, Integer groupId, Integer SubjectId);
    GroupDTO deleteSubject(Integer userId, Integer groupId, Integer SubjectId);
    GroupDTO associateSubjects(Integer userId, Integer groupId, List<Integer> subjectsIds);
}
