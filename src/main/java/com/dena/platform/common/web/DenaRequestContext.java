package com.dena.platform.common.web;

import com.dena.platform.common.utils.ThreadLocalManager;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.util.ContentCachingRequestWrapper;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * An holder class that contain various request data.
 *
 * @author Javad Alimohammadi [<bs.alimohammadi@gmail.com>]
 */
public class DenaRequestContext {
    private final static Logger log = getLogger(DenaRequestContext.class);

    private ContentCachingRequestWrapper request;

    private String requestBody;


    private final static ThreadLocal<DenaRequestContext> ZAGROS_REQUEST_CONTEXT = ThreadLocalManager.createThreadLocal(DenaRequestContext.class);


    public DenaRequestContext(ContentCachingRequestWrapper request) {
        this.request = request;
    }


    public static DenaRequestContext getDenaRequestContext() {
        return ZAGROS_REQUEST_CONTEXT.get();
    }


    public static void setDenaRequestContext(DenaRequestContext zagrosRequestContext) {
        ZAGROS_REQUEST_CONTEXT.set(zagrosRequestContext);
    }


    public boolean isPostRequest() {
        return RequestMethod.POST.name().equalsIgnoreCase(request.getMethod());
    }

    public boolean isPutRequest() {
        return RequestMethod.PUT.name().equalsIgnoreCase(request.getMethod());
    }

    public boolean isGetRequest() {
        return RequestMethod.GET.name().equalsIgnoreCase(request.getMethod());
    }

    public boolean isDeleteRequest() {
        return RequestMethod.DELETE.name().equalsIgnoreCase(request.getMethod());
    }

    public boolean isPatchRequest() {
        return RequestMethod.PATCH.name().equalsIgnoreCase(request.getMethod());
    }

    public HttpServletRequest getRequest() {
        return request;
    }

    public synchronized String getRequestBody() {
        if (StringUtils.isBlank(requestBody)) {
            try {
                if (request.getContentAsByteArray().length == 0) {
                    requestBody = IOUtils.toString(request.getReader());
                } else {
                    requestBody = new String(request.getContentAsByteArray());
                }
            } catch (Exception ex) {
                log.error("Can not read request body", ex);
                throw new RuntimeException("Can not read request body", ex);
            }
        }
        return requestBody;
    }


    public String getPathVariable(String pathName) {
        Object val = ((Map) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE)).get(pathName);
        if (val == null) {
            return "";
        }
        return val.toString();
    }

    public String getParameter(String name) {
        return request.getParameter(name);
    }
}
