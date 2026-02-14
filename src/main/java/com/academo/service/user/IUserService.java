package com.academo.service.user;

import com.academo.model.User;
import com.academo.security.authuser.RegisterDTO;

import java.util.List;

public interface IUserService {

    void createUser(RegisterDTO registerDTO);
    User activateUser(String token);
    User findById(Integer id);
    User findByEmail(String email);
    User update(User user);
}