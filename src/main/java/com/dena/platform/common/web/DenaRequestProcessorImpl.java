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

        /////////////////////////////////////////////
        //            Set app id
        /////////////////////////////////////////////
        String path = request.getServletPath();
        int indexOfVersion = path.indexOf("v");
        int beginAppNameIndex = path.indexOf("/", indexOfVersion) + 1;
        int endAppNameIndex = path.indexOf("/", beginAppNameIndex) - 1;
        String appId = path.substring(beginAppNameIndex, endAppNameIndex + 1);
        denaRequestContext.setAppId(appId);


    }

    @Override
    public void postProcess() {
        ThreadLocalManager.remove();
    }
}
