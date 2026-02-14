package com.academo.repository;

import com.academo.controller.dtos.group.GroupDTO;
import com.academo.model.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GroupRepository extends JpaRepository<Group, Integer> {

    @Query(nativeQuery = true, )
    List<GroupDTO> findAll(Integer userId);
    public Optional<Group> findByIdAndUserId(Integer id, Integer userId);
}
