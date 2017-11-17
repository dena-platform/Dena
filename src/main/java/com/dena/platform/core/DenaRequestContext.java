package com.dena.platform.core;

import com.dena.platform.restapi.endpoint.API;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.HandlerMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * @author Javad Alimohammadi [<bs.alimohammadi@yahoo.com>]
 */
public class DenaRequestContext {
    private final static Logger log = getLogger(DenaRequestContext.class);

    private HttpServletRequest request;
    private String requestBody;
    private Map pathVariables;


    public boolean isPostRequest() {
        return RequestMethod.POST.name().equalsIgnoreCase(request.getMethod());
    }

    public boolean isGetRequest() {
        return RequestMethod.GET.name().equalsIgnoreCase(request.getMethod());
    }

    public HttpServletRequest getRequest() {
        return request;
    }

    public String getRequestBody() {
        if (StringUtils.isBlank(requestBody)) {
            try {
                requestBody = IOUtils.toString(request.getReader());
            } catch (Exception ex) {
                log.error("Can not read request body", ex);
                throw new RuntimeException("Can not read request body", ex);
            }
        }
        return requestBody;
    }

    public String getAppName() {
        return pathVariables.get(API.APP_ID).toString();
    }

    public String getTypeName() {
        return pathVariables.get(API.TYPE_NAME).toString();
    }

    public DenaRequestContext(HttpServletRequest request) {
        this.request = request;
        pathVariables = (Map) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
    }
}
