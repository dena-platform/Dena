package com.dena.platform.common.web;

import com.dena.platform.common.utils.ThreadLocalManager;
import org.springframework.stereotype.Component;
import org.springframework.web.util.ContentCachingRequestWrapper;

/**
 * @author Javad Alimohammadi [<bs.alimohammadi@gmail.com>]
 */
@Component("denaRequestProcessor")
public class DenaRequestProcessorImpl implements DenaRequestProcessor {

    @Override
    public void processRequest(ContentCachingRequestWrapper request) {

        DenaRequestContext denaRequestContext = new DenaRequestContext(request);
        DenaRequestContext.setDenaRequestContext(denaRequestContext);
    }

    @Override
    public void postProcess() {
        ThreadLocalManager.remove();
    }
}
