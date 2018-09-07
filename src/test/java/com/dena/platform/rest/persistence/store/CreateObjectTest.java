package com.dena.platform.rest.persistence.store;

import com.dena.platform.core.feature.persistence.RelationType;
import com.dena.platform.rest.dto.*;
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
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * @author Javad Alimohammadi [<bs.alimohammadi@gmail.com>]
 */
@TestPropertySource(properties = {"dena.api.security.enabled=false"})
public class CreateObjectTest extends AbstractDataStoreTest {

    @Test
    public void test_CreateObject() throws Exception {

        /////////////////////////////////////////////
        //           Send Create Object Request
        /////////////////////////////////////////////
        TestRequestObject requestObject = new TestRequestObject();
        requestObject.addProperty("name", "javad");
        requestObject.addProperty("job", "developer");
        requestObject.addProperty("actor_user", this.user.getEmail());

        TestDenaRelation testDenaRelation = new TestDenaRelation();
        testDenaRelation.setRelationType(RelationType.RELATION_1_TO_1.value);
        testDenaRelation.setRelationName("new_relation");
        testDenaRelation.setIds(Collections.singletonList(objectId1));
        testDenaRelation.setTargetName(CommonConfig.TABLE_NAME);

        requestObject.addRelatedObject(testDenaRelation);

        DenaResponse actualReturnObject = performCreateObject(createJSONFromObject(requestObject), DenaResponse.class);

        /////////////////////////////////////////////
        //            Assert Create Object Response
        /////////////////////////////////////////////
        TestObjectResponse expectedObjectResponse = new TestObjectResponse();
        expectedObjectResponse.objectId = actualReturnObject.getDenaObjectResponseList().get(0).getObjectId();
        expectedObjectResponse.createTime = actualReturnObject.getDenaObjectResponseList().get(0).getCreateTime();
        expectedObjectResponse.updateTime = actualReturnObject.getDenaObjectResponseList().get(0).getUpdateTime();
        expectedObjectResponse.objectURI = "/" + CommonConfig.TABLE_NAME + "/" + expectedObjectResponse.objectId;
        expectedObjectResponse.addProperty("name", "javad");
        expectedObjectResponse.addProperty("job", "developer");


        TestDenaResponse expectedReturnObject = new TestDenaResponse();
        expectedReturnObject.timestamp = actualReturnObject.getTimestamp();
        expectedReturnObject.createObjectCount = 1L;
        expectedReturnObject.setTestObjectResponseList(Collections.singletonList(expectedObjectResponse));

        // assert timestamp
        assertTrue(isTimeEqualRegardlessOfSecond(expectedReturnObject.timestamp, Instant.now().toEpochMilli()));
        assertTrue(isTimeEqualRegardlessOfSecond(expectedObjectResponse.createTime, Instant.now().toEpochMilli()));
        assertNull("mergeUpdate time in creating object should be null", expectedObjectResponse.updateTime);

        JSONAssert.assertEquals(createJSONFromObject(expectedReturnObject), createJSONFromObject(actualReturnObject), false);

    }

