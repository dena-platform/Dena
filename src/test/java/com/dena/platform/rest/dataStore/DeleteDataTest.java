package com.dena.platform.rest.dataStore;

import com.dena.platform.rest.dto.ReturnedObject;
import com.dena.platform.rest.dto.TestErrorResponse;
import com.dena.platform.rest.dto.TestObjectResponse;
import com.dena.platform.rest.dto.TestRelatedObject;
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
public class DeleteDataTest extends AbstractDataStoreTest {

    @Test
    public void test_DeleteObjects() throws Exception {
        /////////////////////////////////////////////
        //            Send Delete Objects Request
        /////////////////////////////////////////////
        ReturnedObject actualReturnObject = performDeleteRequest(Arrays.asList(objectId1, objectId2, objectId3), 200, ReturnedObject.class);

        /////////////////////////////////////////////
        //            Assert Deleted Object
        /////////////////////////////////////////////
        ReturnedObject expectedReturnObject = new ReturnedObject();
        expectedReturnObject.setTimestamp(actualReturnObject.getTimestamp());
        expectedReturnObject.setCount(3L);

        // check timestamp field of returned object
        assertTrue(isTimeEqualRegardlessOfSecond(actualReturnObject.getTimestamp(), Instant.now().toEpochMilli()));
        JSONAssert.assertEquals(createJSONFromObject(expectedReturnObject), createJSONFromObject(actualReturnObject), true);

    }


    @Test
    public void test_DeleteObject_When_Object_Id_Invalid() throws Exception {
        /////////////////////////////////////////////////////////////////////////
        //            Send Delete Object Request - Invalid object id format
        /////////////////////////////////////////////////////////////////////////
        String invalidObjectId = "5a1bd6176f";
        TestErrorResponse actualReturnObject = performDeleteRequest(Collections.singletonList(invalidObjectId), 400, TestErrorResponse.class);

        TestErrorResponse expectedReturnObject = new TestErrorResponse();
        expectedReturnObject.status = 400;
        expectedReturnObject.errorCode = "2002";
        expectedReturnObject.messages = Arrays.asList("objectId is invalid");

        JSONAssert.assertEquals(createJSONFromObject(expectedReturnObject), createJSONFromObject(actualReturnObject), true);
    }

    @Test
    public void test_DeleteObject_When_Object_Id_Not_Exist() throws Exception {
        //////////////////////////////////////////////////////////////////
        //            Send Delete Object Request - object id not exist
        //////////////////////////////////////////////////////////////////

        ReturnedObject actualReturnObject = performDeleteRequest(Collections.singletonList(randomObjectId), 200, ReturnedObject.class);

        ReturnedObject expectedReturnObject = new ReturnedObject();
        expectedReturnObject.setTimestamp(actualReturnObject.getTimestamp());
        expectedReturnObject.setCount(0L);

        // check timestamp field of returned object
        assertTrue(isTimeEqualRegardlessOfSecond(actualReturnObject.getTimestamp(), Instant.now().toEpochMilli()));
        JSONAssert.assertEquals(createJSONFromObject(expectedReturnObject), createJSONFromObject(actualReturnObject), true);
    }

    @Test
    public void test_DeleteObject_When_App_Id_Not_Exist() throws Exception {
        //////////////////////////////////////////////////////////////////
        //            Send Delete Object Request - app id not exist
        //////////////////////////////////////////////////////////////////
        ReturnedObject actualReturnObject = performDeleteRequest(Collections.singletonList(randomObjectId), "/v1/invalid_app_id/denaTestCollection/", 200, ReturnedObject.class);

        ReturnedObject expectedReturnObject = new ReturnedObject();
        expectedReturnObject.setTimestamp(actualReturnObject.getTimestamp());
        expectedReturnObject.setCount(0L);

        // check timestamp field of returned object
        assertTrue(isTimeEqualRegardlessOfSecond(actualReturnObject.getTimestamp(), Instant.now().toEpochMilli()));
        JSONAssert.assertEquals(createJSONFromObject(expectedReturnObject), createJSONFromObject(actualReturnObject), true);

    }


