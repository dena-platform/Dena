package com.dena.platform.config;

import com.dena.platform.restapi.endpoint.v1.API;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Javad Alimohammadi<bs.alimohammadi@gmail.com>
 */

public final class AllowedRequestMatcher implements RequestMatcher {
    private AntPathRequestMatcher registerUrlPath = new AntPathRequestMatcher(API.API_PATH + "*/users/register", null, false);
    private AntPathRequestMatcher loginUrlPath = new AntPathRequestMatcher(API.API_PATH + "*/users/login", null, false);

    public static AllowedRequestMatcher INSTANCE = new AllowedRequestMatcher();

    private AllowedRequestMatcher() {
    }

    @Override
    public boolean matches(HttpServletRequest request) {
        return registerUrlPath.matches(request) || loginUrlPath.matches(request);
    }
}
