package com.dena.platform.core.feature.security;

import com.dena.platform.core.feature.security.model.JWTUserDetails;
import com.dena.platform.core.feature.user.domain.User;
import com.dena.platform.core.feature.user.service.DenaUserManagement;
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

    @Resource
    private DenaUserManagement userManagement;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        JwtAuthenticationToken jwtAuthenticationToken = (JwtAuthenticationToken) authentication;
        String appId = jwtAuthenticationToken.getAppId();

        User user = jwtAuthenticationToken.getUser();

        User retrievedUser = userManagement.findUserById(appId, user.getEmail());

        if (retrievedUser != null && SecurityUtil.matchesPassword(user.getUnencodedPassword(), retrievedUser.getPassword())) {
            jwtService.generateJWTToken(appId, retrievedUser);
            return new JWTUserDetails(user.getEmail(), appId);
        } else {
            throw new BadCredentialsException("User name or password is invalid");
        }

    }

    @Override
    public boolean supports(Class<?> authentication) {
        return (JwtAuthenticationToken.class.isAssignableFrom(authentication));
    }
}
