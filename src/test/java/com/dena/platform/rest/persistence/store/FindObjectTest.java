package com.dena.platform.rest.persistence.store;

import com.dena.platform.rest.dto.TestDenaResponse;
import com.dena.platform.rest.dto.TestObjectResponse;
import com.dena.platform.restapi.dto.response.DenaResponse;
import com.dena.platform.utils.CommonConfig;
import org.junit.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;
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
public class FindObjectTest extends AbstractDataStoreTest {

    @Test
    public void test_FindObject_By_Id_When_Object_Exist() throws Exception {

        /////////////////////////////////////////////
        //            Send Find Object Request
        /////////////////////////////////////////////
        DenaResponse actualReturnObject = performFindRequestByObjectId(objectId3, DenaResponse.class);

        TestDenaResponse expectedReturnObject = new TestDenaResponse();
        expectedReturnObject.httpStatusCode = 200;
        expectedReturnObject.foundObjectCount = 1;
        expectedReturnObject.timestamp = actualReturnObject.getTimestamp();

        /////////////////////////////////////////////
        //            Assert Found Object
        /////////////////////////////////////////////   
        TestObjectResponse testObjectResponse = new TestObjectResponse();
        testObjectResponse.objectId = objectId3;
        testObjectResponse.objectURI = "/" + CommonConfig.TABLE_NAME + "/" + objectId3;
        testObjectResponse.addProperty("name", "javad");
        testObjectResponse.addProperty("job", "developer");
        expectedReturnObject.setTestObjectResponseList(Collections.singletonList(testObjectResponse));

        // check timestamp field of returned object
        assertTrue(isTimeEqualRegardlessOfSecond(actualReturnObject.getTimestamp(), Instant.now().toEpochMilli()));
        JSONAssert.assertEquals(createJSONFromObject(expectedReturnObject), createJSONFromObject(actualReturnObject), true);

    }

    @Test
    public void test_FindObjects_In_Table() throws Exception {
        /////////////////////////////////////////////
        //            Send Find Object Request
        /////////////////////////////////////////////
        DenaResponse actualReturnObject1 = performFindRequestInTable(CommonConfig.TABLE_NAME, 0, 2, DenaResponse.class);
        DenaResponse actualReturnObject2 = performFindRequestInTable(CommonConfig.TABLE_NAME, 4, 5, DenaResponse.class);

        TestDenaResponse expectedReturnObject1 = new TestDenaResponse();
        expectedReturnObject1.httpStatusCode = 200;
        expectedReturnObject1.foundObjectCount = 2;
        expectedReturnObject1.timestamp = actualReturnObject1.getTimestamp();

        TestDenaResponse expectedReturnObject2 = new TestDenaResponse();
        expectedReturnObject2.httpStatusCode = 200;
        expectedReturnObject2.foundObjectCount = 5;
        expectedReturnObject2.timestamp = actualReturnObject2.getTimestamp();


        /////////////////////////////////////////////
        //            Assert Found Object
        /////////////////////////////////////////////

        // Expected result for request 1
        TestObjectResponse testObjectResponse1 = new TestObjectResponse();
        testObjectResponse1.objectId = objectId1;
        testObjectResponse1.objectURI = "/" + CommonConfig.TABLE_NAME + "/" + objectId1;
        testObjectResponse1.addProperty("name", "javad");
        testObjectResponse1.addProperty("job", "developer");

        TestObjectResponse testObjectResponse2 = new TestObjectResponse();
        testObjectResponse2.objectId = objectId2;
        testObjectResponse2.objectURI = "/" + CommonConfig.TABLE_NAME + "/" + objectId2;
        testObjectResponse2.addProperty("name", "javad");
        testObjectResponse2.addProperty("job", "developer");

        expectedReturnObject1.setTestObjectResponseList(Arrays.asList(testObjectResponse1, testObjectResponse2));

        // Expected result for request 2
        TestObjectResponse testObjectResponse3 = new TestObjectResponse();
        testObjectResponse3.objectId = objectId5;
        testObjectResponse3.objectURI = "/" + CommonConfig.TABLE_NAME + "/" + objectId5;
        testObjectResponse3.addProperty("name", "javad");
        testObjectResponse3.addProperty("job", "developer");

        TestObjectResponse testObjectResponse4 = new TestObjectResponse();
        testObjectResponse4.objectId = objectId6;
        testObjectResponse4.objectURI = "/" + CommonConfig.TABLE_NAME + "/" + objectId6;
        testObjectResponse4.addProperty("name", "javad");
        testObjectResponse4.addProperty("job", "developer");

        TestObjectResponse testObjectResponse5 = new TestObjectResponse();
        testObjectResponse5.objectId = objectId7;
        testObjectResponse5.objectURI = "/" + CommonConfig.TABLE_NAME + "/" + objectId7;
        testObjectResponse5.addProperty("name", "javad");
        testObjectResponse5.addProperty("job", "developer");

        TestObjectResponse testObjectResponse6 = new TestObjectResponse();
        testObjectResponse6.objectId = objectId8;
        testObjectResponse6.objectURI = "/" + CommonConfig.TABLE_NAME + "/" + objectId8;
        testObjectResponse6.addProperty("name", "javad");
        testObjectResponse6.addProperty("job", "developer");

        TestObjectResponse testObjectResponse7 = new TestObjectResponse();
        testObjectResponse7.objectId = objectId9;
        testObjectResponse7.objectURI = "/" + CommonConfig.TABLE_NAME + "/" + objectId9;
        testObjectResponse7.addProperty("name", "javad");
        testObjectResponse7.addProperty("job", "developer");

        expectedReturnObject2.setTestObjectResponseList(Arrays.asList(
                testObjectResponse3, testObjectResponse4, testObjectResponse5,
                testObjectResponse6, testObjectResponse7
        ));


        // check timestamp field of returned object
        assertTrue(isTimeEqualRegardlessOfSecond(actualReturnObject1.getTimestamp(), Instant.now().toEpochMilli()));
        assertTrue(isTimeEqualRegardlessOfSecond(actualReturnObject2.getTimestamp(), Instant.now().toEpochMilli()));

        JSONAssert.assertEquals(createJSONFromObject(expectedReturnObject1), createJSONFromObject(actualReturnObject1), JSONCompareMode.NON_EXTENSIBLE);
        JSONAssert.assertEquals(createJSONFromObject(expectedReturnObject2), createJSONFromObject(actualReturnObject2), JSONCompareMode.NON_EXTENSIBLE);

    }


