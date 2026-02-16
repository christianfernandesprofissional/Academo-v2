package com.academo.service.user;

import com.academo.model.User;
import com.academo.repository.UserRepository;
import com.academo.security.authuser.RegisterDTO;
import com.academo.security.service.TokenService;
import com.academo.service.profile.ProfileServiceImpl;
import com.academo.util.exceptions.user.AlreadyActivatedUserException;
import com.academo.util.exceptions.user.ExistingUserException;
import com.academo.util.exceptions.user.UserNotFoundException;
import com.academo.util.mailservice.IMailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class UserServiceImpl implements IUserService {

    private static final LocalDateTime ACTIVATION_TOKEN = LocalDateTime.now().plusMinutes(30).atOffset(ZoneOffset.of("-03:00")).toLocalDateTime();

    private final UserRepository userRepository;
    private final IMailService mailService;
    private final TokenService tokenService;

    public UserServiceImpl(UserRepository userRepository, IMailService mailService, TokenService tokenService) {
        this.userRepository = userRepository;
        this.mailService = mailService;
        this.tokenService = tokenService;
    }

    @Override
    public void createUser(RegisterDTO registerDTO) {
        if(userRepository.findByEmail(registerDTO.email()).isPresent()) throw new ExistingUserException();
        String encryptedPassword = new BCryptPasswordEncoder().encode(registerDTO.password());
        User user = new User();
        user.setEmail(registerDTO.email());
        user.setPassword(encryptedPassword);
        user.setName(registerDTO.name());
        user.setStorageUsage(0L);
        user.setActivationAccountTokenExpiration(ACTIVATION_TOKEN);
        User createdUser = userRepository.save(user);
        var token = tokenService.generateActivationToken(createdUser.getId());
        mailService.sendActivationMail(createdUser.getEmail(), token);
    }

    @Override
    public User activateUser(String token) {
        Integer userId = Integer.parseInt(tokenService.validateActivationToken(token));
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        if(!user.getAccountActivated()) {
            user.setAccountActivated(true);
            user.setActivationAccountTokenExpiration(LocalDateTime.now());
            mailService.sendWelcomeMail(user.getEmail());
            return userRepository.save(user);
        } else {
            throw new AlreadyActivatedUserException("Usuário já ativado na plataforma!");
        }
    }

    @Override
    public User findById(Integer id) {
        return userRepository.findById(id).orElseThrow(UserNotFoundException::new);
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(UserNotFoundException::new);
    }

    @Override
    public User update(User user) {
        return userRepository.save(user);
    }

}
