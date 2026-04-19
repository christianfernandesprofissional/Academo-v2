package com.academo.service.subject;

import com.academo.controller.dtos.subject.CreateSubjectDTO;
import com.academo.controller.dtos.subject.SubjectDTO;
import com.academo.controller.dtos.subject.UpdateSubjectDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ISubjectService {

    Page<SubjectDTO> findAll(Integer userId, Pageable pageable);
    SubjectDTO findById(Integer subjectId, Integer userId);
    Page<SubjectDTO> findByGroup(Integer groupId, Pageable pageable);
    SubjectDTO create(Integer userId, CreateSubjectDTO createSubjectDTO);
    SubjectDTO update(Integer userId, Integer subjectId, UpdateSubjectDTO updateSubjectDTO);
    void delete(Integer userId, Integer subject);
}
