package com.dena.platform.restapi;

import com.dena.platform.common.exception.DenaException;
import com.dena.platform.common.exception.ErrorCode;
import com.dena.platform.common.utils.DenaObjectUtils;
import com.dena.platform.common.web.JSONMapper;
import com.dena.platform.core.DenaRequestContext;
import com.dena.platform.core.dto.DenaObject;
import com.dena.platform.core.feature.persistence.DenaDataStore;
import com.dena.platform.core.feature.persistence.DenaPager;
import com.dena.platform.core.feature.persistence.exception.DataStoreException;
import com.dena.platform.restapi.dto.response.DenaResponse;
import com.dena.platform.restapi.dto.response.DenaResponse.DenaResponseBuilder;
import com.dena.platform.restapi.dto.response.ObjectResponse;
import com.dena.platform.restapi.exception.DenaRestException;
import com.dena.platform.restapi.exception.DenaRestException.DenaRestExceptionBuilder;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

import static javax.servlet.http.HttpServletResponse.SC_BAD_REQUEST;
import static org.slf4j.LoggerFactory.getLogger;

/**
 * @author Javad Alimohammadi [<bs.alimohammadi@yahoo.com>]
 */
@Service("denaRestEntityProcessorImpl")
public class RestProcessorImpl implements DenaRestProcessor {
    private final static Logger log = getLogger(RestProcessorImpl.class);

    public final static String TYPE_NAME = "type-name";
    public final static String APP_ID = "app-id";
    public final static String OBJECT_ID = "object-id";


    @Resource(name = "denaMongoDBDataStoreImpl")
    private DenaDataStore denaDataStore;

    // Creating object(s)
    @Override
    public ResponseEntity handleCreateObject(DenaRequestContext denaRequestContext) {
        String requestBody = denaRequestContext.getRequestBody();
        String appTypeName = denaRequestContext.getPathVariable(TYPE_NAME);
        String appName = denaRequestContext.getPathVariable(APP_ID);

        List<DenaObject> denaObjects;
        try {
            denaObjects = JSONMapper.createListObjectsFromJSON(requestBody, DenaObject.class);
            denaDataStore.storeObjects(denaObjects, appName, appTypeName);
        } catch (DenaException ex) {
            throw buildBadRequestException(ex.getErrorCode());
        }

        DenaResponse denaResponse = DenaResponseBuilder.aDenaResponse()
                .withObjectResponseList(createObjectResponse(denaObjects, appTypeName))
                .withCount(denaObjects.size())
                .withTimestamp(DenaObjectUtils.timeStamp())
                .build();

        return ResponseEntity.ok().body(denaResponse);
    }

    @Override
    public ResponseEntity handleUpdateObject(DenaRequestContext denaRequestContext) {
        String requestBody = denaRequestContext.getRequestBody();
        String appTypeName = denaRequestContext.getPathVariable(TYPE_NAME);
        String appName = denaRequestContext.getPathVariable(APP_ID);

        List<DenaObject> denaObjects = JSONMapper.createListObjectsFromJSON(requestBody, DenaObject.class);

        try {
            denaDataStore.updateObjects(denaObjects, appName, appTypeName);
        } catch (DataStoreException ex) {
            throw buildBadRequestException(ex.getErrorCode());
        }

        DenaResponse denaResponse = DenaResponseBuilder.aDenaResponse()
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

        try {
            // delete relation with single object
            if (StringUtils.isNotBlank(objectId2)) {
                long deleteCount = denaDataStore.deleteRelation(appName, appTypeName, objectId, typeName2, objectId2);
                DenaResponse denaResponse = DenaResponseBuilder.aDenaResponse()
                        .withCount(deleteCount)
                        .withTimestamp(DenaObjectUtils.timeStamp())
                        .build();

                return ResponseEntity.ok().body(denaResponse);
            }
            // delete relation
            else {
                long deleteCount = denaDataStore.deleteRelation(appName, appTypeName, objectId, typeName2);
                DenaResponse denaResponse = DenaResponseBuilder.aDenaResponse()
                        .withCount(deleteCount)
                        .withTimestamp(DenaObjectUtils.timeStamp())
                        .build();

                return ResponseEntity.ok().body(denaResponse);
            }
        } catch (DenaException ex) {
            throw buildBadRequestException(ex.getErrorCode());
        }

    }

