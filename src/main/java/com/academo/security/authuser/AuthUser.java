package com.academo.security.authuser;

import com.academo.model.User;
import com.academo.model.enums.UserRole;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;


public class AuthUser implements UserDetails {

    private static final Logger logger = LoggerFactory.getLogger(AuthUser.class);

    private final User user;

    public AuthUser(User user) {
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(user.getRole().name()));
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        logger.info("[DEBUG] Username do AuthUser: {}", user.getEmail());
        return user.getEmail();
    }

    public User getUser() {return user;}

    public UserRole getUserRole() { return user.getRole(); }
}
