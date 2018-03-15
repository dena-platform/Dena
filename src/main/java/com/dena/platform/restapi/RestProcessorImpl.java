package com.dena.platform.restapi;

import com.dena.platform.common.exception.DenaException;
import com.dena.platform.common.exception.ErrorCode;
import com.dena.platform.common.exception.ParameterInvalidException;
import com.dena.platform.common.utils.DenaObjectUtils;
import com.dena.platform.common.web.DenaRequestContext;
import com.dena.platform.common.web.JSONMapper;
import com.dena.platform.core.dto.DenaObject;
import com.dena.platform.core.feature.persistence.DenaDataStore;
import com.dena.platform.core.feature.persistence.DenaPager;
import com.dena.platform.core.feature.persistence.exception.DataStoreException;
import com.dena.platform.restapi.dto.response.DenaObjectResponse;
import com.dena.platform.restapi.dto.response.DenaResponse;
import com.dena.platform.restapi.dto.response.DenaResponse.DenaResponseBuilder;
import com.dena.platform.restapi.exception.DenaRestException;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
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
public class RestProcessorImpl implements DenaRestProcessor {
    private final static Logger log = getLogger(RestProcessorImpl.class);

    public final static String TYPE_NAME = "type-name";
    public final static String APP_ID = "app-id";
    public final static String OBJECT_ID = "object-id";


    @Resource(name = "denaMongoDBDataStoreImpl")
    private DenaDataStore denaDataStore;

    @Override
    public ResponseEntity handleCreateObject() {
        DenaRequestContext denaRequestContext = DenaRequestContext.getDenaRequestContext();

        String requestBody = denaRequestContext.getRequestBody();
        String appTypeName = denaRequestContext.getPathVariable(TYPE_NAME);
        String appName = denaRequestContext.getPathVariable(APP_ID);

        List<DenaObject> denaObjects;

        try {
            denaObjects = JSONMapper.createListObjectsFromJSON(requestBody, DenaObject.class);
            List<DenaObject> returnObject = denaDataStore.storeObjects(appName, appTypeName, denaObjects.toArray(new DenaObject[0]));
            DenaResponse denaResponse = DenaResponseBuilder.aDenaResponse()
                    .withObjectResponseList(createObjectResponse(returnObject))
                    .withCreateObjectCount(returnObject.size())
                    .withTimestamp(DenaObjectUtils.timeStamp())
                    .build();
            return ResponseEntity.ok().body(denaResponse);
        } catch (DenaException ex) {
            throw DenaRestException.buildException(ex);
        }
    }

    @Override
    public ResponseEntity handleUpdateObject() {
        DenaRequestContext denaRequestContext = DenaRequestContext.getDenaRequestContext();
        String requestBody = denaRequestContext.getRequestBody();
        String appTypeName = denaRequestContext.getPathVariable(TYPE_NAME);
        String appName = denaRequestContext.getPathVariable(APP_ID);

        List<DenaObject> denaObjects = JSONMapper.createListObjectsFromJSON(requestBody, DenaObject.class);

        try {
            List<DenaObject> returnObject = denaDataStore.updateObjects(appName, appTypeName, denaObjects.toArray(new DenaObject[0]));
            DenaResponse response = DenaResponseBuilder.aDenaResponse()
                    .withObjectResponseList(createObjectResponse(returnObject))
                    .withUpdateObjectCount(returnObject.size())
                    .withTimestamp(DenaObjectUtils.timeStamp())
                    .build();
            return ResponseEntity.ok().body(response);
        } catch (DataStoreException ex) {
            throw DenaRestException.buildException(ex);
        }
    }

    @Override
    public ResponseEntity handleDeleteRelation() {
        DenaRequestContext denaRequestContext = DenaRequestContext.getDenaRequestContext();

        String appTypeName = denaRequestContext.getPathVariable(TYPE_NAME);
        String appName = denaRequestContext.getPathVariable(APP_ID);
        String objectId = denaRequestContext.getPathVariable(OBJECT_ID);
        String typeName2 = denaRequestContext.getPathVariable("type-name-2");
        String objectId2 = denaRequestContext.getPathVariable("object-id-2");

        try {
            long deleteCount;

            // delete relation with object
            if (StringUtils.isNotBlank(objectId2)) {
                deleteCount = denaDataStore.deleteRelation(appName, appTypeName, objectId, typeName2, objectId2);
            }
            // delete relation with target type
            else {
                deleteCount = denaDataStore.deleteRelation(appName, appTypeName, objectId, typeName2);
            }

            DenaResponse denaResponse = DenaResponseBuilder.aDenaResponse()
                    .withCreateObjectCount(deleteCount)
                    .withTimestamp(DenaObjectUtils.timeStamp())
                    .build();
            return ResponseEntity.ok().body(denaResponse);
        } catch (DenaException ex) {
            throw DenaRestException.buildException(ex);
        }

    }

