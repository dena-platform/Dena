package com.dena.platform.rest.dataStore;

import com.dena.platform.rest.dto.ReturnedObject;
import com.dena.platform.rest.dto.TestObjectResponse;
import com.dena.platform.rest.dto.TestRelatedObject;
import com.dena.platform.rest.dto.TestRequestObject;
import com.dena.platform.utils.CommonConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.Instant;
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
        requestObject.setObjectId(objectId3);
        requestObject.addProperty("job", "new developer value");
        requestObject.addProperty("new field", "new value");
        String newObjectId = randomObjectId;
        requestObject.getRelatedObjects().add(new TestRelatedObject(newObjectId, CommonConfig.COLLECTION_NAME));

        ReturnedObject actualReturnObject = performUpdateObject(createJSONFromObject(requestObject));

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
        TestRequestObject requestObject = new TestRequestObject();
        requestObject.setObjectId(objectId3);
        requestObject.addProperty("job", "new developer value");
        requestObject.addProperty("new field", "new value");
        String newObjectId = randomObjectId;
        requestObject.getRelatedObjects().add(new TestRelatedObject(newObjectId, CommonConfig.COLLECTION_NAME));

        ReturnedObject actualReturnObject = performUpdateObject(createJSONFromObject(requestObject));


    }


}
