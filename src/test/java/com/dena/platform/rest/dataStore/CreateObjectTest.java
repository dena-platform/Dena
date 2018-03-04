package com.dena.platform.rest.dataStore;

import com.dena.platform.rest.dto.*;
import com.dena.platform.restapi.dto.response.DenaResponse;
import com.dena.platform.utils.CommonConfig;
import org.junit.Test;
import org.skyscreamer.jsonassert.JSONAssert;

import java.time.Instant;
import java.util.Collections;

import static com.dena.platform.utils.JSONMapper.createJSONFromObject;
import static com.dena.platform.utils.TestUtils.isTimeEqualRegardlessOfSecond;
import static org.junit.Assert.assertTrue;

/**
 * @author Javad Alimohammadi [<bs.alimohammadi@yahoo.com>]
 */
public class CreateObjectTest extends AbstractDataStoreTest {
    @Test
    public void test_CreateObject() throws Exception {

        /////////////////////////////////////////////
        //           Send Create Object Request
        /////////////////////////////////////////////
        TestRequestObject requestObject = new TestRequestObject();
        requestObject.addProperty("name", "javad");
        requestObject.addProperty("job", "developer");
//        requestObject.getRelatedObjects().add(new TestRelatedObject(objectId1, CommonConfig.COLLECTION_NAME));

        DenaResponse actualReturnObject = performCreateObject(createJSONFromObject(requestObject), DenaResponse.class);

        /////////////////////////////////////////////
        //            Assert Create Object Response
        /////////////////////////////////////////////
        TestObjectResponse expectedObjectResponse = new TestObjectResponse();
        expectedObjectResponse.objectURI = "/" + CommonConfig.APP_ID + "/" + CommonConfig.COLLECTION_NAME;
        expectedObjectResponse.createTime =actualReturnObject.get
                expectedObjectResponse.addProperty("name", "javad");
        expectedObjectResponse.addProperty("job", "developer");

//        expectedObjectResponse.testRelatedObjects = Collections.singletonList(new TestRelatedObject(objectId1, CommonConfig.COLLECTION_NAME));


        TestDenaResponse expectedReturnObject = new TestDenaResponse();
        expectedReturnObject.setTimestamp(actualReturnObject.getTimestamp());
        expectedReturnObject.setCount(1L);
        expectedReturnObject.setTestObjectResponseList(Collections.singletonList(expectedObjectResponse));

        assertTrue(isTimeEqualRegardlessOfSecond(actualReturnObject.getTimestamp(), Instant.now().toEpochMilli()));
        JSONAssert.assertEquals(createJSONFromObject(expectedReturnObject), createJSONFromObject(actualReturnObject), false);

    }


    @Test
    public void test_CreateObject_When_Relation_Is_Invalid() throws Exception {
        /////////////////////////////////////////////////////////////////
        //           Send Create Object Request - relation not exist
        /////////////////////////////////////////////////////////////////
        TestRequestObject requestObject = new TestRequestObject();
        requestObject.addProperty("job", "new developer");
        requestObject.addProperty("name", "developer");
        requestObject.getRelatedObjects().add(new TestRelatedObject(objectId1, "not_exist_relation"));

        TestErrorResponse actualReturnObject = performCreateObject(createJSONFromObject(requestObject), 400, TestErrorResponse.class);

        /////////////////////////////////////////////////////////////////
        //            Assert Create Object Response - relation not exist
        /////////////////////////////////////////////////////////////////
        TestErrorResponse expectedReturnObject = new TestErrorResponse();
        expectedReturnObject.status = 400;
        expectedReturnObject.errorCode = "2001";
        expectedReturnObject.messages = Collections.singletonList("relation(s) is invalid");

        JSONAssert.assertEquals(createJSONFromObject(expectedReturnObject), createJSONFromObject(actualReturnObject), false);


    }


}
