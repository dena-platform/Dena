package com.dena.platform.rest.dataStore;

import com.dena.platform.rest.dto.*;
import com.dena.platform.utils.CommonConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.Instant;
import java.util.Arrays;
import java.util.Collections;

import static com.dena.platform.utils.JSONMapper.createJSONFromObject;
import static com.dena.platform.utils.TestUtils.isTimeEqualRegardlessOfSecond;
import static org.junit.Assert.assertTrue;

/**
 * @author Javad Alimohammadi [<bs.alimohammadi@yahoo.com>]
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class UpdateDataTest extends AbstractDataStoreTest {

    @Test
    public void test_UpdateObject() throws Exception {
        /////////////////////////////////////////////
        //           Send Update Object Request
        /////////////////////////////////////////////
        TestRequestObject requestObject = new TestRequestObject();
        requestObject.setObjectId(objectId1);
        requestObject.addProperty("job", "new developer value");
        requestObject.addProperty("new field", "new value");
        requestObject.getRelatedObjects().add(new TestRelatedObject(objectId2, CommonConfig.COLLECTION_NAME));

        TestDenaResponse actualReturnObject = performUpdateObject(createJSONFromObject(requestObject), TestDenaResponse.class);

        /////////////////////////////////////////////
        //            Assert Update Response
        /////////////////////////////////////////////
        TestObjectResponse testObjectResponse = new TestObjectResponse();
        testObjectResponse.objectId = objectId1;
        testObjectResponse.objectURI = "/" + CommonConfig.COLLECTION_NAME + "/" + objectId1;
        testObjectResponse.addProperty("job", "new developer value");
        testObjectResponse.addProperty("new field", "new value");
        testObjectResponse.addProperty("name", "javad");
        testObjectResponse.testRelatedObjects = Collections.singletonList(new TestRelatedObject(objectId2, CommonConfig.COLLECTION_NAME));


        TestDenaResponse expectedReturnObject = new TestDenaResponse();
        expectedReturnObject.setTimestamp(actualReturnObject.getTimestamp());
        expectedReturnObject.setCount(1L);
        expectedReturnObject.setTestObjectResponseList(Collections.singletonList(testObjectResponse));

        assertTrue(isTimeEqualRegardlessOfSecond(actualReturnObject.getTimestamp(), Instant.now().toEpochMilli()));
        JSONAssert.assertEquals(createJSONFromObject(expectedReturnObject), createJSONFromObject(actualReturnObject), true);
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
        requestObject.getRelatedObjects().add(new TestRelatedObject(objectId1, CommonConfig.COLLECTION_NAME));

        TestErrorResponse actualReturnObject = performUpdateObject(createJSONFromObject(requestObject), 400, TestErrorResponse.class);

        ////////////////////////////////////////////////////////////////////////////
        //            Assert Update Object Request  - Invalid object id format
        ////////////////////////////////////////////////////////////////////////////
        TestErrorResponse expectedReturnObject = new TestErrorResponse();
        expectedReturnObject.status = 400;
        expectedReturnObject.errorCode = "2002";
        expectedReturnObject.messages = Arrays.asList("objectId is invalid");

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
        requestObject.getRelatedObjects().add(new TestRelatedObject(objectId1, CommonConfig.COLLECTION_NAME));

        TestErrorResponse actualReturnObject = performUpdateObject(createJSONFromObject(requestObject), 400, TestErrorResponse.class);

        ////////////////////////////////////////////////////////////////////////////
        //            Assert Update Object Request  - object id not exist
        ////////////////////////////////////////////////////////////////////////////
        TestErrorResponse expectedReturnObject = new TestErrorResponse();
        expectedReturnObject.status = 400;
        expectedReturnObject.errorCode = "2003";
        expectedReturnObject.messages = Arrays.asList("objectId not found");

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
        requestObject.getRelatedObjects().add(new TestRelatedObject(objectId1, "not_exist_relation"));

        TestErrorResponse actualReturnObject = performUpdateObject(createJSONFromObject(requestObject), 400, TestErrorResponse.class);

        ////////////////////////////////////////////////////////////////////////////
        //            Assert Update Object Request  - relation is not exist
        ////////////////////////////////////////////////////////////////////////////
        TestErrorResponse expectedReturnObject = new TestErrorResponse();
        expectedReturnObject.status = 400;
        expectedReturnObject.errorCode = "2001";
        expectedReturnObject.messages = Collections.singletonList("relation(s) is invalid");

        JSONAssert.assertEquals(createJSONFromObject(expectedReturnObject), createJSONFromObject(actualReturnObject), true);
    }


}
