package com.dena.platform.core.feature.security;

import com.dena.platform.core.feature.security.model.JWTUserDetails;
import com.dena.platform.core.feature.user.domain.User;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author Nazarpour.
 */
@Component
public class JWTAuthenticationProvider implements AuthenticationProvider {
    @Resource
    private TokenService validator;


    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        JwtAuthenticationToken jwtAuthenticationToken = (JwtAuthenticationToken) authentication;
        String token = jwtAuthenticationToken.getAppId();

        User user = validator.validate(token);

        if (user == null) {
            throw new AuthenticationServiceException("not a valid token");
        }

        List<GrantedAuthority> grantedAuthorities = AuthorityUtils
                .commaSeparatedStringToAuthorityList("admin"); //todo change to proper role
        return new JWTUserDetails(user.getEmail(), token, grantedAuthorities);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return (JwtAuthenticationToken.class.isAssignableFrom(authentication));
    }
}
