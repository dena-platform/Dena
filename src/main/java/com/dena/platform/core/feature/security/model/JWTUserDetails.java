package com.dena.platform.core.feature.security.model;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.List;

public class JWTUserDetails implements Authentication {

    private String userName;
    private String token;
    private Long id;
    private boolean isAuthenticated;
    private Collection<? extends GrantedAuthority> authorities;


    public JWTUserDetails(String userName, String token, List<GrantedAuthority> grantedAuthorities) {
        this.userName = userName;
        this.token = token;
        this.authorities = grantedAuthorities;
        this.isAuthenticated = true;
    }


    public String getUserName() {
        return userName;
    }

    public String getToken() {
        return token;
    }


    public Long getId() {
        return id;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
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
        return userName;
    }
}
