package com.dena.platform.core.feature.security.authentication;

import com.dena.platform.common.web.DenaRequestContext;
import com.dena.platform.common.web.JSONMapper;
import com.dena.platform.core.feature.security.JWTService;
import com.dena.platform.core.feature.security.SecurityUtil;
import com.dena.platform.core.feature.security.model.JWTUserDetails;
import com.dena.platform.core.feature.user.domain.User;
import com.dena.platform.core.feature.user.service.DenaUserManagement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;

import javax.annotation.Resource;
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
