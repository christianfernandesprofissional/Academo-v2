package com.academo.service.subject;

import com.academo.controller.dtos.subject.SubjectDTO;
import com.academo.model.Subject;

import java.util.List;

public interface ISubjectService {

    public List<SubjectDTO> findAll(Integer userId);
    public SubjectDTO findBySubjectId(Integer subjectId, Integer userId);
    public List<SubjectDTO> findByGroup(Integer groupId);
    public SubjectDTO create(String name, String description, Integer userId);
    public SubjectDTO updateSubject(Integer userId, SubjectDTO subject);
    public void deleteSubject(Integer userId, Integer subject);
}
