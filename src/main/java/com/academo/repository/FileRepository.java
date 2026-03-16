package com.academo.repository;

import com.academo.model.File;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface FileRepository extends JpaRepository<File, UUID> {
    List<File> findAllBySubjectIdAndUserId(Integer subjectId, Integer userId);
    Optional<File> findByUuidAndUserId(UUID uuid, Integer userId);
}