    @Test
    public void test_CreateBulkObject() throws Exception {

        /////////////////////////////////////////////
        //           Send Create Object Request
        /////////////////////////////////////////////
        TestRequestObject requestObject1 = new TestRequestObject();
        requestObject1.addProperty("name", "javad");
        requestObject1.addProperty("job", "developer");
        requestObject1.addProperty("actor_user", this.user.getEmail());

        TestDenaRelation testDenaRelation = new TestDenaRelation();
        testDenaRelation.setRelationType(RelationType.RELATION_1_TO_1.value);
        testDenaRelation.setRelationName("new_relation");
        testDenaRelation.setIds(Collections.singletonList(objectId1));
        testDenaRelation.setTargetName(CommonConfig.TABLE_NAME);

        requestObject1.addRelatedObject(testDenaRelation);

        TestRequestObject requestObject2 = new TestRequestObject();
        requestObject2.addProperty("name", "abdolah");
        requestObject2.addProperty("job", "developer");

        DenaResponse actualReturnObject = performCreateObject(createJSONFromObject(Arrays.asList(requestObject1, requestObject2)), DenaResponse.class);

        /////////////////////////////////////////////
        //            Assert Create Object Response
        /////////////////////////////////////////////
        TestObjectResponse expectedObjectResponse1 = new TestObjectResponse();
        expectedObjectResponse1.objectId = actualReturnObject.getDenaObjectResponseList().get(0).getObjectId();
        expectedObjectResponse1.createTime = actualReturnObject.getDenaObjectResponseList().get(0).getCreateTime();
        expectedObjectResponse1.updateTime = actualReturnObject.getDenaObjectResponseList().get(0).getUpdateTime();
        expectedObjectResponse1.objectURI = "/" + CommonConfig.TABLE_NAME + "/" + expectedObjectResponse1.objectId;
        expectedObjectResponse1.addProperty("name", "javad");
        expectedObjectResponse1.addProperty("job", "developer");

        TestObjectResponse expectedObjectResponse2 = new TestObjectResponse();
        expectedObjectResponse2.objectId = actualReturnObject.getDenaObjectResponseList().get(1).getObjectId();
        expectedObjectResponse2.createTime = actualReturnObject.getDenaObjectResponseList().get(1).getCreateTime();
        expectedObjectResponse2.updateTime = actualReturnObject.getDenaObjectResponseList().get(1).getUpdateTime();
        expectedObjectResponse2.objectURI = "/" + CommonConfig.TABLE_NAME + "/" + expectedObjectResponse2.objectId;
        expectedObjectResponse2.addProperty("name", "abdolah");
        expectedObjectResponse2.addProperty("job", "developer");


        TestDenaResponse expectedReturnObject = new TestDenaResponse();
        expectedReturnObject.timestamp = actualReturnObject.getTimestamp();
        expectedReturnObject.createObjectCount = 2L;
        expectedReturnObject.setTestObjectResponseList(Arrays.asList(expectedObjectResponse1, expectedObjectResponse2));

        // assert timestamp
        assertTrue(isTimeEqualRegardlessOfSecond(expectedReturnObject.timestamp, Instant.now().toEpochMilli()));
        assertTrue(isTimeEqualRegardlessOfSecond(expectedObjectResponse1.createTime, Instant.now().toEpochMilli()));
        assertNull("mergeUpdate time in creating object should be null", expectedObjectResponse1.updateTime);

        JSONAssert.assertEquals(createJSONFromObject(expectedReturnObject), createJSONFromObject(actualReturnObject), false);

    }

    @Test
    public void test_CreateObject_When_Relation_Is_Invalid() throws Exception {
        /////////////////////////////////////////////////////////////////
        //           Send Create Object Request - relation not exist
        /////////////////////////////////////////////////////////////////
        TestRequestObject requestObject = new TestRequestObject();
        requestObject.addProperty("name", "javad");
        requestObject.addProperty("job", "developer");
        requestObject.addProperty("actor_user", this.user.getEmail());

        TestDenaRelation testDenaRelation = new TestDenaRelation();
        testDenaRelation.setRelationType(RelationType.RELATION_1_TO_1.value);
        testDenaRelation.setRelationName("new_relation");
        testDenaRelation.setIds(Collections.singletonList(objectId1));
        testDenaRelation.setTargetName("not_exist_target");

        requestObject.addRelatedObject(testDenaRelation);

        TestErrorResponse actualReturnObject = performCreateObject(createJSONFromObject(requestObject), 400, TestErrorResponse.class);

        /////////////////////////////////////////////////////////////////
        //            Assert Create Object Response - relation not exist
        /////////////////////////////////////////////////////////////////
        TestErrorResponse expectedReturnObject = new TestErrorResponse();
        expectedReturnObject.status = 400;
        expectedReturnObject.errorCode = "2001";
        expectedReturnObject.messages = Collections.singletonList("Relation(s) is invalid");

        JSONAssert.assertEquals(createJSONFromObject(expectedReturnObject), createJSONFromObject(actualReturnObject), false);

    }


}
