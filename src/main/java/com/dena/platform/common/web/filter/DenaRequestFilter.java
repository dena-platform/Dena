package com.dena.platform.common.web.filter;

import com.dena.platform.common.web.DenaRequestProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

/**
 * @author Javad Alimohammadi [<bs.alimohammadi@yahoo.com>]
 */
@Component("denaRequestFilter")
public class DenaRequestFilter extends AbstractDenaFilter {
    private final static Logger log = LoggerFactory.getLogger(DenaRequestFilter.class);

    @Resource
    protected DenaRequestProcessor denaRequestProcessor;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) {
        try {
            denaRequestProcessor.processRequest((HttpServletRequest) request);
            chain.doFilter(request, response);
        } catch (Exception ex) {
            log.error("Error in processing request", ex);
        } finally {
            denaRequestProcessor.postProcess();
        }
    }
}
