package com.dena.platform.core.feature.security;

import com.dena.platform.core.feature.security.model.JWTUserDetails;
import com.dena.platform.core.feature.user.domain.User;
import com.dena.platform.core.feature.user.service.DenaUserManagement;
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
    private JWTTokenService validator;

    @Resource
    private DenaUserManagement userManagement;


    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        JwtAuthenticationToken jwtAuthenticationToken = (JwtAuthenticationToken) authentication;
        String appId = jwtAuthenticationToken.getAppId();

        User user = jwtAuthenticationToken.getUser();

        User retrievedUser = userManagement.findUserById(appId, user.getEmail());

        if (retrievedUser != null && SecurityUtil.matchesPassword(user.getUnencodedPassword(), retrievedUser.getPassword())) {

        } else {
            throw new AuthenticationServiceException("not a valid token");
        }

        User user = validator.validate(appId);


        List<GrantedAuthority> grantedAuthorities = AuthorityUtils
                .commaSeparatedStringToAuthorityList("admin"); //todo change to proper role
        return new JWTUserDetails(user.getEmail(), appId, grantedAuthorities);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return (JwtAuthenticationToken.class.isAssignableFrom(authentication));
    }
}
