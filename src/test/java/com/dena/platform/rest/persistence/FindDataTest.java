package com.dena.platform.rest.persistence;

import com.dena.platform.rest.dto.TestDenaResponseDTO;
import com.dena.platform.rest.dto.TestObjectResponseDTO;
import com.dena.platform.utils.CommonConfig;
import org.junit.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;

import java.time.Instant;
import java.util.Arrays;
import java.util.Collections;

import static com.dena.platform.utils.JSONMapper.createJSONFromObject;
import static com.dena.platform.utils.TestUtils.isTimeEqualRegardlessOfSecond;
import static org.junit.Assert.assertTrue;

/**
 * @author Javad Alimohammadi [<bs.alimohammadi@yahoo.com>]
 */
public class FindDataTest extends AbstractDataStoreTest {

    @Test
    public void test_FindObject_By_Id_When_Object_Exist() throws Exception {

        /////////////////////////////////////////////
        //            Send Find Object Request
        /////////////////////////////////////////////
        TestDenaResponseDTO actualReturnObject = performFindRequestByObjectId(objectId3);

        TestDenaResponseDTO expectedReturnObject = new TestDenaResponseDTO();
        expectedReturnObject.foundObjectCount = 1L;
        expectedReturnObject.timestamp = actualReturnObject.timestamp;

        /////////////////////////////////////////////
        //            Assert Found Object
        /////////////////////////////////////////////   
        TestObjectResponseDTO testObjectResponseDTO = new TestObjectResponseDTO();
        testObjectResponseDTO.objectId = objectId3;
        testObjectResponseDTO.objectURI = "/" + CommonConfig.COLLECTION_NAME + "/" + objectId3;
        testObjectResponseDTO.addProperty("name", "javad");
        testObjectResponseDTO.addProperty("job", "developer");
        expectedReturnObject.setTestObjectResponseDTOList(Collections.singletonList(testObjectResponseDTO));

        // check timestamp field of returned object
        assertTrue(isTimeEqualRegardlessOfSecond(actualReturnObject.timestamp, Instant.now().toEpochMilli()));
        JSONAssert.assertEquals(createJSONFromObject(expectedReturnObject), createJSONFromObject(actualReturnObject), true);

    }

    @Test
    public void test_FindObjects_In_Table() throws Exception {
        /////////////////////////////////////////////
        //            Send Find Object Request
        /////////////////////////////////////////////
        TestDenaResponseDTO actualReturnObject1 = performFindRequestInTable(CommonConfig.COLLECTION_NAME, 0, 2);
        TestDenaResponseDTO actualReturnObject2 = performFindRequestInTable(CommonConfig.COLLECTION_NAME, 4, 5);

        TestDenaResponseDTO expectedReturnObject1 = new TestDenaResponseDTO();
        expectedReturnObject1.foundObjectCount = 2L;
        expectedReturnObject1.timestamp = actualReturnObject1.timestamp;

        TestDenaResponseDTO expectedReturnObject2 = new TestDenaResponseDTO();
        expectedReturnObject2.foundObjectCount = 5L;
        expectedReturnObject2.timestamp = actualReturnObject2.timestamp;



        /////////////////////////////////////////////
        //            Assert Found Object
        /////////////////////////////////////////////

        // Expected result for request 1
        TestObjectResponseDTO testObjectResponseDTO1 = new TestObjectResponseDTO();
        testObjectResponseDTO1.objectId = objectId1;
        testObjectResponseDTO1.objectURI = "/" + CommonConfig.COLLECTION_NAME + "/" + objectId1;
        testObjectResponseDTO1.addProperty("name", "javad");
        testObjectResponseDTO1.addProperty("job", "developer");

        TestObjectResponseDTO testObjectResponseDTO2 = new TestObjectResponseDTO();
        testObjectResponseDTO2.objectId = objectId2;
        testObjectResponseDTO2.objectURI = "/" + CommonConfig.COLLECTION_NAME + "/" + objectId2;
        testObjectResponseDTO2.addProperty("name", "javad");
        testObjectResponseDTO2.addProperty("job", "developer");

        expectedReturnObject1.setTestObjectResponseDTOList(Arrays.asList(testObjectResponseDTO1, testObjectResponseDTO2));

        // Expected result for request 2
        TestObjectResponseDTO testObjectResponseDTO3 = new TestObjectResponseDTO();
        testObjectResponseDTO3.objectId = objectId5;
        testObjectResponseDTO3.objectURI = "/" + CommonConfig.COLLECTION_NAME + "/" + objectId5;
        testObjectResponseDTO3.addProperty("name", "javad");
        testObjectResponseDTO3.addProperty("job", "developer");

        TestObjectResponseDTO testObjectResponseDTO4 = new TestObjectResponseDTO();
        testObjectResponseDTO4.objectId = objectId6;
        testObjectResponseDTO4.objectURI = "/" + CommonConfig.COLLECTION_NAME + "/" + objectId6;
        testObjectResponseDTO4.addProperty("name", "javad");
        testObjectResponseDTO4.addProperty("job", "developer");

        TestObjectResponseDTO testObjectResponseDTO5 = new TestObjectResponseDTO();
        testObjectResponseDTO5.objectId = objectId7;
        testObjectResponseDTO5.objectURI = "/" + CommonConfig.COLLECTION_NAME + "/" + objectId7;
        testObjectResponseDTO5.addProperty("name", "javad");
        testObjectResponseDTO5.addProperty("job", "developer");

        TestObjectResponseDTO testObjectResponseDTO6 = new TestObjectResponseDTO();
        testObjectResponseDTO6.objectId = objectId8;
        testObjectResponseDTO6.objectURI = "/" + CommonConfig.COLLECTION_NAME + "/" + objectId8;
        testObjectResponseDTO6.addProperty("name", "javad");
        testObjectResponseDTO6.addProperty("job", "developer");

        TestObjectResponseDTO testObjectResponseDTO7 = new TestObjectResponseDTO();
        testObjectResponseDTO7.objectId = objectId9;
        testObjectResponseDTO7.objectURI = "/" + CommonConfig.COLLECTION_NAME + "/" + objectId9;
        testObjectResponseDTO7.addProperty("name", "javad");
        testObjectResponseDTO7.addProperty("job", "developer");

        expectedReturnObject2.setTestObjectResponseDTOList(Arrays.asList(
                testObjectResponseDTO3, testObjectResponseDTO4, testObjectResponseDTO5,
                testObjectResponseDTO6, testObjectResponseDTO7
        ));


        // check timestamp field of returned object
        assertTrue(isTimeEqualRegardlessOfSecond(actualReturnObject1.timestamp, Instant.now().toEpochMilli()));
        assertTrue(isTimeEqualRegardlessOfSecond(actualReturnObject2.timestamp, Instant.now().toEpochMilli()));

        JSONAssert.assertEquals(createJSONFromObject(expectedReturnObject1), createJSONFromObject(actualReturnObject1), JSONCompareMode.NON_EXTENSIBLE);
        JSONAssert.assertEquals(createJSONFromObject(expectedReturnObject2), createJSONFromObject(actualReturnObject2), JSONCompareMode.NON_EXTENSIBLE);

    }


