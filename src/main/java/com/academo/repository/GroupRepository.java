package com.academo.repository;

import com.academo.controller.dtos.group.GroupDTO;
import com.academo.model.Group;
import com.academo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GroupRepository extends JpaRepository<Group, Integer> {


    List<Group> findAllByUserId(Integer userId);
    Optional<Group> findById(Integer id, Integer userId);

}
