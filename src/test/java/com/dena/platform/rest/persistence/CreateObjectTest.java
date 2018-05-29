package com.dena.platform.rest.persistence;

import com.dena.platform.core.feature.persistence.RelationType;
import com.dena.platform.rest.dto.*;
import com.dena.platform.restapi.dto.response.DenaResponse;
import com.dena.platform.utils.CommonConfig;
import org.junit.Test;
import org.skyscreamer.jsonassert.JSONAssert;

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
public class CreateObjectTest extends AbstractDataStoreTest {

    @Test
    public void test_CreateObject() throws Exception {

        /////////////////////////////////////////////
        //           Send Create Object Request
        /////////////////////////////////////////////
        TestRequestObjectDTO requestObject = new TestRequestObjectDTO();
        requestObject.addProperty("name", "javad");
        requestObject.addProperty("job", "developer");
        requestObject.addProperty("actor_user", this.user.getEmail());

        TestDenaRelationDTO testDenaRelationDTO = new TestDenaRelationDTO();
        testDenaRelationDTO.setRelationType(RelationType.RELATION_1_TO_1.value);
        testDenaRelationDTO.setRelationName("new_relation");
        testDenaRelationDTO.setIds(Collections.singletonList(objectId1));
        testDenaRelationDTO.setTargetName(CommonConfig.COLLECTION_NAME);

        requestObject.addRelatedObject(testDenaRelationDTO);

        DenaResponse actualReturnObject = performCreateObject(createJSONFromObject(requestObject), DenaResponse.class);

        /////////////////////////////////////////////
        //            Assert Create Object Response
        /////////////////////////////////////////////
        TestObjectResponseDTO expectedObjectResponse = new TestObjectResponseDTO();
        expectedObjectResponse.objectId = actualReturnObject.getDenaObjectResponseList().get(0).getObjectId();
        expectedObjectResponse.createTime = actualReturnObject.getDenaObjectResponseList().get(0).getCreateTime();
        expectedObjectResponse.updateTime = actualReturnObject.getDenaObjectResponseList().get(0).getUpdateTime();
        expectedObjectResponse.objectURI = "/" + CommonConfig.COLLECTION_NAME + "/" + expectedObjectResponse.objectId;
        expectedObjectResponse.addProperty("name", "javad");
        expectedObjectResponse.addProperty("job", "developer");


        TestDenaResponseDTO expectedReturnObject = new TestDenaResponseDTO();
        expectedReturnObject.timestamp = actualReturnObject.getTimestamp();
        expectedReturnObject.createObjectCount = 1L;
        expectedReturnObject.setTestObjectResponseDTOList(Collections.singletonList(expectedObjectResponse));

        // assert timestamp
        assertTrue(isTimeEqualRegardlessOfSecond(expectedReturnObject.timestamp, Instant.now().toEpochMilli()));
        assertTrue(isTimeEqualRegardlessOfSecond(expectedObjectResponse.createTime, Instant.now().toEpochMilli()));
        assertNull("update time in creating object should be null", expectedObjectResponse.updateTime);

        JSONAssert.assertEquals(createJSONFromObject(expectedReturnObject), createJSONFromObject(actualReturnObject), false);

    }

