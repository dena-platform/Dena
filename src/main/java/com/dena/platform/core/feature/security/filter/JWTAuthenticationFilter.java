package com.dena.platform.core.feature.security.filter;

import com.dena.platform.core.feature.security.JWTAuthenticationToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Javad Alimohammadi<bs.alimohammadi@yahoo.com>
 */

public class JWTAuthenticationFilter extends GenericFilterBean {
    private final static Logger log = LoggerFactory.getLogger(JWTAuthenticationFilter.class);

    private AntPathRequestMatcher path;

    public JWTAuthenticationFilter(String path) {
        this.path = new AntPathRequestMatcher(path);
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;


        if (path.matches(httpServletRequest)) {
            log.info("should not intercept this url");
            chain.doFilter(request, response);
        } else {
            log.info("Get authorization header");
            String jwtToken = httpServletRequest.getHeader(HttpHeaders.AUTHORIZATION);

            JWTAuthenticationToken jwtAuthenticationToken = new JWTAuthenticationToken(jwtToken);
            SecurityContextHolder.getContext().setAuthentication(jwtAuthenticationToken);
            chain.doFilter(request, response);
        }
    }
}
