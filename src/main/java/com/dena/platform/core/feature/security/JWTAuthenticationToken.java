package com.dena.platform.core.feature.security;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

/**
 * @author Nazarpour.
 */

public class JWTAuthenticationToken extends UsernamePasswordAuthenticationToken {

    private String token;

    public JWTAuthenticationToken(String token) {
        super(null, null);
        this.token = token;
    }

    public String getToken() {
        return token;
    }

}