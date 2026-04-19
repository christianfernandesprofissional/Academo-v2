package com.academo.repository;

import com.academo.model.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GroupRepository extends JpaRepository<Group, Integer> {

    List<Group> findAllByUserId(Integer userId);
    Page<Group> findAllByUserId(Integer userId, Pageable pageable);
    Optional<Group> findByIdAndUserId(Integer id, Integer userId);

}
