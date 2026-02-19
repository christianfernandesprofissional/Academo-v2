package com.academo.repository;

import com.academo.model.File;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FileRepository extends JpaRepository<File, String> {
    List<File> findAllBySubjectId(Integer subjectId);
}
