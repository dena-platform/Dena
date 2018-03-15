package com.dena.platform.rest.persistence;

import com.dena.platform.rest.dto.*;
import com.dena.platform.utils.CommonConfig;
import org.junit.Test;
import org.skyscreamer.jsonassert.JSONAssert;

import java.time.Instant;
import java.util.Arrays;
import java.util.Collections;

import static com.dena.platform.utils.JSONMapper.createJSONFromObject;
import static com.dena.platform.utils.TestUtils.isTimeEqualRegardlessOfSecond;
import static org.junit.Assert.assertTrue;

/**
 * @author Javad Alimohammadi [<bs.alimohammadi@yahoo.com>]
 */
public class UpdateDataTest extends AbstractDataStoreTest {

    @Test
    public void test_UpdateObject() throws Exception {
        /////////////////////////////////////////////
        //           Send Update Object Request
        /////////////////////////////////////////////
        TestRequestObjectDTO requestObject = new TestRequestObjectDTO();
        requestObject.setObjectId(objectId1);
        requestObject.addProperty("job", "new job");
        requestObject.addProperty("new field", "new value");

        TestDenaRelationDTO testDenaRelationDTO = TestDenaRelationDTO.TestDenaRelationDTOBuilder.aTestDenaRelationDTO()
                .withRelationName("test_relation")
                .withRelationType("ONE-TO-ONE")
                .withTargetName(CommonConfig.COLLECTION_NAME)
                .withIds(objectId2)
                .build();

        requestObject.getRelatedObjects().add(testDenaRelationDTO);

        TestDenaResponseDTO actualReturnObject = performUpdateObject(createJSONFromObject(requestObject), TestDenaResponseDTO.class);

        /////////////////////////////////////////////
        //            Assert Update Response
        /////////////////////////////////////////////
        TestObjectResponseDTO testObjectResponseDTO = new TestObjectResponseDTO();
        testObjectResponseDTO.objectId = objectId1;
        testObjectResponseDTO.objectURI = "/" + CommonConfig.COLLECTION_NAME + "/" + testObjectResponseDTO.objectId;
        testObjectResponseDTO.updateTime = actualReturnObject.getTestObjectResponseDTOList().get(0).updateTime;
        testObjectResponseDTO.addProperty("job", "new job");
        testObjectResponseDTO.addProperty("new field", "new value");
        testObjectResponseDTO.addProperty("name", "javad");


        TestDenaResponseDTO expectedReturnObject = new TestDenaResponseDTO();
        expectedReturnObject.timestamp = actualReturnObject.timestamp;
        expectedReturnObject.updateObjectCount = (1L);
        expectedReturnObject.setTestObjectResponseDTOList(Collections.singletonList(testObjectResponseDTO));

        assertTrue(isTimeEqualRegardlessOfSecond(actualReturnObject.timestamp, Instant.now().toEpochMilli()));
        assertTrue(isTimeEqualRegardlessOfSecond(testObjectResponseDTO.updateTime, Instant.now().toEpochMilli()));
        JSONAssert.assertEquals(createJSONFromObject(expectedReturnObject), createJSONFromObject(actualReturnObject), true);
    }

    @Test
    public void test_UpdateObject_When_Object_Id_Invalid() throws Exception {
        //////////////////////////////////////////////////////////////////////
        //           Send Update Object Request - Invalid object id format
        //////////////////////////////////////////////////////////////////////
        String invalidObjectId = "5a1bd6176f";
        TestRequestObjectDTO requestObject = new TestRequestObjectDTO();
        requestObject.setObjectId(invalidObjectId);
        requestObject.addProperty("job", "new developer value");
        requestObject.addProperty("new field", "new value");
        TestDenaRelationDTO testDenaRelationDTO = TestDenaRelationDTO.TestDenaRelationDTOBuilder.aTestDenaRelationDTO()
                .withRelationName("test_relation")
                .withRelationType("ONE-TO-ONE")
                .withTargetName(CommonConfig.COLLECTION_NAME)
                .withIds(objectId2)
                .build();

        requestObject.getRelatedObjects().add(testDenaRelationDTO);

        TestErrorResponseDTO actualReturnObject = performUpdateObject(createJSONFromObject(requestObject), 400, TestErrorResponseDTO.class);

        ////////////////////////////////////////////////////////////////////////////
        //            Assert Update Object Request  - Invalid object id format
        ////////////////////////////////////////////////////////////////////////////
        TestErrorResponseDTO expectedReturnObject = new TestErrorResponseDTO();
        expectedReturnObject.status = 400;
        expectedReturnObject.errorCode = "2002";
        expectedReturnObject.messages = Collections.singletonList("object_id is invalid");

        JSONAssert.assertEquals(createJSONFromObject(expectedReturnObject), createJSONFromObject(actualReturnObject), true);
    }

