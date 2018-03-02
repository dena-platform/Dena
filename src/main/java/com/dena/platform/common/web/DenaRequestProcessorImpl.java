package com.dena.platform.common.web;

import com.dena.platform.common.utils.ThreadLocalManager;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Javad Alimohammadi [<bs.alimohammadi@yahoo.com>]
 */
@Component("denaRequestProcessor")
public class DenaRequestProcessorImpl implements DenaRequestProcessor {
    @Override
    public void processRequest(HttpServletRequest request) {
        DenaRequestContext denaRequestContext = new DenaRequestContext(request);
        DenaRequestContext.setDenaRequestContext(denaRequestContext);
    }

    @Override
    public void postProcess() {
        ThreadLocalManager.remove();
    }
}
