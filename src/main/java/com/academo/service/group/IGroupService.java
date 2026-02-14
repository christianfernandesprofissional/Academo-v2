package com.academo.service.group;

import com.academo.model.Group;

import java.util.List;

public interface IGroupService{

    List<Group> findAll(Integer userId);
    Group findById(Integer userId, Integer id);
    Group create(Integer userId, Group group);
    Group update(Integer userId, Group group);
    void remove(Integer userId, Integer groupId);
    Group addSubjectToGroup(Integer userId, Integer groupId, Integer SubjectId);
    Group deleteSubjectFromGroup(Integer userId, Integer groupId, Integer SubjectId);
    Group associateSubjects(Integer userId, Integer groupId, List<Integer> subjectsIds);

}
