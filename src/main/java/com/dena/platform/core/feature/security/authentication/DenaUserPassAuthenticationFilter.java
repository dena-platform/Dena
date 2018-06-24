package com.dena.platform.core.feature.security.authentication;

import com.dena.platform.core.feature.security.JwtAuthenticationToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * When user send simple user & pass this filter call.
 * @author Nazarpour.
 */
public class DenaUserPassAuthenticationFilter extends AbstractAuthenticationProcessingFilter {
    private final static Logger log = LoggerFactory.getLogger(DenaUserPassAuthenticationFilter.class);

    public DenaUserPassAuthenticationFilter(String filterProcessingURL) {
        super(filterProcessingURL);
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


    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        super.successfulAuthentication(request, response, chain, authResult);
        chain.doFilter(request, response);
    }
}
