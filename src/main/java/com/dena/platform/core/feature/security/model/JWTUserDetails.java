package com.dena.platform.core.feature.security.model;

import com.dena.platform.core.feature.user.domain.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class JWTUserDetails implements Authentication {
    private User denaUser;
    private String token;
    private boolean isAuthenticated;


    public JWTUserDetails(User denaUser, String token) {
        this.denaUser = denaUser;
        this.token = token;
        this.isAuthenticated = true;
    }

    public String getToken() {
        return token;
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getDetails() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return null;
    }

    @Override
    public boolean isAuthenticated() {
        return isAuthenticated;
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getName() {
        return denaUser.getEmail();
    }
}
