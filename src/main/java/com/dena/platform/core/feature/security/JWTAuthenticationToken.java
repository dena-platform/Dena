package com.dena.platform.core.feature.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import javax.naming.OperationNotSupportedException;
import java.util.Collection;

/**
 * @author Nazarpour.
 */

public class JWTAuthenticationToken implements Authentication {

    private String token;
    private boolean isAuthenticated = false;



    public JWTAuthenticationToken(String token) {
        this.token = token;
    }

    public JWTAuthenticationToken(String token, boolean isAuthenticated) {
        this.token = token;
        this.isAuthenticated = isAuthenticated;
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
        throw new UnsupportedOperationException("This method should not used");
    }

    @Override
    public String getName() {
        return null;
    }
}