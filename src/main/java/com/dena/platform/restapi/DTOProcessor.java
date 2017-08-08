package com.dena.platform.restapi;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Javad Alimohammadi<bs.alimohammadi@yahoo.com>
 */
public interface DTOProcessor {
    void processDTO(HttpServletRequest request);
}
