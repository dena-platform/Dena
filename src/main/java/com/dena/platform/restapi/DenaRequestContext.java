package com.dena.platform.restapi;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;

import javax.servlet.http.HttpServletRequest;
import java.util.stream.Collectors;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * @author Javad Alimohammadi<bs.alimohammadi@yahoo.com>
 */
public class DenaRequestContext {
    private final static Logger log = getLogger(DenaRequestContext.class);

    private HttpServletRequest request;
    private String requestBody;


    public boolean isPostRequest() {

    }

    public boolean isGetRequest() {

    }

    public HttpServletRequest getRequest() {
        return request;
    }

    public String getRequestBody() {
        if (StringUtils.isBlank(requestBody)) {
            try {
                requestBody = request.getReader().lines().collect(Collectors.joining());
            } catch (Exception ex) {
                log.error("Can not read request body", ex);
                throw new RuntimeException("Can not read request body", ex);
            }
        }
        return requestBody;
    }

    public DenaRequestContext(HttpServletRequest request) {
        this.request = request;
    }
}
