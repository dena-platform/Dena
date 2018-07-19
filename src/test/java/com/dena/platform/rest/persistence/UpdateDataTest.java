package com.dena.platform.rest.persistence;

import com.dena.platform.rest.dto.*;
import com.dena.platform.restapi.dto.response.DenaResponse;
import com.dena.platform.utils.CommonConfig;
import org.junit.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.springframework.test.context.TestPropertySource;

import java.time.Instant;
import java.util.Arrays;
import java.util.Collections;

import static com.dena.platform.utils.JSONMapper.createJSONFromObject;
import static com.dena.platform.utils.TestUtils.isTimeEqualRegardlessOfSecond;
import static org.junit.Assert.assertTrue;

/**
 * @author Javad Alimohammadi [<bs.alimohammadi@gmail.com>]
 */

@TestPropertySource(properties = {"dena.api.security.enabled=false"})
public class UpdateDataTest extends AbstractDataStoreTest {

    @Test
    public void test_UpdateObject() throws Exception {
        /////////////////////////////////////////////
        //           Send Update Object Request
        /////////////////////////////////////////////
        TestRequestObject requestObject = new TestRequestObject();
        requestObject.setObjectId(objectId1);
        requestObject.addProperty("job", "new job");
        requestObject.addProperty("new field", "new value");
        requestObject.addProperty("actor_user", user.getEmail());


        TestDenaRelation testDenaRelation = TestDenaRelation.TestDenaRelationDTOBuilder.aTestDenaRelationDTO()
                .withRelationName("test_relation")
                .withRelationType("ONE-TO-ONE")
                .withTargetName(CommonConfig.COLLECTION_NAME)
                .withIds(objectId2)
                .build();

        requestObject.getRelatedObjects().add(testDenaRelation);

        DenaResponse actualReturnObject = performMergeObject(createJSONFromObject(requestObject), DenaResponse.class);

        /////////////////////////////////////////////
        //            Assert Update Response
        /////////////////////////////////////////////
        TestObjectResponse testObjectResponse = new TestObjectResponse();
        testObjectResponse.objectId = objectId1;
        testObjectResponse.objectURI = "/" + CommonConfig.COLLECTION_NAME + "/" + testObjectResponse.objectId;
        testObjectResponse.updateTime = actualReturnObject.getDenaObjectResponseList().get(0).getUpdateTime();
        testObjectResponse.addProperty("job", "new job");
        testObjectResponse.addProperty("new field", "new value");
        testObjectResponse.addProperty("name", "javad");


        TestDenaResponseDTO expectedReturnObject = new TestDenaResponseDTO();
        expectedReturnObject.httpStatusCode = 200;
        expectedReturnObject.timestamp = actualReturnObject.getTimestamp();
        expectedReturnObject.updateObjectCount = (1L);
        expectedReturnObject.setTestObjectResponseList(Collections.singletonList(testObjectResponse));

        assertTrue(isTimeEqualRegardlessOfSecond(actualReturnObject.getTimestamp(), Instant.now().toEpochMilli()));
        assertTrue(isTimeEqualRegardlessOfSecond(testObjectResponse.updateTime, Instant.now().toEpochMilli()));
        JSONAssert.assertEquals(createJSONFromObject(expectedReturnObject), createJSONFromObject(actualReturnObject), true);
    }

