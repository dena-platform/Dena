package com.dena.platform.core.feature.security.filter;

import com.dena.platform.common.web.DenaRequestContext;
import com.dena.platform.core.feature.security.JWTAuthenticationToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Perform login operation based on JWT token in request.
 *
 * @author Javad Alimohammadi<bs.alimohammadi@yahoo.com>
 */

public class JWTAuthenticationFilter extends OncePerRequestFilter {
    private final static Logger log = LoggerFactory.getLogger(JWTAuthenticationFilter.class);


    private AuthenticationManager authenticationManager;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        log.trace("Get authorization header");
        String jwtToken = DenaRequestContext.getDenaRequestContext().getToken();
        JWTAuthenticationToken jwtAuthenticationToken = new JWTAuthenticationToken(jwtToken);

        try {
            Authentication authentication = authenticationManager.authenticate(jwtAuthenticationToken);

            // Token is valid
            SecurityContextHolder.getContext().setAuthentication(authentication);

        } catch (AuthenticationException e) {
            log.trace("Provided JWT token is invalid", e);
        }

        filterChain.doFilter(request, response);

    }

    public JWTAuthenticationFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

}