    @Test
    public void test_FindRelatedObject() throws Exception {
        /////////////////////////////////////////////
        //            Send Find Object Request
        /////////////////////////////////////////////
        DenaResponse actualReturnObject = performFindRelationRequest(objectId3, CommonConfig.RELATION_NAME, 0, 2, DenaResponse.class);

        /////////////////////////////////////////////
        //            Assert Found Object
        /////////////////////////////////////////////
        TestDenaResponse expectedReturnObject = new TestDenaResponse();
        expectedReturnObject.httpStatusCode = 200;
        expectedReturnObject.foundObjectCount = 2;
        expectedReturnObject.timestamp = actualReturnObject.getTimestamp();


        TestObjectResponse testObjectResponse1 = new TestObjectResponse();
        testObjectResponse1.objectId = objectId1;
        testObjectResponse1.objectURI = "/" + CommonConfig.TABLE_NAME + "/" + objectId1;
        testObjectResponse1.addProperty("name", "javad");
        testObjectResponse1.addProperty("job", "developer");

        TestObjectResponse testObjectResponse2 = new TestObjectResponse();
        testObjectResponse2.objectId = objectId2;
        testObjectResponse2.objectURI = "/" + CommonConfig.TABLE_NAME + "/" + objectId2;
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
        DenaResponse actualReturnObject = performFindRequestByObjectId(randomObjectId, DenaResponse.class);

        /////////////////////////////////////////////
        //            Assert Found Object
        /////////////////////////////////////////////
        TestDenaResponse expectedReturnObject = new TestDenaResponse();
        expectedReturnObject.httpStatusCode = 200;
        expectedReturnObject.foundObjectCount = 0;
        expectedReturnObject.timestamp = actualReturnObject.getTimestamp();

        // check timestamp field of returned object
        assertTrue(isTimeEqualRegardlessOfSecond(actualReturnObject.getTimestamp(), Instant.now().toEpochMilli()));
        JSONAssert.assertEquals(createJSONFromObject(expectedReturnObject), createJSONFromObject(actualReturnObject), JSONCompareMode.NON_EXTENSIBLE);

    }

}