    @Override
    public ResponseEntity handleDeleteObject() {
        DenaRequestContext denaRequestContext = DenaRequestContext.getDenaRequestContext();

        String appId = denaRequestContext.getPathVariable(APP_ID);
        String typeName = denaRequestContext.getPathVariable(TYPE_NAME);
        String[] objectIds = denaRequestContext.getPathVariable(OBJECT_ID).split(",");


        try {
            if (ArrayUtils.isEmpty(objectIds)) {
                log.warn("object id is empty");
                throw new ParameterInvalidException("object id is empty", ErrorCode.INVALID_REQUEST);
            }
            if (StringUtils.isEmpty(appId)) {
                log.warn("app id is empty");
                throw new ParameterInvalidException("app id is empty", ErrorCode.INVALID_REQUEST);
            }
            if (StringUtils.isEmpty(typeName)) {
                log.warn("type name is empty");
                throw new ParameterInvalidException("type name is empty", ErrorCode.INVALID_REQUEST);
            }

            long deleteCount = denaDataStore.deleteObjects(appId, typeName, objectIds);
            DenaResponse denaResponse = DenaResponseBuilder.aDenaResponse()
                    .withDeleteObjectCount(deleteCount)
                    .withTimestamp(DenaObjectUtils.timeStamp())
                    .build();
            return ResponseEntity.ok().body(denaResponse);
        } catch (DenaException ex) {
            throw DenaRestException.buildException(ex);
        }

    }

    @Override
    public ResponseEntity handleFindObject() {
        DenaRequestContext denaRequestContext = DenaRequestContext.getDenaRequestContext();

        String typeName = denaRequestContext.getPathVariable(TYPE_NAME);
        String appId = denaRequestContext.getPathVariable(APP_ID);
        String objectId = denaRequestContext.getPathVariable(OBJECT_ID);
        String targetType = denaRequestContext.getPathVariable("target-type");
        List<DenaObject> foundDenaObject;
        DenaResponse denaResponse;

        try {
            // find single object by id
            if (StringUtils.isBlank(targetType)) {
                foundDenaObject = denaDataStore.findObject(appId, typeName, objectId);

                if (CollectionUtils.isNotEmpty(foundDenaObject)) {
                    denaResponse = makeDenaResponse(1L, createObjectResponse(foundDenaObject));
                } else {
                    denaResponse = makeDenaResponse(0L, null);
                }
            }
            // find relation objects
            else {
                DenaPager denaPager = constructPager(denaRequestContext);
                foundDenaObject = denaDataStore.findObjectRelation(appId, typeName, objectId, targetType, denaPager);

                denaResponse = DenaResponseBuilder.aDenaResponse()
                        .withCreateObjectCount(foundDenaObject.size())
                        .withObjectResponseList(createObjectResponse(foundDenaObject))
                        .withCreateObjectCount(denaPager.getCount())
                        .withPage(denaPager.getPage())
                        .withTimestamp(DenaObjectUtils.timeStamp())
                        .build();

            }
            return ResponseEntity.ok().body(denaResponse);
        } catch (DenaException ex) {
            throw DenaRestException.buildException(ex);
        }
    }


    private List<DenaObjectResponse> createObjectResponse(List<DenaObject> denaObjects) {
        List<DenaObjectResponse> denaObjectResponses = new ArrayList<>();
        denaObjects.forEach(denaObject -> {
            DenaObjectResponse objectResponse = new DenaObjectResponse();
            objectResponse.setObjectId(denaObject.getObjectId());
            objectResponse.setCreateTime(denaObject.getCreateTime());
            objectResponse.setUpdateTime(denaObject.getUpdateTime());
            objectResponse.setObjectURI(denaObject.getObjectURI());
            objectResponse.setFields(denaObject.getOtherFields());
            denaObjectResponses.add(objectResponse);
        });

        return denaObjectResponses;
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

    private DenaResponse makeDenaResponse(long count, List<DenaObjectResponse> denaObjectResponseList) {
        return DenaResponseBuilder.aDenaResponse()
                .withCreateObjectCount(count)
                .withTimestamp(DenaObjectUtils.timeStamp())
                .withObjectResponseList(denaObjectResponseList)
                .build();

    }


}
