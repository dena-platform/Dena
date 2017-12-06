package com.dena.platform.restapi;

import com.dena.platform.common.utils.DenaObjectUtils;
import com.dena.platform.common.web.JSONMapper;
import com.dena.platform.core.dto.DenaObject;
import com.dena.platform.core.DenaRequestContext;
import com.dena.platform.core.feature.datastore.DenaDataStore;
import com.dena.platform.core.feature.datastore.DenaPager;
import com.dena.platform.restapi.dto.DenaResponse;
import com.dena.platform.restapi.dto.ObjectResponse;
import com.dena.platform.restapi.exception.DenaRestException;
import com.dena.platform.restapi.exception.ErrorCodes;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * @author Javad Alimohammadi [<bs.alimohammadi@yahoo.com>]
 */
@Service("denaRestEntityProcessorImpl")
public class RestProcessorImpl implements RestEntityProcessor {
    private final static Logger log = getLogger(RestProcessorImpl.class);

    public final static String TYPE_NAME = "type-name";
    public final static String APP_ID = "app-id";
    public final static String OBJECT_ID = "object-id";


    @Resource(name = "denaMongoDBDataStoreImpl")
    private DenaDataStore denaDataStore;


    @Override
    public ResponseEntity processRestRequest(DenaRequestContext denaRequestContext) {


        // Creating object(s)
        if (denaRequestContext.isPostRequest()) {
            return handlePostRequest(denaRequestContext);
        }

        // Update object(s)
        if (denaRequestContext.isPutRequest()) {
            return handlePutRequest(denaRequestContext);
        }

        // Delete object(s) or relation
        if (denaRequestContext.isDeleteRequest()) {
            return handleDeleteRequest(denaRequestContext);
        }


        return ResponseEntity.badRequest().build();

    }


    private ResponseEntity handlePostRequest(DenaRequestContext denaRequestContext) {
        String requestBody = denaRequestContext.getRequestBody();
        String appTypeName = denaRequestContext.getPathVariable(TYPE_NAME);
        String appName = denaRequestContext.getPathVariable(APP_ID);

        List<DenaObject> denaObjects = JSONMapper.createListObjectsFromJSON(requestBody, DenaObject.class);
        denaDataStore.storeObjects(denaObjects, appName, appTypeName);

        DenaResponse denaResponse = DenaResponse.DenaResponseBuilder.aDenaResponse()
                .withObjectResponseList(createObjectResponse(denaObjects, appTypeName))
                .withCount(denaObjects.size())
                .withTimestamp(DenaObjectUtils.timeStamp())
                .build();

        return ResponseEntity.ok().body(denaResponse);

    }

    private ResponseEntity handlePutRequest(DenaRequestContext denaRequestContext) {
        String requestBody = denaRequestContext.getRequestBody();
        String appTypeName = denaRequestContext.getPathVariable(TYPE_NAME);
        String appName = denaRequestContext.getPathVariable(APP_ID);

        List<DenaObject> denaObjects = JSONMapper.createListObjectsFromJSON(requestBody, DenaObject.class);

        denaDataStore.updateObjects(denaObjects, appName, appTypeName);

        DenaResponse denaResponse = DenaResponse.DenaResponseBuilder.aDenaResponse()
                .withObjectResponseList(createObjectResponse(denaObjects, appTypeName))
                .withCount(denaObjects.size())
                .withTimestamp(DenaObjectUtils.timeStamp())
                .build();

        return ResponseEntity.ok().body(denaResponse);

    }

    @Override
    public ResponseEntity handleDeleteRelation(DenaRequestContext denaRequestContext) {
        String appTypeName = denaRequestContext.getPathVariable(TYPE_NAME);
        String appName = denaRequestContext.getPathVariable(APP_ID);
        String objectId = denaRequestContext.getPathVariable(OBJECT_ID);
        String typeName2 = denaRequestContext.getPathVariable("type-name-2");
        String objectId2 = denaRequestContext.getPathVariable("object-id-2");

        if (StringUtils.isNotBlank(objectId2)) {
            long deleteCount = denaDataStore.deleteRelation(appName, appTypeName, objectId, typeName2, objectId2);
            DenaResponse denaResponse = DenaResponse.DenaResponseBuilder.aDenaResponse()
                    .withCount(deleteCount)
                    .withTimestamp(DenaObjectUtils.timeStamp())
                    .build();

            return ResponseEntity.ok().body(denaResponse);
        } else {
            long deleteCount = denaDataStore.deleteRelation(appName, appTypeName, objectId, typeName2);
            DenaResponse denaResponse = DenaResponse.DenaResponseBuilder.aDenaResponse()
                    .withCount(deleteCount)
                    .withTimestamp(DenaObjectUtils.timeStamp())
                    .build();

            return ResponseEntity.ok().body(denaResponse);
        }

    }

