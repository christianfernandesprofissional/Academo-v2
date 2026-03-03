package com.academo.security.service;

import com.academo.model.User;
import com.academo.repository.UserRepository;
import com.academo.security.authuser.AuthUser;
import com.academo.util.exceptions.user.UserIsNotActiveException;
import com.academo.util.exceptions.user.UserNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
public class AuthUserService implements UserDetailsService {

    private static final Logger logger = LoggerFactory.getLogger(AuthUserService.class);

    private final UserRepository userRepository;

    public AuthUserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UserIsNotActiveException {
        logger.info("[DEBUG] loadUserByUsername username: {}", username);
        User user = userRepository.findByEmail(username).orElseThrow(UserNotFoundException::new);

        if(user.getAccountActivated()) {
            return new AuthUser(user);
        } else {
            throw  new UserIsNotActiveException("O usuário não está ativo", user);
        }
    }


}
