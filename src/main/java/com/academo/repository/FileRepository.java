package com.academo.repository;

import com.academo.model.File;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface FileRepository extends JpaRepository<File, UUID> {
    List<File> findAllBySubjectIdAndUserId(Integer subjectId, Integer userId);
    Page<File> findAllBySubjectIdAndUserId(Integer subjectId, Integer userId, Pageable pageable);
    Optional<File> findByUuidAndUserId(UUID uuid, Integer userId);
}