    @Override
    public ResponseEntity handleFindObject(DenaRequestContext denaRequestContext) {
        String typeName = denaRequestContext.getPathVariable(TYPE_NAME);
        String appId = denaRequestContext.getPathVariable(APP_ID);
        String objectId = denaRequestContext.getPathVariable(OBJECT_ID);
        String targetType = denaRequestContext.getPathVariable("target-type");
        List<DenaObject> resultObject;

        if (StringUtils.isBlank(targetType)) {  // read type objects
            DenaObject denaObject = denaDataStore.findObject(appId, typeName, objectId);
            resultObject = Collections.singletonList(denaObject);

        } else { // read relation objects
            DenaObject denaObject = denaDataStore.findObjectRelation(appId, typeName, objectId, targetType, constructPager(denaRequestContext));
            resultObject = Collections.singletonList(denaObject);
        }

        DenaResponse denaResponse = DenaResponse.DenaResponseBuilder.aDenaResponse()
                .withCount(resultObject.size())
                .withObjectResponseList(createObjectResponse(resultObject, typeName))
                .withTimestamp(DenaObjectUtils.timeStamp())
                .build();

        return ResponseEntity.ok().body(denaResponse);
    }


    private ResponseEntity handleDeleteRequest(DenaRequestContext denaRequestContext) {
        String typeName = denaRequestContext.getPathVariable(TYPE_NAME);
        String appId = denaRequestContext.getPathVariable(APP_ID);
        List<String> objectId = Arrays.asList(denaRequestContext.getPathVariable(OBJECT_ID).split(","));

        if (CollectionUtils.isNotEmpty(objectId)) {
            long deleteCount = denaDataStore.deleteObjects(appId, typeName, objectId);
            DenaResponse denaResponse = DenaResponse.DenaResponseBuilder.aDenaResponse()
                    .withCount(deleteCount)
                    .withTimestamp(DenaObjectUtils.timeStamp())
                    .build();

            return ResponseEntity.ok().body(denaResponse);
        }

        throw buildException(HttpServletResponse.SC_BAD_REQUEST, ErrorCodes.ObjectId_INVALID_EXCEPTION);
    }

    private List<ObjectResponse> createObjectResponse(List<DenaObject> denaObjects, String typeName) {
        List<ObjectResponse> objectResponses = new ArrayList<>();
        denaObjects.forEach(denaObject -> {
            ObjectResponse objectResponse = new ObjectResponse();
            objectResponse.setObjectId(denaObject.getObjectId());
            objectResponse.setFields(denaObject.getFields());
            objectResponse.setURI(DenaObjectUtils.getURIForResource(typeName, objectResponse.getObjectId()));
            objectResponse.setRelatedObjects(denaObject.getRelatedObjects());

            objectResponses.add(objectResponse);
        });

        return objectResponses;
    }

    private DenaRestException buildException(final int statusCode, ErrorCodes errorCodes) {
        return DenaRestException.DenaRestExceptionBuilder.aDenaRestException()
                .withStatusCode(statusCode)
                .withErrorCode(errorCodes.getErrorCode())
                .addMessageCode(errorCodes.getMessageCode(), null)
                .build();
    }

    private DenaPager constructPager(DenaRequestContext denaRequestContext) {
        int limit = 0;
        long page = 0;

        if (StringUtils.isNotBlank(denaRequestContext.getParameter(DenaPager.ITEMP_PER_PAGE_PARAMETER))) {
            limit = Integer.valueOf(denaRequestContext.getParameter(DenaPager.ITEMP_PER_PAGE_PARAMETER));
        }

        if (StringUtils.isNotBlank(denaRequestContext.getParameter(DenaPager.PAGE_PARAMETER))) {
            page = Long.valueOf(denaRequestContext.getParameter(DenaPager.PAGE_PARAMETER));
        }

        return DenaPager.DenaPagerBuilder.aDenaPager()
                .withLimit(limit)
                .withPage(page)
                .build();

    }

}
