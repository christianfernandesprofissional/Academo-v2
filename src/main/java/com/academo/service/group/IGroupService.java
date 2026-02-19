package com.academo.service.group;

import com.academo.controller.dtos.group.AssociateSubjectsDTO;
import com.academo.controller.dtos.group.CreateGroupDTO;
import com.academo.controller.dtos.group.GroupDTO;
import com.academo.controller.dtos.group.UpdateGroupDTO;

import java.util.List;

public interface IGroupService{

    List<GroupDTO> findAll(Integer userId);
    GroupDTO findById(Integer userId, Integer id);
    GroupDTO create(Integer userId, CreateGroupDTO createGroupDTO);
    GroupDTO update(Integer userId, Integer groupId, UpdateGroupDTO updateGroupDTO);
    void delete(Integer userId, Integer groupId);
    GroupDTO addSubject(Integer userId, Integer groupId, Integer SubjectId);
    GroupDTO deleteSubject(Integer userId, Integer groupId, Integer SubjectId);
    GroupDTO associateSubjects(Integer userId, Integer groupId, AssociateSubjectsDTO dto);
}