    @Test
    public void test_FindRelatedObject() throws Exception {
        /////////////////////////////////////////////
        //            Send Find Object Request
        /////////////////////////////////////////////
        TestDenaResponseDTO actualReturnObject = performFindRelationRequest(objectId3, CommonConfig.RELATION_NAME);

        /////////////////////////////////////////////
        //            Assert Found Object
        /////////////////////////////////////////////
        TestDenaResponseDTO expectedReturnObject = new TestDenaResponseDTO();
        expectedReturnObject.foundObjectCount = 2L;
        expectedReturnObject.timestamp = actualReturnObject.timestamp;


        TestObjectResponseDTO testObjectResponseDTO1 = new TestObjectResponseDTO();
        testObjectResponseDTO1.objectId = objectId1;
        testObjectResponseDTO1.objectURI = "/" + CommonConfig.COLLECTION_NAME + "/" + objectId1;
        testObjectResponseDTO1.addProperty("name", "javad");
        testObjectResponseDTO1.addProperty("job", "developer");

        TestObjectResponseDTO testObjectResponseDTO2 = new TestObjectResponseDTO();
        testObjectResponseDTO2.objectId = objectId2;
        testObjectResponseDTO2.objectURI = "/" + CommonConfig.COLLECTION_NAME + "/" + objectId2;
        testObjectResponseDTO2.addProperty("name", "javad");
        testObjectResponseDTO2.addProperty("job", "developer");

        expectedReturnObject.setTestObjectResponseDTOList(Arrays.asList(testObjectResponseDTO1, testObjectResponseDTO2));


        // check timestamp field of returned object
        assertTrue(isTimeEqualRegardlessOfSecond(actualReturnObject.timestamp, Instant.now().toEpochMilli()));
        JSONAssert.assertEquals(createJSONFromObject(expectedReturnObject), createJSONFromObject(actualReturnObject), false);

    }

    @Test
    public void test_FindObject_When_Object_Not_Exist() throws Exception {

        /////////////////////////////////////////////
        //            Send Find Object Request
        /////////////////////////////////////////////
        TestDenaResponseDTO actualReturnObject = performFindRequestByObjectId(randomObjectId);

        /////////////////////////////////////////////
        //            Assert Found Object
        /////////////////////////////////////////////
        TestDenaResponseDTO expectedReturnObject = new TestDenaResponseDTO();
        expectedReturnObject.foundObjectCount = 0L;
        expectedReturnObject.timestamp = actualReturnObject.timestamp;

        // check timestamp field of returned object
        assertTrue(isTimeEqualRegardlessOfSecond(actualReturnObject.timestamp, Instant.now().toEpochMilli()));
        JSONAssert.assertEquals(createJSONFromObject(expectedReturnObject), createJSONFromObject(actualReturnObject), JSONCompareMode.NON_EXTENSIBLE);

    }

}