    @SuppressWarnings("Duplicates")
    @Test
    public void test_BulkUpdateObject() throws Exception {
        /////////////////////////////////////////////
        //           Send Update Object Request
        /////////////////////////////////////////////
        TestRequestObject requestObject1 = new TestRequestObject();
        requestObject1.setObjectId(objectId1);
        requestObject1.addProperty("job", "new job");
        requestObject1.addProperty("new field", "new value");
        requestObject1.addProperty("actor_user", user.getEmail());


        TestDenaRelation testDenaRelation = TestDenaRelation.TestDenaRelationDTOBuilder.aTestDenaRelationDTO()
                .withRelationName("test_relation")
                .withRelationType("ONE-TO-ONE")
                .withTargetName(CommonConfig.COLLECTION_NAME)
                .withIds(objectId2)
                .build();

        requestObject1.getRelatedObjects().add(testDenaRelation);

        TestRequestObject requestObject2 = new TestRequestObject();
        requestObject2.setObjectId(objectId2);
        requestObject2.addProperty("job", "new job 2");
        requestObject2.addProperty("new field", "new value");
        requestObject2.addProperty("actor_user", user.getEmail());


        DenaResponse actualReturnObject = performMergeObject(
                createJSONFromObject(Arrays.asList(requestObject1, requestObject2)),
                DenaResponse.class);

        /////////////////////////////////////////////
        //            Assert Update Response
        /////////////////////////////////////////////
        TestObjectResponse testObjectResponse1 = null;
        TestObjectResponse testObjectResponse2 = null;

        // Because the order of return object in not guaranteed in datastore we use this statement
        if (actualReturnObject.getDenaObjectResponseList().get(0).getObjectId().equals(objectId1)) {
            testObjectResponse1 = new TestObjectResponse();
            testObjectResponse1.objectId = objectId1;
            testObjectResponse1.objectURI = "/" + CommonConfig.COLLECTION_NAME + "/" + testObjectResponse1.objectId;
            testObjectResponse1.updateTime = actualReturnObject.getDenaObjectResponseList().get(0).getUpdateTime();
            testObjectResponse1.addProperty("job", "new job");
            testObjectResponse1.addProperty("new field", "new value");
            testObjectResponse1.addProperty("name", "javad");
        }

        if (actualReturnObject.getDenaObjectResponseList().get(1).getObjectId().equals(objectId1)) {
            testObjectResponse1 = new TestObjectResponse();
            testObjectResponse1.objectId = objectId1;
            testObjectResponse1.objectURI = "/" + CommonConfig.COLLECTION_NAME + "/" + testObjectResponse1.objectId;
            testObjectResponse1.updateTime = actualReturnObject.getDenaObjectResponseList().get(1).getUpdateTime();
            testObjectResponse1.addProperty("job", "new job");
            testObjectResponse1.addProperty("new field", "new value");
            testObjectResponse1.addProperty("name", "javad");
        }

        if (actualReturnObject.getDenaObjectResponseList().get(0).getObjectId().equals(objectId2)) {
            testObjectResponse2 = new TestObjectResponse();
            testObjectResponse2.objectId = objectId2;
            testObjectResponse2.objectURI = "/" + CommonConfig.COLLECTION_NAME + "/" + testObjectResponse2.objectId;
            testObjectResponse2.updateTime = actualReturnObject.getDenaObjectResponseList().get(0).getUpdateTime();
            testObjectResponse2.addProperty("job", "new job 2");
            testObjectResponse2.addProperty("new field", "new value");
            testObjectResponse2.addProperty("name", "javad");
        }

        if (actualReturnObject.getDenaObjectResponseList().get(1).getObjectId().equals(objectId2)) {
            testObjectResponse2 = new TestObjectResponse();
            testObjectResponse2.objectId = objectId2;
            testObjectResponse2.objectURI = "/" + CommonConfig.COLLECTION_NAME + "/" + testObjectResponse2.objectId;
            testObjectResponse2.updateTime = actualReturnObject.getDenaObjectResponseList().get(1).getUpdateTime();
            testObjectResponse2.addProperty("job", "new job 2");
            testObjectResponse2.addProperty("new field", "new value");
            testObjectResponse2.addProperty("name", "javad");
        }


        TestDenaResponseDTO expectedReturnObject = new TestDenaResponseDTO();
        expectedReturnObject.httpStatusCode = 200;
        expectedReturnObject.timestamp = actualReturnObject.getTimestamp();
        expectedReturnObject.updateObjectCount = (2L);
        expectedReturnObject.setTestObjectResponseList(Arrays.asList(testObjectResponse1, testObjectResponse2));

        assertTrue(isTimeEqualRegardlessOfSecond(actualReturnObject.getTimestamp(), Instant.now().toEpochMilli()));
        assertTrue(isTimeEqualRegardlessOfSecond(testObjectResponse1.updateTime, Instant.now().toEpochMilli()));
        JSONAssert.assertEquals(createJSONFromObject(expectedReturnObject), createJSONFromObject(actualReturnObject), JSONCompareMode.NON_EXTENSIBLE);
    }


