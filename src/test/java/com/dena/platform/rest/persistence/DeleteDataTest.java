package com.dena.platform.rest.persistence;

import com.dena.platform.rest.dto.TestDenaResponseDTO;
import com.dena.platform.rest.dto.TestErrorResponseDTO;
import com.dena.platform.rest.dto.TestObjectResponseDTO;
import com.dena.platform.rest.dto.TestRelatedObjectDTO;
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
public class DeleteDataTest extends AbstractDataStoreTest {

    @Test
    public void test_DeleteObjects() throws Exception {
        /////////////////////////////////////////////
        //            Send Delete Objects Request
        /////////////////////////////////////////////
        TestDenaResponseDTO actualReturnObject = performDeleteRequest(Arrays.asList(objectId1, objectId2, objectId3), 200, TestDenaResponseDTO.class);

        /////////////////////////////////////////////
        //            Assert Deleted Object
        /////////////////////////////////////////////
        TestDenaResponseDTO expectedReturnObject = new TestDenaResponseDTO();
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
        TestErrorResponseDTO actualReturnObject = performDeleteRequest(Collections.singletonList(invalidObjectId), 400, TestErrorResponseDTO.class);

        TestErrorResponseDTO expectedReturnObject = new TestErrorResponseDTO();
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

        TestDenaResponseDTO actualReturnObject = performDeleteRequest(Collections.singletonList(randomObjectId), 200, TestDenaResponseDTO.class);

        TestDenaResponseDTO expectedReturnObject = new TestDenaResponseDTO();
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
        TestDenaResponseDTO actualReturnObject = performDeleteRequest(Collections.singletonList(randomObjectId), "/v1/invalid_app_id/denaTestCollection/", 200, TestDenaResponseDTO.class);

        TestDenaResponseDTO expectedReturnObject = new TestDenaResponseDTO();
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
        TestDenaResponseDTO actualReturnObject = performDeleteRelationWithObject(objectId3, CommonConfig.COLLECTION_NAME, 200, objectId1, TestDenaResponseDTO.class);

        /////////////////////////////////////////////
        //            Assert Delete Relation
        /////////////////////////////////////////////
        TestDenaResponseDTO expectedReturnObject = new TestDenaResponseDTO();
        expectedReturnObject.setTimestamp(actualReturnObject.getTimestamp());
        expectedReturnObject.setCount(1L);

        assertTrue(isTimeEqualRegardlessOfSecond(actualReturnObject.getTimestamp(), Instant.now().toEpochMilli()));
        JSONAssert.assertEquals(createJSONFromObject(expectedReturnObject), createJSONFromObject(actualReturnObject), true);

        /////////////////////////////////////////////
        //            Check Response Find
        /////////////////////////////////////////////
        actualReturnObject = performFindRequest(objectId3);
        expectedReturnObject = new TestDenaResponseDTO();
        expectedReturnObject.setCount(1L);
        expectedReturnObject.setTimestamp(actualReturnObject.getTimestamp());

        TestObjectResponseDTO testObjectResponseDTO = new TestObjectResponseDTO();
        testObjectResponseDTO.objectId = objectId3;
        testObjectResponseDTO.objectURI = "/" + CommonConfig.COLLECTION_NAME + "/" + objectId3;
        testObjectResponseDTO.addProperty("name", "javad");
        testObjectResponseDTO.addProperty("job", "developer");
        testObjectResponseDTO.testRelatedObjectDTOS = Collections.singletonList(new TestRelatedObjectDTO(objectId2, CommonConfig.COLLECTION_NAME));
        expectedReturnObject.setTestObjectResponseDTOList(Collections.singletonList(testObjectResponseDTO));

        assertTrue(isTimeEqualRegardlessOfSecond(actualReturnObject.getTimestamp(), Instant.now().toEpochMilli()));
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
        expectedReturnObject.messages = Arrays.asList("objectId is invalid");

        JSONAssert.assertEquals(createJSONFromObject(expectedReturnObject), createJSONFromObject(actualReturnObject), true);

    }

    @Test
    public void test_DeleteRelation() throws Exception {
        /////////////////////////////////////////////
        //            Send Delete Relation
        /////////////////////////////////////////////
        TestDenaResponseDTO actualReturnObject = performDeleteRelation(objectId3, CommonConfig.COLLECTION_NAME);

        /////////////////////////////////////////////
        //            Assert Delete Relation
        /////////////////////////////////////////////
        TestDenaResponseDTO expectedReturnObject = new TestDenaResponseDTO();
        expectedReturnObject.setTimestamp(actualReturnObject.getTimestamp());
        expectedReturnObject.setCount(2L);
        expectedReturnObject.setTimestamp(actualReturnObject.getTimestamp());

        assertTrue(isTimeEqualRegardlessOfSecond(actualReturnObject.getTimestamp(), Instant.now().toEpochMilli()));
        JSONAssert.assertEquals(createJSONFromObject(expectedReturnObject), createJSONFromObject(actualReturnObject), true);

        /////////////////////////////////////////////
        //            Check Response Find
        /////////////////////////////////////////////
        actualReturnObject = performFindRequest(objectId3);
        expectedReturnObject = new TestDenaResponseDTO();
        expectedReturnObject.setCount(1L);
        expectedReturnObject.setTimestamp(actualReturnObject.getTimestamp());

        TestObjectResponseDTO testObjectResponseDTO = new TestObjectResponseDTO();
        testObjectResponseDTO.objectId = objectId3;
        testObjectResponseDTO.objectURI = "/" + CommonConfig.COLLECTION_NAME + "/" + objectId3;
        testObjectResponseDTO.addProperty("name", "javad");
        testObjectResponseDTO.addProperty("job", "developer");
        expectedReturnObject.setTestObjectResponseDTOList(Collections.singletonList(testObjectResponseDTO));

        assertTrue(isTimeEqualRegardlessOfSecond(actualReturnObject.getTimestamp(), Instant.now().toEpochMilli()));
        JSONAssert.assertEquals(createJSONFromObject(expectedReturnObject), createJSONFromObject(actualReturnObject), true);

    }

    @Test
    public void test_DeleteRelation_With_Object_When_Relation_Not_Exist() throws Exception {
        /////////////////////////////////////////////////////////////////////////
        //            Send Delete Relation - Invalid relation name
        /////////////////////////////////////////////////////////////////////////
        TestDenaResponseDTO actualReturnObject = performDeleteRelationWithObject(objectId3, "not_exist_relation", 200, objectId1, TestDenaResponseDTO.class);

        TestDenaResponseDTO expectedReturnObject = new TestDenaResponseDTO();
        expectedReturnObject.setTimestamp(actualReturnObject.getTimestamp());
        expectedReturnObject.setCount(0L);

        JSONAssert.assertEquals(createJSONFromObject(expectedReturnObject), createJSONFromObject(actualReturnObject), true);

    }


}
