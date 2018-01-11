package com.dena.platform.rest.dataStore;

import com.dena.platform.rest.dto.*;
import com.dena.platform.utils.CommonConfig;
import org.junit.Ignore;
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

    @Ignore
    @Test
    public void test_UpdateObject() throws Exception {
        /////////////////////////////////////////////
        //           Send Update Object Request
        /////////////////////////////////////////////
        TestRequestObject requestObject = new TestRequestObject();
        requestObject.setObjectId(objectId3);
        requestObject.addProperty("job", "new developer value");
        requestObject.addProperty("new field", "new value");
        String newObjectId = randomObjectId;
        requestObject.getRelatedObjects().add(new TestRelatedObject(newObjectId, CommonConfig.COLLECTION_NAME));

        ReturnedObject actualReturnObject = performUpdateObject(createJSONFromObject(requestObject), ReturnedObject.class);

        /////////////////////////////////////////////
        //            Assert Update Response
        /////////////////////////////////////////////
        TestObjectResponse testObjectResponse = new TestObjectResponse();
        testObjectResponse.objectId = objectId3;
        testObjectResponse.objectURI = "/" + CommonConfig.COLLECTION_NAME + "/" + objectId3;
        testObjectResponse.addProperty("job", "new developer value");
        testObjectResponse.addProperty("new field", "new value");
        testObjectResponse.addProperty("name", "javad");
        testObjectResponse.testRelatedObjects = Collections.singletonList(new TestRelatedObject(newObjectId, CommonConfig.COLLECTION_NAME));


        ReturnedObject expectedReturnObject = new ReturnedObject();
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
        String newObjectId = randomObjectId;
        requestObject.getRelatedObjects().add(new TestRelatedObject(newObjectId, CommonConfig.COLLECTION_NAME));

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


}
