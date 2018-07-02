package com.dena.platform.core.feature.security;

import com.dena.platform.core.feature.user.domain.User;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

/**
 * @author Nazarpour.
 */

public class JWTAuthenticationToken extends UsernamePasswordAuthenticationToken {

    private String appId;

    private User user;


    public JWTAuthenticationToken(String appId, User user) {
        super(user.getEmail(), user.getUnencodedPassword());
        this.appId = appId;
        this.user = user;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}