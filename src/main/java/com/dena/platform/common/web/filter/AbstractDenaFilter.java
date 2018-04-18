package com.dena.platform.common.web.filter;

import javax.servlet.*;
import java.io.IOException;

/**
 * @author Javad Alimohammadi [<bs.alimohammadi@gmail.com>]
 */
public abstract class AbstractDenaFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

    }

    @Override
    public void destroy() {

    }
}
