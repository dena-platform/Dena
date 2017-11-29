package com.dena.platform.restapi;

import com.dena.platform.common.utils.DenaObjectUtils;
import com.dena.platform.common.web.JSONMapper;
import com.dena.platform.core.dto.DenaObject;
import com.dena.platform.core.DenaRequestContext;
import com.dena.platform.core.feature.datastore.DenaDataStore;
import com.dena.platform.restapi.dto.DenaResponse;
import com.dena.platform.restapi.dto.ObjectResponse;
import org.slf4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
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
    public ResponseEntity processRestRequest(DenaRequestContext denaRequestContext) {


        // Creating new object(s)
        if (denaRequestContext.isPostRequest()) {
            return handlePostRequest(denaRequestContext);
        }

        // Update object(s)
        if (denaRequestContext.isPutRequest()) {
            return handlePutRequest(denaRequestContext);
        }

        return ResponseEntity.badRequest().build();

    }


    private ResponseEntity handlePostRequest(DenaRequestContext denaRequestContext) {
        String requestBody = denaRequestContext.getRequestBody();
        String typeName = denaRequestContext.getTypeName();
        String appName = denaRequestContext.getAppName();

        List<DenaObject> denaObjects = JSONMapper.createListObjectsFromJSON(requestBody, DenaObject.class);
        denaDataStore.storeObjects(denaObjects, appName, typeName);

        DenaResponse denaResponse = DenaResponse.DenaResponseBuilder.aDenaResponse()
                .withObjectResponseList(createObjectResponse(denaObjects, typeName))
                .withCount(denaObjects.size())
                .withTimestamp(DenaObjectUtils.timeStamp())
                .build();

        return ResponseEntity.ok().body(denaResponse);

    }

    private ResponseEntity handlePutRequest(DenaRequestContext denaRequestContext) {
        String requestBody = denaRequestContext.getRequestBody();
        String typeName = denaRequestContext.getTypeName();
        String appName = denaRequestContext.getAppName();

        List<DenaObject> denaObjects = JSONMapper.createListObjectsFromJSON(requestBody, DenaObject.class);
        denaDataStore.updateObjects(denaObjects, appName, typeName);

        DenaResponse denaResponse = DenaResponse.DenaResponseBuilder.aDenaResponse()
                .withObjectResponseList(createObjectResponse(denaObjects, typeName))
                .withCount(denaObjects.size())
                .withTimestamp(DenaObjectUtils.timeStamp())
                .build();

        return ResponseEntity.ok().body(denaResponse);

    }


    private List<ObjectResponse> createObjectResponse(List<DenaObject> denaObjects, String typeName) {
        List<ObjectResponse> objectResponses = new ArrayList<>();
        denaObjects.forEach(denaObject -> {
            ObjectResponse objectResponse = new ObjectResponse();
            objectResponse.setFields(denaObject.getFields());
            objectResponse.setObjectId(denaObject.getObjectId());
            objectResponse.setURI(DenaObjectUtils.getURIForResource(typeName, objectResponse.getObjectId()));

            objectResponses.add(objectResponse);
        });

        return objectResponses;
    }

}
