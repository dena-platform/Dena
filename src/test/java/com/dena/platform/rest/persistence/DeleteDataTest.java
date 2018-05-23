package com.dena.platform.rest.persistence;

import com.dena.platform.rest.dto.TestDenaResponseDTO;
import com.dena.platform.rest.dto.TestErrorResponseDTO;
import com.dena.platform.rest.dto.TestObjectResponseDTO;
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
 * @author Javad Alimohammadi [<bs.alimohammadi@gmail.com>]
 */
public class DeleteDataTest extends AbstractDataStoreTest {

    @Test
    public void test_DeleteObjects() throws Exception {
        /////////////////////////////////////////////
        //            Send Delete Objects Request
        /////////////////////////////////////////////
        TestDenaResponseDTO actualReturnObject = performDeleteRequest(Arrays.asList(objectId1, objectId2, objectId3),
                user.getEmail(), 200, TestDenaResponseDTO.class);

        /////////////////////////////////////////////
        //            Assert Deleted Object
        /////////////////////////////////////////////
        TestDenaResponseDTO expectedReturnObject = new TestDenaResponseDTO();
        expectedReturnObject.timestamp = actualReturnObject.timestamp;
        expectedReturnObject.deleteObjectCount = 3L;

        // check timestamp field of returned object
        assertTrue(isTimeEqualRegardlessOfSecond(actualReturnObject.timestamp, Instant.now().toEpochMilli()));
        JSONAssert.assertEquals(createJSONFromObject(expectedReturnObject), createJSONFromObject(actualReturnObject), true);

    }


    @Test
    public void test_DeleteObject_When_Object_Id_Invalid() throws Exception {
        /////////////////////////////////////////////////////////////////////////
        //            Send Delete Object Request - Invalid object id format
        /////////////////////////////////////////////////////////////////////////
        String invalidObjectId = "5a1bd6176f";
        TestErrorResponseDTO actualReturnObject = performDeleteRequest(Collections.singletonList(invalidObjectId),
                user.getEmail(), 400, TestErrorResponseDTO.class);

        TestErrorResponseDTO expectedReturnObject = new TestErrorResponseDTO();
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

        TestDenaResponseDTO actualReturnObject = performDeleteRequest(Collections.singletonList(randomObjectId),
                user.getEmail(), 200, TestDenaResponseDTO.class);

        TestDenaResponseDTO expectedReturnObject = new TestDenaResponseDTO();
        expectedReturnObject.timestamp = actualReturnObject.timestamp;
        expectedReturnObject.deleteObjectCount = 0L;

        // check timestamp field of returned object
        assertTrue(isTimeEqualRegardlessOfSecond(actualReturnObject.timestamp, Instant.now().toEpochMilli()));
        JSONAssert.assertEquals(createJSONFromObject(expectedReturnObject), createJSONFromObject(actualReturnObject), true);
    }

    @Test
    public void test_DeleteObject_When_App_Id_Not_Exist() throws Exception {
        //////////////////////////////////////////////////////////////////
        //            Send Delete Object Request - app id not exist
        //////////////////////////////////////////////////////////////////
        TestDenaResponseDTO actualReturnObject = performDeleteRequest(Collections.singletonList(randomObjectId),
                user.getEmail(), "/v1/invalid_app_id/denaTestCollection/", 200, TestDenaResponseDTO.class);

        TestDenaResponseDTO expectedReturnObject = new TestDenaResponseDTO();
        expectedReturnObject.timestamp = (actualReturnObject.timestamp);
        expectedReturnObject.deleteObjectCount = (0L);

        // check timestamp field of returned object
        assertTrue(isTimeEqualRegardlessOfSecond(actualReturnObject.timestamp, Instant.now().toEpochMilli()));
        JSONAssert.assertEquals(createJSONFromObject(expectedReturnObject), createJSONFromObject(actualReturnObject), true);

    }