    @Test
    public void test_UpdateObject_When_Object_Id_Invalid() throws Exception {
        //////////////////////////////////////////////////////////////////////
        //           Send Update Object Request - Invalid object id format
        //////////////////////////////////////////////////////////////////////
        String invalidObjectId = "5a1bd6176f";
        TestRequestObject requestObject = new TestRequestObject();
        requestObject.setObjectId(invalidObjectId);
        requestObject.addProperty("job", "new developer value");
        requestObject.addProperty("new field", "new value");
        requestObject.addProperty("actor_user", user.getEmail());
        TestDenaRelation testDenaRelation = TestDenaRelation.TestDenaRelationDTOBuilder.aTestDenaRelationDTO()
                .withRelationName("test_relation")
                .withRelationType("ONE-TO-ONE")
                .withTargetName(CommonConfig.COLLECTION_NAME)
                .withIds(objectId2)
                .build();

        requestObject.getRelatedObjects().add(testDenaRelation);

        TestErrorResponse actualReturnObject = performUpdateObject(createJSONFromObject(requestObject), 400, TestErrorResponse.class);

        ////////////////////////////////////////////////////////////////////////////
        //            Assert Update Object Request  - Invalid object id format
        ////////////////////////////////////////////////////////////////////////////
        TestErrorResponse expectedReturnObject = new TestErrorResponse();
        expectedReturnObject.status = 400;
        expectedReturnObject.errorCode = "2002";
        expectedReturnObject.messages = Collections.singletonList("Object_id is invalid");

        JSONAssert.assertEquals(createJSONFromObject(expectedReturnObject), createJSONFromObject(actualReturnObject), true);
    }

    @Test
    public void test_UpdateObject_When_Object_Id_Not_Exist() throws Exception {
        //////////////////////////////////////////////////////////////////////
        //           Send Update Object Request - object id not exist
        //////////////////////////////////////////////////////////////////////
        String notExistObjectId = "5a206dc2cc2a9b26e483d664";
        TestRequestObject requestObject = new TestRequestObject();
        requestObject.setObjectId(notExistObjectId);
        requestObject.addProperty("job", "new developer value");
        requestObject.addProperty("new field", "new value");
        requestObject.addProperty("actor_user", user.getEmail());

        TestDenaRelation testDenaRelation = TestDenaRelation.TestDenaRelationDTOBuilder.aTestDenaRelationDTO()
                .withRelationName("test_relation")
                .withRelationType("ONE-TO-ONE")
                .withTargetName(CommonConfig.COLLECTION_NAME)
                .withIds(objectId2)
                .build();

        requestObject.getRelatedObjects().add(testDenaRelation);
        TestErrorResponse actualReturnObject = performUpdateObject(createJSONFromObject(requestObject), 400, TestErrorResponse.class);

        ////////////////////////////////////////////////////////////////////////////
        //            Assert Update Object Request  - object id not exist
        ////////////////////////////////////////////////////////////////////////////
        TestErrorResponse expectedReturnObject = new TestErrorResponse();
        expectedReturnObject.status = 400;
        expectedReturnObject.errorCode = "2003";
        expectedReturnObject.messages = Collections.singletonList("Object_id not found");

        JSONAssert.assertEquals(createJSONFromObject(expectedReturnObject), createJSONFromObject(actualReturnObject), true);
    }

    @Test
    public void test_UpdateObject_When_Relation_Is_Invalid() throws Exception {
        //////////////////////////////////////////////////////////////////////
        //           Send Update Object Request - relation is not exist
        //////////////////////////////////////////////////////////////////////
        TestRequestObject requestObject = new TestRequestObject();
        requestObject.setObjectId(objectId3);
        requestObject.addProperty("job", "new developer value");
        requestObject.addProperty("new field", "new value");
        requestObject.addProperty("actor_user", user.getEmail());

        TestDenaRelation testDenaRelation = TestDenaRelation.TestDenaRelationDTOBuilder.aTestDenaRelationDTO()
                .withRelationName("test_relation")
                .withRelationType("ONE-TO-ONE")
                .withTargetName(CommonConfig.COLLECTION_NAME)
                .withIds("invalid id")
                .build();

        requestObject.getRelatedObjects().add(testDenaRelation);
        TestErrorResponse actualReturnObject = performUpdateObject(createJSONFromObject(requestObject), 400, TestErrorResponse.class);

        ////////////////////////////////////////////////////////////////////////////
        //            Assert Update Object Request  - relation is not exist
        ////////////////////////////////////////////////////////////////////////////
        TestErrorResponse expectedReturnObject = new TestErrorResponse();
        expectedReturnObject.status = 400;
        expectedReturnObject.errorCode = "2001";
        expectedReturnObject.messages = Collections.singletonList("Relation(s) is invalid");

        JSONAssert.assertEquals(createJSONFromObject(expectedReturnObject), createJSONFromObject(actualReturnObject), true);
    }


}
