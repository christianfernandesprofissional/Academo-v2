package com.academo.controller;

import com.academo.model.User;
import com.academo.repository.UserRepository;
import com.academo.security.authuser.*;
import com.academo.security.service.TokenService;
import com.academo.service.profile.ProfileServiceImpl;
import com.academo.util.exceptions.user.ExistingUserException;
import com.academo.util.exceptions.user.UserNotFoundException;
import com.academo.util.exceptions.user.WrongDataException;
import com.academo.util.mailservice.IMailService;
import com.academo.util.mailservice.JavaMailApp;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

@RestController
@RequestMapping("/auth")
@Tag(name = "Usuários")
public class UserController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProfileServiceImpl profileService;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private IMailService mail;

    @Operation(summary = "Realiza login do Usuário no sistema", method = "POST")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Login realizado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Erro ao tentar acessar a conta"),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
    })
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody UserAuthDTO user) {
        try {
            UsernamePasswordAuthenticationToken userPass = new UsernamePasswordAuthenticationToken(user.username(), user.password());
            Authentication auth = authenticationManager.authenticate(userPass);
            var token = tokenService.generateLoginToken((AuthUser) auth.getPrincipal());
            User u = userRepository.findByEmail(user.username());
            if(u == null) u = userRepository.findByName(user.username());
            return ResponseEntity.ok(new LoginResponseDTO(token, u.getId(), u.getName()));
        } catch (Exception e) {
            throw new WrongDataException();
        }
    }

    @Operation(summary = "Cadastra usuário no sistema", method = "POST")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Usuário cadastrado com sucesso. Verifique sua caixa de email"),
            @ApiResponse(responseCode = "400", description = "Este usuário já está cadastrado no sistema"),
    })
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterDTO register) throws ExistingUserException {
        if(userRepository.findByName(register.name()) != null ||
                userRepository.findByEmail(register.email()) != null) throw new ExistingUserException();

        String encryptedPassword = new BCryptPasswordEncoder().encode(register.password());
        User user = new  User(register.name(), encryptedPassword,register.email());
        user.setStorageUsage(0L);
        LocalDateTime expiresAt = LocalDateTime.now().plusMinutes(30).plusSeconds(20).atOffset(ZoneOffset.of("-03:00")).toLocalDateTime();
        user.setTokenExpiresAt(expiresAt);
        User createdUser = userRepository.save(user);
        profileService.create(createdUser);
        enviarEmailDeAtivacao(createdUser.getEmail(), createdUser.getId());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Operation(summary = "Realiza a ativação da conta", method = "POST")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Conta ativada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado"),
            @ApiResponse(responseCode = "400", description = "Token expirado")
    })
    @PostMapping("/activate")
    public ResponseEntity<User> activate(@RequestParam("value") String token) {
        Integer userId = Integer.parseInt(tokenService.validateActivationToken(token));
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        if(!user.getIsActive()) {
            user.setTokenExpiresAt(LocalDateTime.now());
            user.setIsActive(true);
            userRepository.save(user);
            mail.enviarEmailBoasVindas(user.getEmail());
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    /*
    -------- Métodos Auxiliares
     */

    private void enviarEmailDeAtivacao(String email, Integer userId) {
        var token = tokenService.generateActivationToken(userId);
        mail.enviarEmailDeAtivacao(email, token);
    }

}
