package com.dena.platform.rest.persistence;

import com.dena.platform.rest.dto.TestDenaResponse;
import com.dena.platform.rest.dto.TestErrorResponse;
import com.dena.platform.rest.dto.TestObjectResponse;
import com.dena.platform.restapi.dto.response.DenaResponse;
import com.dena.platform.utils.CommonConfig;
import org.junit.Test;
import org.skyscreamer.jsonassert.JSONAssert;
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
public class DeleteObjectTest extends AbstractDataStoreTest {

    @Test
    public void test_DeleteObjects() throws Exception {
        /////////////////////////////////////////////
        //            Send Delete Objects Request
        /////////////////////////////////////////////
        DenaResponse actualReturnObject = performDeleteRequest(Arrays.asList(objectId1, objectId2, objectId3),
                200, DenaResponse.class);

        /////////////////////////////////////////////
        //            Assert Deleted Object
        /////////////////////////////////////////////
        TestDenaResponse expectedReturnObject = new TestDenaResponse();
        expectedReturnObject.httpStatusCode = 200;
        expectedReturnObject.timestamp = actualReturnObject.getTimestamp();
        expectedReturnObject.deleteObjectCount = 3L;

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
        TestErrorResponse actualReturnObject = performDeleteRequest(Collections.singletonList(invalidObjectId),
                400, TestErrorResponse.class);

        TestErrorResponse expectedReturnObject = new TestErrorResponse();
        expectedReturnObject.status = 400;
        expectedReturnObject.errorCode = "2002";
        expectedReturnObject.messages = Collections.singletonList("Object_id is invalid");

        JSONAssert.assertEquals(createJSONFromObject(expectedReturnObject), createJSONFromObject(actualReturnObject), true);
    }

    @Test
    public void test_DeleteObject_When_Object_Id_Not_Exist() throws Exception {
        //////////////////////////////////////////////////////////////////
        //            Send Delete Object Request - object id not exist
        //////////////////////////////////////////////////////////////////

        DenaResponse actualReturnObject = performDeleteRequest(Collections.singletonList(randomObjectId),
                200, DenaResponse.class);

        TestDenaResponse expectedReturnObject = new TestDenaResponse();
        expectedReturnObject.httpStatusCode = 200;
        expectedReturnObject.timestamp = actualReturnObject.getTimestamp();
        expectedReturnObject.deleteObjectCount = 0L;

        // check timestamp field of returned object
        assertTrue(isTimeEqualRegardlessOfSecond(actualReturnObject.getTimestamp(), Instant.now().toEpochMilli()));
        JSONAssert.assertEquals(createJSONFromObject(expectedReturnObject), createJSONFromObject(actualReturnObject), true);
    }

    @Test
    public void test_DeleteObject_When_App_Id_Not_Exist() throws Exception {
        //////////////////////////////////////////////////////////////////
        //            Send Delete Object Request - app id not exist
        //////////////////////////////////////////////////////////////////
        DenaResponse actualReturnObject = performDeleteRequest(Collections.singletonList(randomObjectId),
                200, DenaResponse.class);

        TestDenaResponse expectedReturnObject = new TestDenaResponse();
        expectedReturnObject.httpStatusCode = 200;
        expectedReturnObject.timestamp = (actualReturnObject.getTimestamp());
        expectedReturnObject.deleteObjectCount = (0L);

        // check timestamp field of returned object
        assertTrue(isTimeEqualRegardlessOfSecond(actualReturnObject.getTimestamp(), Instant.now().toEpochMilli()));
        JSONAssert.assertEquals(createJSONFromObject(expectedReturnObject), createJSONFromObject(actualReturnObject), true);

    }


