package com.academo.service.user;

import com.academo.controller.dtos.security.ForgotPasswordDTO;
import com.academo.controller.dtos.security.ResetPasswordDTO;
import com.academo.controller.dtos.security.TokenPasswordDTO;
import com.academo.controller.dtos.user.UserDTO;
import com.academo.model.User;
import com.academo.controller.dtos.security.RegisterDTO;

public interface IUserService {

    void createUser(RegisterDTO registerDTO);
    UserDTO activateUser(String token);
    User findById(Integer id);
    User findByEmail(String email);
    UserDTO update(User user);
    TokenPasswordDTO forgotPassword(ForgotPasswordDTO forgotPasswordDTO);
    void resetPassword(String token, ResetPasswordDTO resetPasswordDTO);
}