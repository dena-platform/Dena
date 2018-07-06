package com.dena.platform.core.feature.security.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Perform handleLoginUser operation when user send user & password parameter and send JWT token in response.
 *
 * @author Nazarpour.
 */

public class DenaUserPassAuthenticationFilter extends AbstractAuthenticationProcessingFilter {
    private final static Logger log = LoggerFactory.getLogger(DenaUserPassAuthenticationFilter.class);


    public DenaUserPassAuthenticationFilter(String filterProcessingURL) {
        super(filterProcessingURL);
    }

    /**
     * @param request
     * @param response
     * @return
     * @throws AuthenticationException
     */
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        return null;
    }

}
