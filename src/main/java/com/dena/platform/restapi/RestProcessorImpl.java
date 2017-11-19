package com.dena.platform.restapi;

import com.dena.platform.common.exception.InvalidFormatException;
import com.dena.platform.common.web.JSONMapper;
import com.dena.platform.core.DenaObject;
import com.dena.platform.core.DenaRequestContext;
import com.dena.platform.core.feature.datastore.DenaDataStore;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * @author Javad Alimohammadi [<bs.alimohammadi@yahoo.com>]
 */
@Service("denaRestEntityProcessorImpl")
public class RestProcessorImpl implements RestEntityProcessor {
    private final static Logger log = getLogger(RestProcessorImpl.class);

    @Resource(name = "denaMongoDBDataStoreImpl")
    private DenaDataStore denaDataStore;


    @Override
    public void processRestRequest(DenaRequestContext denaRequestContext) {

        // Creating new object
        if (denaRequestContext.isPostRequest()) {
            String requestBody = denaRequestContext.getRequestBody();
            List<DenaObject> denaObjects = JSONMapper.createListObjectsFromJSON(requestBody, DenaObject.class);

            denaObjects.forEach(denaObject -> {
                denaObject.setTypeName(denaRequestContext.getTypeName());
                denaObject.setAppName(denaRequestContext.getAppName());
            });

            denaDataStore.storeObjects(denaObjects, denaRequestContext.getAppName(), denaRequestContext.getTypeName());
        }

    }

}
