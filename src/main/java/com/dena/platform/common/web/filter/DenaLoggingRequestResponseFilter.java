package com.dena.platform.common.web.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import java.io.IOException;

/**
 * @author Javad Alimohammadi [<bs.alimohammadi@gmail.com>]
 */

public class DenaLoggingRequestResponseFilter extends AbstractDenaFilter {
    private final static Logger log = LoggerFactory.getLogger(DenaLoggingRequestResponseFilter.class);

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        super.init(filterConfig);
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        super.doFilter(request, response, chain);
    }

    @Override
    public void destroy() {
        super.destroy();
    }
}