    @Test
    public void test_CreateBulkObject() throws Exception {

        /////////////////////////////////////////////
        //           Send Create Object Request
        /////////////////////////////////////////////
        TestRequestObjectDTO requestObject1 = new TestRequestObjectDTO();
        requestObject1.addProperty("name", "javad");
        requestObject1.addProperty("job", "developer");
        requestObject1.addProperty("actor_user", this.user.getEmail());

        TestDenaRelationDTO testDenaRelationDTO = new TestDenaRelationDTO();
        testDenaRelationDTO.setRelationType(RelationType.RELATION_1_TO_1.value);
        testDenaRelationDTO.setRelationName("new_relation");
        testDenaRelationDTO.setIds(Collections.singletonList(objectId1));
        testDenaRelationDTO.setTargetName(CommonConfig.COLLECTION_NAME);

        requestObject1.addRelatedObject(testDenaRelationDTO);

        TestRequestObjectDTO requestObject2 = new TestRequestObjectDTO();
        requestObject2.addProperty("name", "abdolah");
        requestObject2.addProperty("job", "developer");

        DenaResponse actualReturnObject = performCreateObject(createJSONFromObject(Arrays.asList(requestObject1, requestObject2)), DenaResponse.class);

        /////////////////////////////////////////////
        //            Assert Create Object Response
        /////////////////////////////////////////////
        TestObjectResponseDTO expectedObjectResponse1 = new TestObjectResponseDTO();
        expectedObjectResponse1.objectId = actualReturnObject.getDenaObjectResponseList().get(0).getObjectId();
        expectedObjectResponse1.createTime = actualReturnObject.getDenaObjectResponseList().get(0).getCreateTime();
        expectedObjectResponse1.updateTime = actualReturnObject.getDenaObjectResponseList().get(0).getUpdateTime();
        expectedObjectResponse1.objectURI = "/" + CommonConfig.COLLECTION_NAME + "/" + expectedObjectResponse1.objectId;
        expectedObjectResponse1.addProperty("name", "javad");
        expectedObjectResponse1.addProperty("job", "developer");

        TestObjectResponseDTO expectedObjectResponse2 = new TestObjectResponseDTO();
        expectedObjectResponse2.objectId = actualReturnObject.getDenaObjectResponseList().get(1).getObjectId();
        expectedObjectResponse2.createTime = actualReturnObject.getDenaObjectResponseList().get(1).getCreateTime();
        expectedObjectResponse2.updateTime = actualReturnObject.getDenaObjectResponseList().get(1).getUpdateTime();
        expectedObjectResponse2.objectURI = "/" + CommonConfig.COLLECTION_NAME + "/" + expectedObjectResponse2.objectId;
        expectedObjectResponse2.addProperty("name", "abdolah");
        expectedObjectResponse2.addProperty("job", "developer");


        TestDenaResponseDTO expectedReturnObject = new TestDenaResponseDTO();
        expectedReturnObject.timestamp = actualReturnObject.getTimestamp();
        expectedReturnObject.createObjectCount = 2L;
        expectedReturnObject.setTestObjectResponseDTOList(Arrays.asList(expectedObjectResponse1, expectedObjectResponse2));

        // assert timestamp
        assertTrue(isTimeEqualRegardlessOfSecond(expectedReturnObject.timestamp, Instant.now().toEpochMilli()));
        assertTrue(isTimeEqualRegardlessOfSecond(expectedObjectResponse1.createTime, Instant.now().toEpochMilli()));
        assertNull("update time in creating object should be null", expectedObjectResponse1.updateTime);

        JSONAssert.assertEquals(createJSONFromObject(expectedReturnObject), createJSONFromObject(actualReturnObject), true);

    }

    @Test
    public void test_CreateObject_When_Relation_Is_Invalid() throws Exception {
        /////////////////////////////////////////////////////////////////
        //           Send Create Object Request - relation not exist
        /////////////////////////////////////////////////////////////////
        TestRequestObjectDTO requestObject = new TestRequestObjectDTO();
        requestObject.addProperty("name", "javad");
        requestObject.addProperty("job", "developer");
        requestObject.addProperty("actor_user", this.user.getEmail());

        TestDenaRelationDTO testDenaRelationDTO = new TestDenaRelationDTO();
        testDenaRelationDTO.setRelationType(RelationType.RELATION_1_TO_1.value);
        testDenaRelationDTO.setRelationName("new_relation");
        testDenaRelationDTO.setIds(Collections.singletonList(objectId1));
        testDenaRelationDTO.setTargetName("not_exist_target");

        requestObject.addRelatedObject(testDenaRelationDTO);

        TestErrorResponseDTO actualReturnObject = performCreateObject(createJSONFromObject(requestObject), 400, TestErrorResponseDTO.class);

        /////////////////////////////////////////////////////////////////
        //            Assert Create Object Response - relation not exist
        /////////////////////////////////////////////////////////////////
        TestErrorResponseDTO expectedReturnObject = new TestErrorResponseDTO();
        expectedReturnObject.status = 400;
        expectedReturnObject.errorCode = "2001";
        expectedReturnObject.messages = Collections.singletonList("Relation(s) is invalid");

        JSONAssert.assertEquals(createJSONFromObject(expectedReturnObject), createJSONFromObject(actualReturnObject), false);

    }


}
