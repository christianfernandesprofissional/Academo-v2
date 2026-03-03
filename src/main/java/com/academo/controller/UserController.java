package com.academo.controller;

import com.academo.controller.dtos.security.LoginResponseDTO;
import com.academo.controller.dtos.security.RegisterDTO;
import com.academo.controller.dtos.security.UserAuthDTO;
import com.academo.controller.dtos.user.UserDTO;
import com.academo.model.User;
import com.academo.security.authuser.*;
import com.academo.security.service.TokenService;
import com.academo.service.user.IUserService;
import com.academo.util.exceptions.user.ExistingUserException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Usuários")
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

    @Operation(summary = "Realiza login do Usuário no sistema", method = "POST")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Login realizado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Erro ao tentar acessar a conta"),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
    })
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody @Valid UserAuthDTO user) {
        UsernamePasswordAuthenticationToken userPass = new UsernamePasswordAuthenticationToken(user.email(), user.password());
        Authentication auth = authenticationManager.authenticate(userPass);
        var token = tokenService.generateLoginToken((AuthUser) auth.getPrincipal());
        logger.info("[DEBUG] Token: {}", token);
        User u = userService.findByEmail(user.email());
        return ResponseEntity.ok(new LoginResponseDTO(token, u.getId(), u.getName()));
    }

    @Operation(summary = "Cadastra usuário no sistema", method = "POST")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Usuário cadastrado com sucesso. Verifique sua caixa de email"),
            @ApiResponse(responseCode = "400", description = "Este usuário já está cadastrado no sistema"),
    })
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody @Valid RegisterDTO register) throws ExistingUserException {
        userService.createUser(register);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Operation(summary = "Realiza a ativação da conta", method = "POST")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Conta ativada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado"),
            @ApiResponse(responseCode = "400", description = "Token expirado")
    })
    @PostMapping("/activate")
    public ResponseEntity<UserDTO> activate(@RequestParam("token") String token) {
        logger.debug("[DEBUG] Token: {}", token);
        return ResponseEntity.status(HttpStatus.OK).body(userService.activateUser(token));
    }





}
