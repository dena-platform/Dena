package com.dena.platform.restapi;

import com.dena.platform.common.config.DenaConfigReader;
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
import com.dena.platform.core.feature.persistence.SchemaManager;
import com.dena.platform.core.feature.persistence.exception.DataStoreException;
import com.dena.platform.core.feature.search.Search;
import com.dena.platform.core.feature.security.service.DenaSecurityService;
import com.dena.platform.core.feature.user.domain.User;
import com.dena.platform.core.feature.user.service.DenaUserManagement;
import com.dena.platform.restapi.dto.response.DenaObjectResponse;
import com.dena.platform.restapi.dto.response.DenaResponse;
import com.dena.platform.restapi.dto.response.DenaResponse.DenaResponseBuilder;
import com.dena.platform.restapi.exception.DenaRestException;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * @author Javad Alimohammadi [<bs.alimohammadi@gmail.com>]
 */
@Service("denaRestEntityProcessorImpl")
public class RestProcessorImpl implements DenaRestProcessor {
    private final static Logger log = getLogger(RestProcessorImpl.class);

    public final static String TABLE_NAME = "table-name";
    public final static String OBJECT_ID = "object-id";
    public final static String RELATION_NAME = "relation-name";
    public final static String QUERY_STRING = "query-string";
    public final static String USER_NAME = "user-name";
    public final static String RELOAD_RELATION_PARAMETER = "loadRelation";

    @Resource
    private Search search;

    @Resource
    private DenaDataStore denaDataStore;

    @Resource
    private DenaUserManagement denaUserManagement;

    @Resource
    private DenaApplicationManagement denaApplicationManagement;

    @Resource
    private SchemaManager schemaManager;

    @Resource
    private DenaSecurityService denaSecurityService;


