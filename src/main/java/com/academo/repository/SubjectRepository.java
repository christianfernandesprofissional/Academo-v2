package com.academo.repository;

import com.academo.model.Subject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SubjectRepository extends JpaRepository<Subject, Integer> {
    List<Subject> findAllByUserId(Integer userId);
    Optional<Subject> findByIdAndUserId(Integer subjectId, Integer userId);

}
