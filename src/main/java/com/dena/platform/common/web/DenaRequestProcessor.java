package com.dena.platform.common.web;

import org.springframework.web.util.ContentCachingRequestWrapper;

/**
 * @author Javad Alimohammadi [<bs.alimohammadi@yahoo.com>]
 */
public interface DenaRequestProcessor {
    /**
     * Process incoming http request.
     *
     * @param request
     */
    void processRequest(ContentCachingRequestWrapper request);

    void postProcess();
}