    @Override
    public ResponseEntity handleCreateObject() {
        DenaRequestContext denaRequestContext = DenaRequestContext.getDenaRequestContext();

        String requestBody = denaRequestContext.getRequestBody();
        String tableName = denaRequestContext.getPathVariable(TABLE_NAME);
        String appId = denaRequestContext.getAppId();
        boolean loadRelation = Boolean.parseBoolean(denaRequestContext.getParameter(RELOAD_RELATION_PARAMETER));

        List<DenaObject> denaObjects;

        try {
            denaObjects = JSONMapper.createListObjectsFromJSON(requestBody, DenaObject.class);
            List<DenaObject> returnObject = denaDataStore.store(appId, tableName, denaObjects.toArray(new DenaObject[0]));

            String userName = denaObjects.get(0).getActorUsername();//TODO
            User user = denaUserManagement.findUserByEmailAddress(appId, userName);

            //search.index(appName, appTypeName, user, returnObject.toArray(new DenaObject[0]));

            DenaResponse denaResponse = DenaResponseBuilder.aDenaResponse()
                    .withHttpStatusCode(HttpStatus.OK.value())
                    .withDenaObjectResponseList(createObjectResponse(returnObject, loadRelation))
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
        String appTypeName = denaRequestContext.getPathVariable(TABLE_NAME);
        String appName = denaRequestContext.getAppId();
        boolean loadRelation = Boolean.parseBoolean(denaRequestContext.getParameter(RELOAD_RELATION_PARAMETER));


        List<DenaObject> denaObjects = JSONMapper.createListObjectsFromJSON(requestBody, DenaObject.class);
        String userName = denaObjects.get(0).getActorUsername();//TODO
        User user = denaUserManagement.findUserByEmailAddress(appName, userName);

        try {
            List<DenaObject> returnObject;
            if (denaRequestContext.isPatchRequest()) { // is merge update request?
                returnObject = denaDataStore.mergeUpdate(appName, appTypeName, denaObjects.toArray(new DenaObject[0]));
            } else {
                returnObject = denaDataStore.replaceUpdate(appName, appTypeName, denaObjects.toArray(new DenaObject[0]));
            }
            // search.updateIndex(appName, appTypeName, user, returnObject.toArray(new DenaObject[0])); // todo : handle user name when security is disabled

            DenaResponse response = DenaResponseBuilder.aDenaResponse()
                    .withDenaObjectResponseList(createObjectResponse(returnObject, loadRelation))
                    .withUpdateObjectCount(returnObject.size())
                    .withTimestamp(DenaObjectUtils.timeStamp())
                    .withHttpStatusCode(HttpStatus.OK.value())
                    .build();
            return ResponseEntity.ok().body(response);
        } catch (DataStoreException ex) {
            throw DenaRestException.buildException(ex);
        }
    }

    @Override
    public ResponseEntity handleDeleteRelation() {
        DenaRequestContext denaRequestContext = DenaRequestContext.getDenaRequestContext();

        String appId = denaRequestContext.getAppId();
        String parentTypeName = denaRequestContext.getPathVariable(TABLE_NAME);
        String parentObjectId = denaRequestContext.getPathVariable(OBJECT_ID);
        String relationName = denaRequestContext.getPathVariable("table-name-2");
        String childObjectId = denaRequestContext.getPathVariable("object-id-2");

        try {
            long deleteCount;

            // delete relation with object
            if (StringUtils.isNotBlank(childObjectId)) {
                deleteCount = denaDataStore.deleteRelation(appId, parentTypeName, parentObjectId, relationName, childObjectId);
            }
            // delete relation with target type
            else {
                deleteCount = denaDataStore.deleteRelation(appId, parentTypeName, parentObjectId, relationName);
            }

            DenaResponse denaResponse = DenaResponseBuilder.aDenaResponse()
                    .withDeleteRelationCount(deleteCount)
                    .withTimestamp(DenaObjectUtils.timeStamp())
                    .withHttpStatusCode(HttpStatus.OK.value())
                    .build();
            return ResponseEntity.ok().body(denaResponse);
        } catch (DenaException ex) {
            throw DenaRestException.buildException(ex);
        }

    }

    @Override
    public ResponseEntity handleDeleteObject() {
        DenaRequestContext denaRequestContext = DenaRequestContext.getDenaRequestContext();

        String appId = denaRequestContext.getAppId();
        String tableName = denaRequestContext.getPathVariable(TABLE_NAME);
        String[] objectIds = denaRequestContext.getPathVariable(OBJECT_ID).split(",");
//        String userName = denaRequestContext.getPathVariable(USER_NAME);

//        User user = denaUserManagement.findUserByEmailAddress(appId, userName);


        try {
            if (ArrayUtils.isEmpty(objectIds)) {
                log.warn("object id is empty");
                throw new ParameterInvalidException("object id is empty", ErrorCode.INVALID_REQUEST);
            }
            if (StringUtils.isEmpty(appId)) {
                log.warn("app id is empty");
                throw new ParameterInvalidException("app id is empty", ErrorCode.INVALID_REQUEST);
            }
            if (StringUtils.isEmpty(tableName)) {
                log.warn("type name is empty");
                throw new ParameterInvalidException("type name is empty", ErrorCode.INVALID_REQUEST);
            }

//            search.deleteIndexByIds(appId, user, objectIds);

            long deleteCount = denaDataStore.delete(appId, tableName, objectIds);
            DenaResponse denaResponse = DenaResponseBuilder.aDenaResponse()
                    .withDeleteObjectCount(deleteCount)
                    .withTimestamp(DenaObjectUtils.timeStamp())
                    .withHttpStatusCode(HttpStatus.OK.value())
                    .build();
            return ResponseEntity.ok().body(denaResponse);
        } catch (DenaException ex) {
            throw DenaRestException.buildException(ex);
        }

    }

    @Override
    public ResponseEntity handleFindObject() {
        DenaRequestContext denaRequestContext = DenaRequestContext.getDenaRequestContext();
        boolean loadRelation = Boolean.parseBoolean(denaRequestContext.getParameter(RELOAD_RELATION_PARAMETER));

        String appId = denaRequestContext.getAppId();
        String parentTableName = denaRequestContext.getPathVariable(TABLE_NAME);
        String objectId = denaRequestContext.getPathVariable(OBJECT_ID);
        String relationName = denaRequestContext.getPathVariable(RELATION_NAME);
        List<DenaObject> foundDenaObject;
        DenaResponse denaResponse;

        try {

            if (StringUtils.isBlank(relationName)) {

                if (StringUtils.isBlank(objectId)) {
                    // find all object in table
                    foundDenaObject = denaDataStore.findAll(appId, parentTableName, constructPager());
                } else {
                    // find single object by id
                    foundDenaObject = denaDataStore.find(appId, parentTableName, objectId);
                }

            }
            // find related objects
            else {
                foundDenaObject = denaDataStore.findRelatedObject(appId, parentTableName, objectId,
                        relationName, constructPager());
            }

            if (CollectionUtils.isNotEmpty(foundDenaObject)) {
                denaResponse = DenaResponseBuilder.aDenaResponse()
                        .withFoundObjectCount(foundDenaObject.size())
                        .withDenaObjectResponseList(createObjectResponse(foundDenaObject, loadRelation))
                        .withTimestamp(DenaObjectUtils.timeStamp())
                        .withHttpStatusCode(HttpStatus.OK.value())
                        .build();

            } else {
                denaResponse = DenaResponseBuilder.aDenaResponse()
                        .withFoundObjectCount(foundDenaObject.size())
                        .withTimestamp(DenaObjectUtils.timeStamp())
                        .withHttpStatusCode(HttpStatus.OK.value())
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
        String appId = denaRequestContext.getAppId();
        String requestBody = denaRequestContext.getRequestBody();
        HashMap<String, Object> requestParameter = JSONMapper.createHashMapFromJSON(requestBody);

        String email = (String) requestParameter.get(User.EMAIL_FIELD_NAME);
        String password = (String) requestParameter.get(User.PASSWORD_FIELD_NAME);
        Map<String, Object> otherFields = new HashMap<>();

        try {

            if (StringUtils.isEmpty(email)) {
                log.warn("Email field is empty");
                throw new ParameterInvalidException("email field is not set", ErrorCode.EMAIL_FIELD_IS_INVALID);
            } else if (StringUtils.isEmpty(email)) {
                log.warn("Password field is empty");
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
                    .withCreatedUserCount(1)
                    .withDenaObjectResponseList(createObjectResponse(Collections.singletonList(registeredUser)))
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
                    .withDenaObjectResponseList(createObjectResponse(Collections.singletonList(registeredApplication)))
                    .withTimestamp(DenaObjectUtils.timeStamp())
                    .withHttpStatusCode(200)
                    .build();


            return ResponseEntity.ok().body(denaResponse);
        } catch (DenaException ex) {
            throw DenaRestException.buildException(ex);
        }

    }

    @Override
    public ResponseEntity handleCreateSchema() {

        DenaRequestContext denaRequestContext = DenaRequestContext.getDenaRequestContext();
        String appId = denaRequestContext.getAppId();
        String tableName = denaRequestContext.getPathVariable(TABLE_NAME);

        try {
            int schemaCount = schemaManager.createSchema(appId, tableName);
            DenaResponse denaResponse = DenaResponseBuilder.aDenaResponse()
                    .withHttpStatusCode(200)
                    .withCreateTableCount(schemaCount)
                    .withTimestamp(DenaObjectUtils.timeStamp())
                    .build();
            return ResponseEntity.ok().body(denaResponse);
        } catch (DenaException ex) {
            throw DenaRestException.buildException(ex);
        }

    }

    @Override
    public ResponseEntity handleGetAllSchema() {
        String appId = DenaRequestContext.getDenaRequestContext().getAppId();

        try {
            List<DenaObject> denaObjectList = schemaManager.findAllSchema(appId);
            DenaResponse denaResponse = DenaResponseBuilder.aDenaResponse()
                    .withHttpStatusCode(200)
                    .withFoundTableCount(denaObjectList.size())
                    .withDenaObjectResponseList(createObjectResponse(denaObjectList))
                    .withTimestamp(DenaObjectUtils.timeStamp())
                    .build();
            return ResponseEntity.ok().body(denaResponse);

        } catch (DenaException ex) {
            throw DenaRestException.buildException(ex);
        }

    }

    @Override
    public ResponseEntity handleDeleteSchema() {
        DenaRequestContext denaRequestContext = DenaRequestContext.getDenaRequestContext();
        String appId = denaRequestContext.getAppId();
        String tableName = denaRequestContext.getPathVariable(TABLE_NAME);

        try {
            int deleteSchemaCount = schemaManager.deleteSchema(appId, tableName);
            DenaResponse denaResponse = DenaResponseBuilder.aDenaResponse()
                    .withHttpStatusCode(200)
                    .withDeleteTableCount(deleteSchemaCount)
                    .withTimestamp(DenaObjectUtils.timeStamp())
                    .build();
            return ResponseEntity.ok().body(denaResponse);

        } catch (DenaException ex) {
            throw DenaRestException.buildException(ex);
        }

    }

    @Override
    public ResponseEntity handleSearch() {
        DenaRequestContext denaRequestContext = DenaRequestContext.getDenaRequestContext();

        String appId = denaRequestContext.getAppId();
        String queryString = denaRequestContext.getPathVariable(QUERY_STRING);
        String userId = denaRequestContext.getPathVariable(USER_NAME);
        String appTypeName = denaRequestContext.getPathVariable(TABLE_NAME);
        List<DenaObject> foundDenaObject;
        DenaResponse denaResponse;

        try {
            User user = denaUserManagement.findUserByEmailAddress(appId, userId);
            foundDenaObject = search.query(appId, appTypeName, user, queryString, constructPager());

            if (CollectionUtils.isNotEmpty(foundDenaObject)) {
                denaResponse = DenaResponseBuilder.aDenaResponse()
                        .withFoundObjectCount(foundDenaObject.size())
                        .withDenaObjectResponseList(createObjectResponse(foundDenaObject))
                        .withTimestamp(DenaObjectUtils.timeStamp())
                        .build();

            } else {
                denaResponse = DenaResponseBuilder.aDenaResponse()
                        .withFoundObjectCount(foundDenaObject.size())
                        .withTimestamp(DenaObjectUtils.timeStamp())
                        .build();

            }


            return ResponseEntity.ok().body(denaResponse);
        } catch (DenaException ex) {
            throw DenaRestException.buildException(ex);
        }
    }

    @Override
    public ResponseEntity handleLoginUser() {
        String appId = DenaRequestContext.getDenaRequestContext().getAppId();
        String requestBody = DenaRequestContext.getDenaRequestContext().getRequestBody();
        HashMap<String, Object> parameters = JSONMapper.createHashMapFromJSON(requestBody);

        String userName = (String) parameters.get(User.EMAIL_FIELD_NAME);
        String password = (String) parameters.get(User.PASSWORD_FIELD_NAME);
        DenaResponse denaResponse;

        try {

            if (StringUtils.isEmpty(userName)) {
                log.warn("user name field is empty");
                throw new ParameterInvalidException("user_name is not set", ErrorCode.EMAIL_FIELD_IS_INVALID);
            }

            if (StringUtils.isEmpty(password)) {
                log.warn("password field is empty");
                throw new ParameterInvalidException("password is not set", ErrorCode.PASSWORD_FIELD_IS_INVALID);
            }

            DenaObject authenticateUser = denaSecurityService.authenticateUser(appId, userName, password);
            denaResponse = DenaResponseBuilder.aDenaResponse()
                    .withFoundObjectCount(1)
                    .withDenaObjectResponseList(createObjectResponse(Collections.singletonList(authenticateUser)))
                    .withTimestamp(DenaObjectUtils.timeStamp())
                    .build();
            return ResponseEntity.ok().body(denaResponse);
        } catch (DenaException ex) {
            throw DenaRestException.buildException(ex);
        }

    }

    @Override
    public ResponseEntity handleLogoutUser() {
        DenaRequestContext denaRequestContext = DenaRequestContext.getDenaRequestContext();

        HashMap<String, Object> parameters = JSONMapper.createHashMapFromJSON(denaRequestContext.getRequestBody());

        String appId = DenaRequestContext.getDenaRequestContext().getAppId();
        String userName = (String) parameters.get(User.EMAIL_FIELD_NAME);

        try {
            if (StringUtils.isEmpty(userName)) {
                log.warn("user name field is empty");
                throw new ParameterInvalidException("user_name is not set", ErrorCode.EMAIL_FIELD_IS_INVALID);
            }

            DenaObject denaObject = denaSecurityService.logoutUser(appId, userName);
            DenaResponse denaResponse = DenaResponseBuilder.aDenaResponse()
                    .withDenaObjectResponseList(createObjectResponse(Collections.singletonList(denaObject)))
                    .withTimestamp(DenaObjectUtils.timeStamp())
                    .build();

            return ResponseEntity.ok().body(denaResponse);
        } catch (DenaException ex) {
            throw DenaRestException.buildException(ex);
        }

    }

    private List<DenaObjectResponse> createObjectResponse(List<DenaObject> denaObjects) {
        return createObjectResponse(denaObjects, false);
    }

    private List<DenaObjectResponse> createObjectResponse(List<DenaObject> denaObjects, boolean reloadRelation) {
        List<DenaObjectResponse> denaObjectResponses = new ArrayList<>();
        denaObjects.forEach(denaObject -> {
            DenaObjectResponse objectResponse = new DenaObjectResponse();
            objectResponse.setObjectId(denaObject.getObjectId());
            objectResponse.setCreateTime(denaObject.getCreateTime());
            objectResponse.setUpdateTime(denaObject.getUpdateTime());
            objectResponse.setObjectURI(denaObject.getObjectURI());
            objectResponse.setFields(denaObject.getOtherFields());
            if (reloadRelation) {
                objectResponse.setRelation(denaObject.getDenaRelations());
            }
            denaObjectResponses.add(objectResponse);
        });

        return denaObjectResponses;
    }

    private DenaPager constructPager() {
        DenaRequestContext denaRequestContext = DenaRequestContext.getDenaRequestContext();
        String startIndex = denaRequestContext.getParameter(DenaPager.START_INDEX_PARAMETER);
        String pageSize = denaRequestContext.getParameter(DenaPager.PAGE_SIZE_PARAMETER);


        int defaultStartIndex = NumberUtils.toInt(startIndex, 0);
        int defaultPageSize = NumberUtils.toInt(pageSize, DenaConfigReader.readIntProperty("dena.pager.max.results", 50));

        if (defaultStartIndex < 0) {
            defaultStartIndex = 0;
        }

        if (defaultPageSize < 1 || defaultPageSize > DenaConfigReader.readIntProperty("dena.pager.max.results", 50)) {
            defaultPageSize = DenaConfigReader.readIntProperty("dena.pager.max.results", 50);
        }


        return DenaPager.DenaPagerBuilder.aDenaPager()
                .withPageSize(defaultPageSize)
                .withStartIndex(defaultStartIndex)
                .build();
    }

}