    @Test
    public void test_DeleteRelation_With_Object() throws Exception {

        /////////////////////////////////////////////
        //            Send Delete Relation
        /////////////////////////////////////////////
        ReturnedObject actualReturnObject = performDeleteRelationWithObject(objectId3, CommonConfig.COLLECTION_NAME, 200, objectId1, ReturnedObject.class);

        /////////////////////////////////////////////
        //            Assert Delete Relation
        /////////////////////////////////////////////
        ReturnedObject expectedReturnObject = new ReturnedObject();
        expectedReturnObject.setTimestamp(actualReturnObject.getTimestamp());
        expectedReturnObject.setCount(1L);

        assertTrue(isTimeEqualRegardlessOfSecond(actualReturnObject.getTimestamp(), Instant.now().toEpochMilli()));
        JSONAssert.assertEquals(createJSONFromObject(expectedReturnObject), createJSONFromObject(actualReturnObject), true);

        /////////////////////////////////////////////
        //            Check Response Find
        /////////////////////////////////////////////
        actualReturnObject = performFindRequest(objectId3);
        expectedReturnObject = new ReturnedObject();
        expectedReturnObject.setCount(1L);
        expectedReturnObject.setTimestamp(actualReturnObject.getTimestamp());

        TestObjectResponse testObjectResponse = new TestObjectResponse();
        testObjectResponse.objectId = objectId3;
        testObjectResponse.objectURI = "/" + CommonConfig.COLLECTION_NAME + "/" + objectId3;
        testObjectResponse.addProperty("name", "javad");
        testObjectResponse.addProperty("job", "developer");
        testObjectResponse.testRelatedObjects = Collections.singletonList(new TestRelatedObject(objectId2, CommonConfig.COLLECTION_NAME));
        expectedReturnObject.setTestObjectResponseList(Collections.singletonList(testObjectResponse));

        assertTrue(isTimeEqualRegardlessOfSecond(actualReturnObject.getTimestamp(), Instant.now().toEpochMilli()));
        JSONAssert.assertEquals(createJSONFromObject(expectedReturnObject), createJSONFromObject(actualReturnObject), true);

    }

    @Test
    public void test_DeleteRelation_With_Object_When_Object_Id_Invalid() throws Exception {
        /////////////////////////////////////////////////////////////////////////
        //            Send Delete Relation - Invalid object id format
        /////////////////////////////////////////////////////////////////////////
        String invalidObjectId = "5a1bd6176f";
        TestErrorResponse actualReturnObject = performDeleteRelationWithObject(invalidObjectId, CommonConfig.COLLECTION_NAME, 400, objectId1, TestErrorResponse.class);

        TestErrorResponse expectedReturnObject = new TestErrorResponse();
        expectedReturnObject.status = 400;
        expectedReturnObject.errorCode = "2002";
        expectedReturnObject.messages = Arrays.asList("objectId is invalid");

        JSONAssert.assertEquals(createJSONFromObject(expectedReturnObject), createJSONFromObject(actualReturnObject), true);

    }

    @Test
    public void test_DeleteRelation() throws Exception {
        /////////////////////////////////////////////
        //            Send Delete Relation
        /////////////////////////////////////////////
        ReturnedObject actualReturnObject = performDeleteRelation(objectId3, CommonConfig.COLLECTION_NAME);

        /////////////////////////////////////////////
        //            Assert Delete Relation
        /////////////////////////////////////////////
        ReturnedObject expectedReturnObject = new ReturnedObject();
        expectedReturnObject.setTimestamp(actualReturnObject.getTimestamp());
        expectedReturnObject.setCount(2L);
        expectedReturnObject.setTimestamp(actualReturnObject.getTimestamp());

        assertTrue(isTimeEqualRegardlessOfSecond(actualReturnObject.getTimestamp(), Instant.now().toEpochMilli()));
        JSONAssert.assertEquals(createJSONFromObject(expectedReturnObject), createJSONFromObject(actualReturnObject), true);

        /////////////////////////////////////////////
        //            Check Response Find
        /////////////////////////////////////////////
        actualReturnObject = performFindRequest(objectId3);
        expectedReturnObject = new ReturnedObject();
        expectedReturnObject.setCount(1L);
        expectedReturnObject.setTimestamp(actualReturnObject.getTimestamp());

        TestObjectResponse testObjectResponse = new TestObjectResponse();
        testObjectResponse.objectId = objectId3;
        testObjectResponse.objectURI = "/" + CommonConfig.COLLECTION_NAME + "/" + objectId3;
        testObjectResponse.addProperty("name", "javad");
        testObjectResponse.addProperty("job", "developer");
        expectedReturnObject.setTestObjectResponseList(Collections.singletonList(testObjectResponse));

        assertTrue(isTimeEqualRegardlessOfSecond(actualReturnObject.getTimestamp(), Instant.now().toEpochMilli()));
        JSONAssert.assertEquals(createJSONFromObject(expectedReturnObject), createJSONFromObject(actualReturnObject), true);

    }

    @Test
    public void test_DeleteRelation_With_Object_When_Relation_Not_Exist() throws Exception {
        /////////////////////////////////////////////////////////////////////////
        //            Send Delete Relation - Invalid relation name
        /////////////////////////////////////////////////////////////////////////
        ReturnedObject actualReturnObject = performDeleteRelationWithObject(objectId3, "not_exist_relation", 200, objectId1, ReturnedObject.class);

        ReturnedObject expectedReturnObject = new ReturnedObject();
        expectedReturnObject.setTimestamp(actualReturnObject.getTimestamp());
        expectedReturnObject.setCount(0L);

        JSONAssert.assertEquals(createJSONFromObject(expectedReturnObject), createJSONFromObject(actualReturnObject), true);

    }


}
