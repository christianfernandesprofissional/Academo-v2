package com.academo.repository;

import com.academo.model.File;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FileRepository extends JpaRepository<File, String> {
    public List<File> findAllBySubjectId(Integer subjectId);
}
