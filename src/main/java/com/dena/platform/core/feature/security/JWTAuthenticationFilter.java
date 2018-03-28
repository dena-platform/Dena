package com.dena.platform.core.feature.security;

import com.dena.platform.restapi.endpoint.v1.API;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Nazarpour.
 */
public class JWTAuthenticationFilter extends AbstractAuthenticationProcessingFilter {
    private final static Logger log = LoggerFactory.getLogger(JWTAuthenticationFilter.class);

    public JWTAuthenticationFilter() {
        super(API.API_PATH);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
        String token = request.getHeader("token");
        if (token == null) {
            log.info("null token passed in the request");
            throw new AuthenticationServiceException("not a valid token");
        }
        JwtAuthenticationToken authenticationToken = new JwtAuthenticationToken(token);
        return getAuthenticationManager().authenticate(authenticationToken);
    }
}
