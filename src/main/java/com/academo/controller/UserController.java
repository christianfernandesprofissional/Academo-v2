package com.academo.controller;

import com.academo.controller.dtos.security.*;
import com.academo.controller.dtos.user.UserDTO;
import com.academo.model.User;
import com.academo.security.authuser.*;
import com.academo.security.service.TokenService;
import com.academo.service.user.IUserService;
import com.academo.util.exceptions.user.ExistingUserException;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    private final AuthenticationManager authenticationManager;
    private final IUserService userService;
    private final TokenService tokenService;

    public UserController(AuthenticationManager authenticationManager, IUserService userService, TokenService tokenService) {
        this.authenticationManager = authenticationManager;
        this.userService = userService;
        this.tokenService = tokenService;
    }
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody @Valid UserAuthDTO user) {
        UsernamePasswordAuthenticationToken userPass = new UsernamePasswordAuthenticationToken(user.email(), user.password());
        Authentication auth = authenticationManager.authenticate(userPass);
        var token = tokenService.generateLoginToken((AuthUser) auth.getPrincipal());
        User u = userService.login(user.email());
        return ResponseEntity.ok(new LoginResponseDTO(token, u.getId(), u.getName()));
    }
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody @Valid RegisterDTO register) throws ExistingUserException {
        userService.createUser(register);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
    @PostMapping("/activate")
    public ResponseEntity<Void> activate(@RequestParam("token") String token) {
        userService.activateUser(token);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PostMapping("forgot-password")
    public ResponseEntity<Void> forgotPassword(@RequestBody @Valid ForgotPasswordDTO forgotPasswordDTO) {
        userService.forgotPassword(forgotPasswordDTO);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PostMapping("/reset-password")
    public ResponseEntity<Void> resetPassword(@RequestParam("token") String token, @Valid @RequestBody ResetPasswordDTO resetPasswordDTO) {
        userService.resetPassword(token, resetPasswordDTO);
        return ResponseEntity.ok().build();
    }
}
