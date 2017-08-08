package com.dena.platform.restapi;

import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Javad Alimohammadi<bs.alimohammadi@yahoo.com>
 */
@Service("denaDTOProcessor")
public class DTOProcessorImpl implements DTOProcessor {

    @Override
    public void processDTO(HttpServletRequest request) {
        IOUtils.toString(request.getReader());
    }
}
