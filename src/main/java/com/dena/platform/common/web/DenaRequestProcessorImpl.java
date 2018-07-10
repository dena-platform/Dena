package com.dena.platform.common.web;

import com.dena.platform.common.utils.ThreadLocalManager;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
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

        /////////////////////////////////////////////
        //            Set app_id
        /////////////////////////////////////////////
        String path = request.getRequestURL().toString();
        int indexOfVersion = path.indexOf("v");
        int beginAppNameIndex = path.indexOf("/", indexOfVersion) + 1;
        int endAppNameIndex = path.indexOf("/", beginAppNameIndex) - 1;
        String appId = path.substring(beginAppNameIndex, endAppNameIndex + 1);

        denaRequestContext.setAppId(appId);

        /////////////////////////////////////////////
        //            Set token
        /////////////////////////////////////////////
        String jwtToken = denaRequestContext.getRequest().getHeader(HttpHeaders.AUTHORIZATION);
        if (StringUtils.isNotBlank(jwtToken)) {
            denaRequestContext.setToken(jwtToken);
        }


    }

    @Override
    public void postProcess() {
        ThreadLocalManager.remove();
    }
}
