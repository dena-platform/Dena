package com.dena.platform.core.feature.security;

import com.dena.platform.core.feature.security.service.JWTService;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author Nazarpour.
 */
@Component
public class JWTAuthenticationProvider implements AuthenticationProvider {
    @Resource
    private JWTService jwtService;


    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        JWTAuthenticationToken jwtAuthenticationToken = (JWTAuthenticationToken) authentication;

        String jwtToken = jwtAuthenticationToken.getToken();

        if (jwtService.isTokenValid(jwtToken)) {
            JWTAuthenticationToken authenticatedUser = new JWTAuthenticationToken(jwtToken, true);
            return authenticatedUser;
        } else {
            throw new BadCredentialsException("User name or password is invalid");
        }


    }

    @Override
    public boolean supports(Class<?> authentication) {
        return (JWTAuthenticationToken.class.isAssignableFrom(authentication));
    }
}