    @Test
    public void test_UpdateObject_When_Object_Id_Not_Exist() throws Exception {
        //////////////////////////////////////////////////////////////////////
        //           Send Update Object Request - object id not exist
        //////////////////////////////////////////////////////////////////////
        String notExistObjectId = "5a206dc2cc2a9b26e483d664";
        TestRequestObjectDTO requestObject = new TestRequestObjectDTO();
        requestObject.setObjectId(notExistObjectId);
        requestObject.addProperty("job", "new developer value");
        requestObject.addProperty("new field", "new value");

        TestDenaRelationDTO testDenaRelationDTO = TestDenaRelationDTO.TestDenaRelationDTOBuilder.aTestDenaRelationDTO()
                .withRelationName("test_relation")
                .withRelationType("ONE-TO-ONE")
                .withTargetName(CommonConfig.COLLECTION_NAME)
                .withIds(objectId2)
                .build();

        requestObject.getRelatedObjects().add(testDenaRelationDTO);
        TestErrorResponseDTO actualReturnObject = performUpdateObject(createJSONFromObject(requestObject), 400, TestErrorResponseDTO.class);

        ////////////////////////////////////////////////////////////////////////////
        //            Assert Update Object Request  - object id not exist
        ////////////////////////////////////////////////////////////////////////////
        TestErrorResponseDTO expectedReturnObject = new TestErrorResponseDTO();
        expectedReturnObject.status = 400;
        expectedReturnObject.errorCode = "2003";
        expectedReturnObject.messages = Collections.singletonList("object_id not found");

        JSONAssert.assertEquals(createJSONFromObject(expectedReturnObject), createJSONFromObject(actualReturnObject), true);
    }

    @Test
    public void test_UpdateObject_When_Relation_Is_Invalid() throws Exception {
        //////////////////////////////////////////////////////////////////////
        //           Send Update Object Request - relation is not exist
        //////////////////////////////////////////////////////////////////////
        TestRequestObjectDTO requestObject = new TestRequestObjectDTO();
        requestObject.setObjectId(objectId3);
        requestObject.addProperty("job", "new developer value");
        requestObject.addProperty("new field", "new value");
        TestDenaRelationDTO testDenaRelationDTO = TestDenaRelationDTO.TestDenaRelationDTOBuilder.aTestDenaRelationDTO()
                .withRelationName("test_relation")
                .withRelationType("ONE-TO-ONE")
                .withTargetName(CommonConfig.COLLECTION_NAME)
                .withIds("invalid id")
                .build();

        requestObject.getRelatedObjects().add(testDenaRelationDTO);
        TestErrorResponseDTO actualReturnObject = performUpdateObject(createJSONFromObject(requestObject), 400, TestErrorResponseDTO.class);

        ////////////////////////////////////////////////////////////////////////////
        //            Assert Update Object Request  - relation is not exist
        ////////////////////////////////////////////////////////////////////////////
        TestErrorResponseDTO expectedReturnObject = new TestErrorResponseDTO();
        expectedReturnObject.status = 400;
        expectedReturnObject.errorCode = "2001";
        expectedReturnObject.messages = Collections.singletonList("relation(s) is invalid");

        JSONAssert.assertEquals(createJSONFromObject(expectedReturnObject), createJSONFromObject(actualReturnObject), true);
    }


}
