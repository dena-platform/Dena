package com.dena.platform.common.web;

import com.dena.platform.common.utils.ThreadLocalManager;
import org.springframework.stereotype.Component;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponseWrapper;

/**
 * @author Javad Alimohammadi [<bs.alimohammadi@yahoo.com>]
 */
@Component("denaRequestProcessor")
public class DenaRequestProcessorImpl implements DenaRequestProcessor {

    @Override
    public void processRequest(HttpServletRequest request) {
        DenaRequestContext denaRequestContext = new DenaRequestContext(new ContentCachingRequestWrapper(request));
        DenaRequestContext.setDenaRequestContext(denaRequestContext);
    }

    @Override
    public void postProcess() {
        ThreadLocalManager.remove();
    }
}
