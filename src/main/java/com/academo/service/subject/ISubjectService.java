package com.academo.service.subject;

import com.academo.controller.dtos.subject.CreateSubjectDTO;
import com.academo.controller.dtos.subject.SubjectDTO;
import com.academo.model.Subject;

import java.util.List;

public interface ISubjectService {

    List<SubjectDTO> findAll(Integer userId);
    SubjectDTO findById(Integer subjectId, Integer userId);
    List<SubjectDTO> findByGroup(Integer groupId);
    SubjectDTO create(Integer userId, CreateSubjectDTO createSubjectDTO);
    SubjectDTO update(Integer userId, SubjectDTO subject);
    void delete(Integer userId, Integer subject);
}
