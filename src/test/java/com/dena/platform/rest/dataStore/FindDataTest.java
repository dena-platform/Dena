package com.dena.platform.rest.dataStore;

import com.dena.platform.rest.dto.ReturnedObject;
import com.dena.platform.rest.dto.TestObjectResponse;
import com.dena.platform.rest.dto.TestRelatedObject;
import com.dena.platform.utils.CommonConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;
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
public class FindDataTest extends AbstractDataStoreTest {

    @Test
    public void test_FindObject_When_Object_Exist() throws Exception {

        /////////////////////////////////////////////
        //            Send Find Object Request
        /////////////////////////////////////////////
        ReturnedObject actualReturnObject = performFindRequest(objectId3);

        ReturnedObject expectedReturnObject = new ReturnedObject();
        expectedReturnObject.setCount(1L);
        expectedReturnObject.setTimestamp(actualReturnObject.getTimestamp());

        /////////////////////////////////////////////
        //            Assert Found Object
        /////////////////////////////////////////////   
        TestObjectResponse testObjectResponse = new TestObjectResponse();
        testObjectResponse.objectId = objectId3;
        testObjectResponse.objectURI = "/" + CommonConfig.COLLECTION_NAME + "/" + objectId3;
        testObjectResponse.addProperty("name", "javad");
        testObjectResponse.addProperty("job", "developer");
        testObjectResponse.testRelatedObjects = Arrays.asList(new TestRelatedObject(objectId1, CommonConfig.COLLECTION_NAME), new TestRelatedObject(objectId2, CommonConfig.COLLECTION_NAME));
        expectedReturnObject.setTestObjectResponseList(Collections.singletonList(testObjectResponse));

        // check timestamp field of returned object
        assertTrue(isTimeEqualRegardlessOfSecond(actualReturnObject.getTimestamp(), Instant.now().toEpochMilli()));
        JSONAssert.assertEquals(createJSONFromObject(expectedReturnObject), createJSONFromObject(actualReturnObject), true);

    }

    @Test
    public void test_FindRelatedObject() throws Exception {
        /////////////////////////////////////////////
        //            Send Find Object Request
        /////////////////////////////////////////////
        ReturnedObject actualReturnObject = performFindRelationRequest(objectId3, CommonConfig.COLLECTION_NAME);

        /////////////////////////////////////////////
        //            Assert Found Object
        /////////////////////////////////////////////
        ReturnedObject expectedReturnObject = new ReturnedObject();
        expectedReturnObject.setCount(2L);
        expectedReturnObject.setTimestamp(actualReturnObject.getTimestamp());


        TestObjectResponse testObjectResponse1 = new TestObjectResponse();
        testObjectResponse1.objectId = objectId1;
        testObjectResponse1.objectURI = "/" + CommonConfig.COLLECTION_NAME + "/" + objectId1;
        testObjectResponse1.addProperty("name", "javad");
        testObjectResponse1.addProperty("job", "developer");

        TestObjectResponse testObjectResponse2 = new TestObjectResponse();
        testObjectResponse2.objectId = objectId2;
        testObjectResponse2.objectURI = "/" + CommonConfig.COLLECTION_NAME + "/" + objectId2;
        testObjectResponse2.addProperty("name", "javad");
        testObjectResponse2.addProperty("job", "developer");

        expectedReturnObject.setTestObjectResponseList(Arrays.asList(testObjectResponse1, testObjectResponse2));


        // check timestamp field of returned object
        assertTrue(isTimeEqualRegardlessOfSecond(actualReturnObject.getTimestamp(), Instant.now().toEpochMilli()));
        JSONAssert.assertEquals(createJSONFromObject(expectedReturnObject), createJSONFromObject(actualReturnObject), false);

    }

    @Test
    public void test_FindObject_When_Object_Not_Exist() throws Exception {

        /////////////////////////////////////////////
        //            Send Find Object Request
        /////////////////////////////////////////////
        ReturnedObject actualReturnObject = performFindRequest(randomObjectId);

        /////////////////////////////////////////////
        //            Assert Found Object
        /////////////////////////////////////////////
        ReturnedObject expectedReturnObject = new ReturnedObject();
        expectedReturnObject.setCount(0L);
        expectedReturnObject.setTimestamp(actualReturnObject.getTimestamp());

        // check timestamp field of returned object
        assertTrue(isTimeEqualRegardlessOfSecond(actualReturnObject.getTimestamp(), Instant.now().toEpochMilli()));
        JSONAssert.assertEquals(createJSONFromObject(expectedReturnObject), createJSONFromObject(actualReturnObject), JSONCompareMode.NON_EXTENSIBLE);

    }

}
