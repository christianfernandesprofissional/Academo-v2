package com.academo.repository;

import com.academo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    User findByName(String name);
    Optional<User> findByEmail(String email);
}
