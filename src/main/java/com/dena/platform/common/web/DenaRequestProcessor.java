package com.dena.platform.common.web;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Javad Alimohammadi [<bs.alimohammadi@yahoo.com>]
 */
public interface DenaRequestProcessor {
    /**
     * Process incoming http request.
     *
     * @param request
     */
    void processRequest(HttpServletRequest request);

    void postProcess();
}