    @Override
    public ResponseEntity handleDeleteObject(DenaRequestContext denaRequestContext) {
        String typeName = denaRequestContext.getPathVariable(TYPE_NAME);
        String appId = denaRequestContext.getPathVariable(APP_ID);
        List<String> objectId = Arrays.asList(denaRequestContext.getPathVariable(OBJECT_ID).split(","));

        if (CollectionUtils.isEmpty(objectId)) {
            throw buildException(SC_BAD_REQUEST, ErrorCode.ObjectId_INVALID_EXCEPTION);
        }

        try {
            long deleteCount = denaDataStore.deleteObjects(appId, typeName, objectId);
            DenaResponse denaResponse = DenaResponseBuilder.aDenaResponse()
                    .withCount(deleteCount)
                    .withTimestamp(DenaObjectUtils.timeStamp())
                    .build();
            return ResponseEntity.ok().body(denaResponse);
        } catch (DenaException ex) {
            throw buildException(SC_BAD_REQUEST, ex.getErrorCode());
        }

    }

    @Override
    public ResponseEntity handleFindObject(DenaRequestContext denaRequestContext) {
        String typeName = denaRequestContext.getPathVariable(TYPE_NAME);
        String appId = denaRequestContext.getPathVariable(APP_ID);
        String objectId = denaRequestContext.getPathVariable(OBJECT_ID);
        String targetType = denaRequestContext.getPathVariable("target-type");
        List<DenaObject> resultObject;
        DenaResponse denaResponse;

        try {
            // find single object by id
            if (StringUtils.isBlank(targetType)) {
                Optional<DenaObject> denaObject = denaDataStore.findObject(appId, typeName, objectId);
                if (denaObject.isPresent()) {
                    resultObject = Collections.singletonList(denaObject.get());
                    denaResponse = makeDenaResponse(1L, createObjectResponse(resultObject, typeName));
                } else {
                    denaResponse = makeDenaResponse(0L, null);
                }
            }
            // find relation objects
            else {
                DenaPager denaPager = constructPager(denaRequestContext);
                DenaObject denaObject = denaDataStore.findObjectRelation(appId, typeName, objectId, targetType, denaPager);
                resultObject = Collections.singletonList(denaObject);

                denaResponse = DenaResponseBuilder.aDenaResponse()
                        .withCount(resultObject.size())
                        .withObjectResponseList(createObjectResponse(resultObject, typeName))
                        .withTotalCount(denaPager.getCount())
                        .withPage(denaPager.getPage())
                        .withTimestamp(DenaObjectUtils.timeStamp())
                        .build();

            }
            return ResponseEntity.ok().body(denaResponse);
        } catch (DenaException ex) {
            throw buildBadRequestException(ex.getErrorCode());
        }
    }


    private List<ObjectResponse> createObjectResponse(List<DenaObject> denaObjects, String typeName) {
        List<ObjectResponse> objectResponses = new ArrayList<>();
        denaObjects.forEach(denaObject -> {
            ObjectResponse objectResponse = new ObjectResponse();
            objectResponse.setObjectId(denaObject.getObjectId());
            objectResponse.setFields(denaObject.getFields());
            objectResponse.setObjectURI(DenaObjectUtils.getURIForResource(typeName, objectResponse.getObjectId()));
            objectResponse.setRelatedObjects(denaObject.getRelatedObjects());

            objectResponses.add(objectResponse);
        });

        return objectResponses;
    }

    private DenaRestException buildException(final int statusCode, ErrorCode errorCode) {
        return DenaRestExceptionBuilder.aDenaRestException()
                .withStatusCode(statusCode)
                .withErrorCode(errorCode.getErrorCode())
                .addMessageCode(errorCode.getMessageCode(), null)
                .build();
    }

    private DenaRestException buildBadRequestException(ErrorCode errorCode) {
        return buildException(SC_BAD_REQUEST, errorCode);
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

    private DenaResponse makeDenaResponse(long count, List<ObjectResponse> objectResponseList) {
        return DenaResponseBuilder.aDenaResponse()
                .withCount(count)
                .withObjectResponseList(objectResponseList)
                .withTimestamp(DenaObjectUtils.timeStamp())
                .build();

    }

}
