package com.dena.platform.core.feature.security.authentication;

import com.dena.platform.common.web.DenaRequestContext;
import com.dena.platform.common.web.JSONMapper;
import com.dena.platform.core.feature.security.JwtAuthenticationToken;
import com.dena.platform.core.feature.security.JWTTokenService;
import com.dena.platform.core.feature.user.domain.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;

import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Use when user send user & password.
 *
 * @author Nazarpour.
 */
public class DenaUserPassAuthenticationFilter extends AbstractAuthenticationProcessingFilter {
    private final static Logger log = LoggerFactory.getLogger(DenaUserPassAuthenticationFilter.class);

    @Resource
    private JWTTokenService JWTTokenService;

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
        DenaRequestContext denaRequestContext = DenaRequestContext.getDenaRequestContext();

        String appId = denaRequestContext.getPathVariable("app_id");
        String requestBody = denaRequestContext.getRequestBody();
        User user = JSONMapper.createObjectFromJSON(requestBody, User.class);


        JwtAuthenticationToken authenticationToken = new JwtAuthenticationToken(appId, user);
        return authenticationToken;
    }


    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        super.successfulAuthentication(request, response, chain, authResult);
        chain.doFilter(request, response);
    }
}
