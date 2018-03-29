package com.dena.platform.restapi;

import com.dena.platform.common.exception.DenaException;
import com.dena.platform.common.exception.ErrorCode;
import com.dena.platform.common.exception.ParameterInvalidException;
import com.dena.platform.common.utils.DenaObjectUtils;
import com.dena.platform.common.web.DenaRequestContext;
import com.dena.platform.common.web.JSONMapper;
import com.dena.platform.core.dto.DenaObject;
import com.dena.platform.core.feature.app.domain.DenaApplication;
import com.dena.platform.core.feature.app.service.DenaApplicationManagement;
import com.dena.platform.core.feature.persistence.DenaDataStore;
import com.dena.platform.core.feature.persistence.DenaPager;
import com.dena.platform.core.feature.persistence.exception.DataStoreException;
import com.dena.platform.core.feature.security.JsonWebTokenGenerator;
import com.dena.platform.core.feature.user.service.DenaUserManagement;
import com.dena.platform.core.feature.user.domain.User;
import com.dena.platform.restapi.dto.response.DenaObjectResponse;
import com.dena.platform.restapi.dto.response.DenaResponse;
import com.dena.platform.restapi.dto.response.DenaResponse.DenaResponseBuilder;
import com.dena.platform.restapi.dto.response.TokenGenResponse;
import com.dena.platform.restapi.exception.DenaRestException;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

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


    @Resource
    private DenaDataStore denaDataStore;

    @Resource
    private DenaUserManagement denaUserManagement;

    @Resource
    private DenaApplicationManagement denaApplicationManagement;

    @Override
    public ResponseEntity handleCreateObject() {
        DenaRequestContext denaRequestContext = DenaRequestContext.getDenaRequestContext();

        String requestBody = denaRequestContext.getRequestBody();
        String appTypeName = denaRequestContext.getPathVariable(TYPE_NAME);
        String appName = denaRequestContext.getPathVariable(APP_ID);

        List<DenaObject> denaObjects;

        try {
            denaObjects = JSONMapper.createListObjectsFromJSON(requestBody, DenaObject.class);
            List<DenaObject> returnObject = denaDataStore.store(appName, appTypeName, denaObjects.toArray(new DenaObject[0]));
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
            List<DenaObject> returnObject = denaDataStore.update(appName, appTypeName, denaObjects.toArray(new DenaObject[0]));
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

        String parentTypeName = denaRequestContext.getPathVariable(TYPE_NAME);
        String appName = denaRequestContext.getPathVariable(APP_ID);
        String parentObjectId = denaRequestContext.getPathVariable(OBJECT_ID);
        String relationName = denaRequestContext.getPathVariable("type-name-2");
        String childObjectId = denaRequestContext.getPathVariable("object-id-2");

        try {
            long deleteCount;

            // delete relation with object
            if (StringUtils.isNotBlank(childObjectId)) {
                deleteCount = denaDataStore.deleteRelation(appName, parentTypeName, parentObjectId, relationName, childObjectId);
            }
            // delete relation with target type
            else {
                deleteCount = denaDataStore.deleteRelation(appName, parentTypeName, parentObjectId, relationName);
            }

            DenaResponse denaResponse = DenaResponseBuilder.aDenaResponse()
                    .withDeleteRelationCount(deleteCount)
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

            long deleteCount = denaDataStore.delete(appId, typeName, objectIds);
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

        String parentTypeName = denaRequestContext.getPathVariable(TYPE_NAME);
        String appId = denaRequestContext.getPathVariable(APP_ID);
        String objectId = denaRequestContext.getPathVariable(OBJECT_ID);
        String relationName = denaRequestContext.getPathVariable("relation-name");
        List<DenaObject> foundDenaObject;
        DenaResponse denaResponse;

        try {
            // find single object by id
            if (StringUtils.isBlank(relationName)) {
                foundDenaObject = denaDataStore.find(appId, parentTypeName, objectId);

                if (CollectionUtils.isNotEmpty(foundDenaObject)) {
                    denaResponse = DenaResponseBuilder.aDenaResponse()
                            .withFoundObjectCount(1)
                            .withObjectResponseList(createObjectResponse(foundDenaObject))
                            .withTimestamp(DenaObjectUtils.timeStamp())
                            .build();

                } else {
                    denaResponse = DenaResponseBuilder.aDenaResponse()
                            .withFoundObjectCount(0)
                            .withTimestamp(DenaObjectUtils.timeStamp())
                            .build();

                }
            }
            // find related objects
            else {
                DenaPager denaPager = constructPager(denaRequestContext);
                foundDenaObject = denaDataStore.findRelatedObject(appId, parentTypeName, objectId, relationName, denaPager);

                denaResponse = DenaResponseBuilder.aDenaResponse()
                        .withFoundObjectCount(foundDenaObject.size())
                        .withObjectResponseList(createObjectResponse(foundDenaObject))
                        .withPage(denaPager.getPage())
                        .withTimestamp(DenaObjectUtils.timeStamp())
                        .build();

            }
            return ResponseEntity.ok().body(denaResponse);
        } catch (DenaException ex) {
            throw DenaRestException.buildException(ex);
        }
    }

    @Override
    public ResponseEntity handleRegisterUser() {
        DenaRequestContext denaRequestContext = DenaRequestContext.getDenaRequestContext();
        String appId = denaRequestContext.getPathVariable(APP_ID);
        String requestBody = denaRequestContext.getRequestBody();
        HashMap<String, Object> requestParameter = JSONMapper.createHashMapFromJSON(requestBody);

        String email = (String) requestParameter.get(User.EMAIL_FIELD_NAME);
        String password = (String) requestParameter.get(User.PASSWORD_FIELD_NAME);
        Map<String, Object> otherFields = new HashMap<>();

        try {

            if (StringUtils.isEmpty(email)) {
                log.warn("email is empty");
                throw new ParameterInvalidException("email field is not set", ErrorCode.EMAIL_FIELD_IS_INVALID);
            } else if (StringUtils.isEmpty(email)) {
                log.warn("password is empty");
                throw new ParameterInvalidException("password field is not set", ErrorCode.PASSWORD_FIELD_IS_INVALID);
            }


            for (Map.Entry<String, Object> entry : requestParameter.entrySet()) {
                if (!entry.getKey().equalsIgnoreCase(User.EMAIL_FIELD_NAME) && !entry.getKey().equalsIgnoreCase(User.PASSWORD_FIELD_NAME)) {
                    otherFields.put(entry.getKey(), entry.getValue());
                }
            }


            User user = User.UserBuilder.anUser()
                    .withEmail(email)
                    .withUnencodedPassword(password)
                    .withOtherFields(otherFields)
                    .build();

            DenaObject registeredUser = denaUserManagement.registerUser(appId, user);

            DenaResponse denaResponse = DenaResponseBuilder.aDenaResponse()
                    .withCreateObjectCount(1)
                    .withObjectResponseList(createObjectResponse(Collections.singletonList(registeredUser)))
                    .withTimestamp(DenaObjectUtils.timeStamp())
                    .build();

            return ResponseEntity.ok().body(denaResponse);
        } catch (DenaException ex) {
            throw DenaRestException.buildException(ex);
        }

    }

    @Override
    public ResponseEntity handleRegisterApplication() {
        DenaRequestContext denaRequestContext = DenaRequestContext.getDenaRequestContext();
        String requestBody = denaRequestContext.getRequestBody();
        HashMap<String, Object> requestParameter = JSONMapper.createHashMapFromJSON(requestBody);

        String applicationName = (String) requestParameter.get(DenaApplication.APP_NAME_FIELD);
        String creatorId = (String) requestParameter.get(DenaApplication.CREATOR_ID_FIELD);
        try {
            if (StringUtils.isEmpty(applicationName)) {
                log.warn("application name field is empty");
                throw new ParameterInvalidException("application_name is not set", ErrorCode.APP_NAME_FIELD_IS_INVALID);
            }

            if (StringUtils.isEmpty(creatorId)) {
                log.warn("creator_id name field is empty");
                throw new ParameterInvalidException("creator_id is not set", ErrorCode.CREATOR_FIELD_IS_INVALID);
            }


            DenaApplication denaApplication = new DenaApplication();
            denaApplication.setApplicationName(applicationName);
            denaApplication.setCreatorId(creatorId);

            DenaObject registeredApplication = denaApplicationManagement.registerApplication(denaApplication);

            DenaResponse denaResponse = DenaResponseBuilder.aDenaResponse()
                    .withCreateObjectCount(1)
                    .withObjectResponseList(createObjectResponse(Collections.singletonList(registeredApplication)))
                    .withTimestamp(DenaObjectUtils.timeStamp())
                    .build();


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

        if (StringUtils.isNotBlank(denaRequestContext.getParameter(DenaPager.ITEM_PER_PAGE_PARAMETER))) {
            limit = Integer.valueOf(denaRequestContext.getParameter(DenaPager.ITEM_PER_PAGE_PARAMETER));
        }

        if (StringUtils.isNotBlank(denaRequestContext.getParameter(DenaPager.PAGE_PARAMETER))) {
            page = Long.valueOf(denaRequestContext.getParameter(DenaPager.PAGE_PARAMETER));
        }

        return DenaPager.DenaPagerBuilder.aDenaPager()
                .withLimit(limit)
                .withPage(page)
                .build();

    }

    @Resource
    private JsonWebTokenGenerator jwtGenerator;

    //// TODO: remove form hear
    @Override
    public ResponseEntity login() {
        DenaRequestContext denaRequestContext = DenaRequestContext.getDenaRequestContext();

        String appId = denaRequestContext.getPathVariable(APP_ID);
        String requestBody = denaRequestContext.getRequestBody();
        User user = JSONMapper.createObjectFromJSON(requestBody, User.class);

        String token = jwtGenerator.generate(appId, user);

        TokenGenResponse response = new TokenGenResponse();
        response.setToken(token);
        return ResponseEntity.ok().body(response);
    }

}
