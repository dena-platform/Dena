package com.dena.platform.core.feature.security;

import com.dena.platform.core.feature.user.domain.User;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

/**
 * @author Nazarpour.
 */

public class JWTAuthenticationToken extends UsernamePasswordAuthenticationToken {

    private User user;

    public JWTAuthenticationToken(User user) {
        super(user.getEmail(), user.getUnencodedPassword());
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}