    @Test
    public void test_DeleteRelation_With_Object() throws Exception {

        /////////////////////////////////////////////
        //            Send Delete Relation
        /////////////////////////////////////////////
        TestDenaResponseDTO actualReturnObject = performDeleteRelationWithObject(objectId3, CommonConfig.RELATION_NAME, 200, objectId1, TestDenaResponseDTO.class);

        /////////////////////////////////////////////
        //            Assert Delete Relation
        /////////////////////////////////////////////
        TestDenaResponseDTO expectedReturnObject = new TestDenaResponseDTO();
        expectedReturnObject.timestamp = (actualReturnObject.timestamp);
        expectedReturnObject.deleteRelationCount = 1L;

        assertTrue(isTimeEqualRegardlessOfSecond(actualReturnObject.timestamp, Instant.now().toEpochMilli()));
        JSONAssert.assertEquals(createJSONFromObject(expectedReturnObject), createJSONFromObject(actualReturnObject), true);


        /////////////////////////////////////////////
        //            Check Deleted Object
        /////////////////////////////////////////////
        actualReturnObject = performFindRelationRequest(objectId3, CommonConfig.RELATION_NAME, 0, 1);
        expectedReturnObject = new TestDenaResponseDTO();
        expectedReturnObject.foundObjectCount = 1L;
        expectedReturnObject.timestamp = actualReturnObject.timestamp;

        TestObjectResponseDTO testObjectResponseDTO = new TestObjectResponseDTO();
        testObjectResponseDTO.objectId = objectId2;
        testObjectResponseDTO.objectURI = "/" + CommonConfig.COLLECTION_NAME + "/" + objectId2;
        testObjectResponseDTO.addProperty("name", "javad");
        testObjectResponseDTO.addProperty("job", "developer");
        expectedReturnObject.setTestObjectResponseDTOList(Collections.singletonList(testObjectResponseDTO));

        assertTrue(isTimeEqualRegardlessOfSecond(actualReturnObject.timestamp, Instant.now().toEpochMilli()));
        JSONAssert.assertEquals(createJSONFromObject(expectedReturnObject), createJSONFromObject(actualReturnObject), true);

    }

    @Test
    public void test_DeleteRelation_With_Object_When_Object_Id_Invalid() throws Exception {
        /////////////////////////////////////////////////////////////////////////
        //            Send Delete Relation - Invalid object id format
        /////////////////////////////////////////////////////////////////////////
        String invalidObjectId = "5a1bd6176f";
        TestErrorResponseDTO actualReturnObject = performDeleteRelationWithObject(invalidObjectId, CommonConfig.COLLECTION_NAME, 400, objectId1, TestErrorResponseDTO.class);

        TestErrorResponseDTO expectedReturnObject = new TestErrorResponseDTO();
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
        TestDenaResponseDTO actualReturnObject = performDeleteRelation(objectId3, CommonConfig.RELATION_NAME);

        /////////////////////////////////////////////
        //            Assert Delete Relation
        /////////////////////////////////////////////
        TestDenaResponseDTO expectedReturnObject = new TestDenaResponseDTO();
        expectedReturnObject.timestamp = (actualReturnObject.timestamp);
        expectedReturnObject.deleteRelationCount = 2L;

        assertTrue(isTimeEqualRegardlessOfSecond(actualReturnObject.timestamp, Instant.now().toEpochMilli()));
        JSONAssert.assertEquals(createJSONFromObject(expectedReturnObject), createJSONFromObject(actualReturnObject), true);


        /////////////////////////////////////////////
        //            Check Deleted Object
        /////////////////////////////////////////////

        actualReturnObject = performFindRelationRequest(objectId3, CommonConfig.RELATION_NAME, 0, 1);
        expectedReturnObject = new TestDenaResponseDTO();
        expectedReturnObject.foundObjectCount = 0L;
        expectedReturnObject.timestamp = actualReturnObject.timestamp;

        expectedReturnObject.setTestObjectResponseDTOList(Collections.emptyList());

        assertTrue(isTimeEqualRegardlessOfSecond(actualReturnObject.timestamp, Instant.now().toEpochMilli()));
        JSONAssert.assertEquals(createJSONFromObject(expectedReturnObject), createJSONFromObject(actualReturnObject), true);

    }

    @Test
    public void test_DeleteRelation_With_Object_When_Relation_Not_Exist() throws Exception {
        /////////////////////////////////////////////////////////////////////////
        //            Send Delete Relation - Invalid relation name
        /////////////////////////////////////////////////////////////////////////
        TestDenaResponseDTO actualReturnObject = performDeleteRelationWithObject(objectId3, "not_exist_relation", 200, objectId1, TestDenaResponseDTO.class);

        TestDenaResponseDTO expectedReturnObject = new TestDenaResponseDTO();
        expectedReturnObject.timestamp = (actualReturnObject.timestamp);
        expectedReturnObject.deleteRelationCount = 0L;

        JSONAssert.assertEquals(createJSONFromObject(expectedReturnObject), createJSONFromObject(actualReturnObject), true);

    }


}