    @Test
    public void test_DeleteRelation_With_Object() throws Exception {

        /////////////////////////////////////////////
        //            Send Delete Relation
        /////////////////////////////////////////////
        DenaResponse actualReturnObject = performDeleteRelationWithObject(objectId3, CommonConfig.RELATION_NAME, 200,
                objectId1, DenaResponse.class);

        /////////////////////////////////////////////
        //            Assert Delete Relation
        /////////////////////////////////////////////
        TestDenaResponse expectedReturnObject = new TestDenaResponse();
        expectedReturnObject.httpStatusCode = 200;
        expectedReturnObject.timestamp = (actualReturnObject.getTimestamp());
        expectedReturnObject.deleteRelationCount = 1L;

        assertTrue(isTimeEqualRegardlessOfSecond(actualReturnObject.getTimestamp(), Instant.now().toEpochMilli()));
        JSONAssert.assertEquals(createJSONFromObject(expectedReturnObject), createJSONFromObject(actualReturnObject), true);


        /////////////////////////////////////////////
        //            Check Deleted Object
        /////////////////////////////////////////////
        actualReturnObject = performFindRelationRequest(objectId3, CommonConfig.RELATION_NAME, 0, 1, DenaResponse.class);
        expectedReturnObject = new TestDenaResponse();
        expectedReturnObject.httpStatusCode = 200;
        expectedReturnObject.foundObjectCount = 1;
        expectedReturnObject.timestamp = actualReturnObject.getTimestamp();

        TestObjectResponse testObjectResponse = new TestObjectResponse();
        testObjectResponse.objectId = objectId2;
        testObjectResponse.objectURI = "/" + CommonConfig.COLLECTION_NAME + "/" + objectId2;
        testObjectResponse.addProperty("name", "javad");
        testObjectResponse.addProperty("job", "developer");
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
        expectedReturnObject.messages = Arrays.asList("Object_id is invalid");

        JSONAssert.assertEquals(createJSONFromObject(expectedReturnObject), createJSONFromObject(actualReturnObject), true);

    }

    @Test
    public void test_DeleteRelation() throws Exception {
        /////////////////////////////////////////////
        //            Send Delete Relation
        /////////////////////////////////////////////
        DenaResponse actualReturnObject = performDeleteRelation(objectId3, CommonConfig.RELATION_NAME, DenaResponse.class);

        /////////////////////////////////////////////
        //            Assert Delete Relation
        /////////////////////////////////////////////
        TestDenaResponse expectedReturnObject = new TestDenaResponse();
        expectedReturnObject.httpStatusCode = 200;
        expectedReturnObject.timestamp = (actualReturnObject.getTimestamp());
        expectedReturnObject.deleteRelationCount = 2L;

        assertTrue(isTimeEqualRegardlessOfSecond(actualReturnObject.getTimestamp(), Instant.now().toEpochMilli()));
        JSONAssert.assertEquals(createJSONFromObject(expectedReturnObject), createJSONFromObject(actualReturnObject), true);


        /////////////////////////////////////////////
        //            Check Deleted Object
        /////////////////////////////////////////////

        actualReturnObject = performFindRelationRequest(objectId3, CommonConfig.RELATION_NAME, 0, 1, DenaResponse.class);
        expectedReturnObject = new TestDenaResponse();
        expectedReturnObject.httpStatusCode = 200;
        expectedReturnObject.foundObjectCount = 0;
        expectedReturnObject.timestamp = actualReturnObject.getTimestamp();

        expectedReturnObject.setTestObjectResponseList(Collections.emptyList());

        assertTrue(isTimeEqualRegardlessOfSecond(actualReturnObject.getTimestamp(), Instant.now().toEpochMilli()));
        JSONAssert.assertEquals(createJSONFromObject(expectedReturnObject), createJSONFromObject(actualReturnObject), true);

    }

    @Test
    public void test_DeleteRelation_With_Object_When_Relation_Not_Exist() throws Exception {
        /////////////////////////////////////////////////////////////////////////
        //            Send Delete Relation - Invalid relation name
        /////////////////////////////////////////////////////////////////////////
        DenaResponse actualReturnObject = performDeleteRelationWithObject(objectId3, "not_exist_relation", 200, objectId1, DenaResponse.class);

        TestDenaResponse expectedReturnObject = new TestDenaResponse();
        expectedReturnObject.httpStatusCode = 200;
        expectedReturnObject.timestamp = (actualReturnObject.getTimestamp());
        expectedReturnObject.deleteRelationCount = 0L;

        JSONAssert.assertEquals(createJSONFromObject(expectedReturnObject), createJSONFromObject(actualReturnObject), true);

    }


}
