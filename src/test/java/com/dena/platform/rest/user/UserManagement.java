package com.dena.platform.rest.user;

import com.dena.platform.rest.dto.TestDenaResponseDTO;
import com.dena.platform.rest.dto.TestErrorResponseDTO;
import com.dena.platform.rest.dto.TestObjectResponseDTO;
import com.dena.platform.rest.dto.TestRequestObjectDTO;
import com.dena.platform.rest.persistence.AbstractDataStoreTest;
import com.dena.platform.restapi.dto.response.DenaResponse;
import com.dena.platform.restapi.dto.response.ErrorResponse;
import org.junit.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.http.HttpStatus;

import java.time.Instant;
import java.util.Collections;

import static com.dena.platform.utils.JSONMapper.createJSONFromObject;
import static com.dena.platform.utils.TestUtils.isTimeEqualRegardlessOfSecond;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * @author Javad Alimohammadi [<bs.alimohammadi@yahoo.com>]
 */
public class UserManagement extends AbstractDataStoreTest {

    @Test
    public void test_RegisterUser() throws Exception {

        /////////////////////////////////////////////
        //           Send Update Object Request
        /////////////////////////////////////////////
        TestRequestObjectDTO requestObject = new TestRequestObjectDTO();
        requestObject.addProperty("email", "user2@denaplatform.com");
        requestObject.addProperty("password", "123456");
        requestObject.addProperty("name", "alex");
        requestObject.addProperty("family", "smith");

        DenaResponse actualReturnObject = performRegisterUser(createJSONFromObject(requestObject), HttpStatus.OK, DenaResponse.class);

        /////////////////////////////////////////////
        //            Assert Register User Response
        /////////////////////////////////////////////
        TestObjectResponseDTO expectedObjectResponse = new TestObjectResponseDTO();
        expectedObjectResponse.objectId = actualReturnObject.getDenaObjectResponseList().get(0).getObjectId();
        expectedObjectResponse.objectURI = "/DENA_USER/" + expectedObjectResponse.objectId;
        expectedObjectResponse.createTime = actualReturnObject.getDenaObjectResponseList().get(0).getCreateTime();
        expectedObjectResponse.updateTime = null;

        expectedObjectResponse.addProperty("password", "123456");
        expectedObjectResponse.addProperty("is_active", true);
        expectedObjectResponse.addProperty("email", "user2@denaplatform.com");
        expectedObjectResponse.addProperty("name", "alex");
        expectedObjectResponse.addProperty("family", "smith");


        TestDenaResponseDTO expectedReturnObject = new TestDenaResponseDTO();
        expectedReturnObject.timestamp = actualReturnObject.getTimestamp();
        expectedReturnObject.createObjectCount = 1L;
        expectedReturnObject.setTestObjectResponseDTOList(Collections.singletonList(expectedObjectResponse));

        // assert timestamp
        assertTrue(isTimeEqualRegardlessOfSecond(expectedReturnObject.timestamp, Instant.now().toEpochMilli()));
        assertTrue(isTimeEqualRegardlessOfSecond(expectedObjectResponse.createTime, Instant.now().toEpochMilli()));
        assertNull("update time in registering user should be null", expectedObjectResponse.updateTime);

        JSONAssert.assertEquals(createJSONFromObject(expectedReturnObject), createJSONFromObject(actualReturnObject), true);

    }

    @Test
    public void test_RegisterUser_When_Email_Is_Empty() throws Exception {

        /////////////////////////////////////////////
        //           Send Update Object Request
        /////////////////////////////////////////////
        TestRequestObjectDTO requestObject = new TestRequestObjectDTO();
        requestObject.addProperty("email", "");
        requestObject.addProperty("password", "123456");
        requestObject.addProperty("name", "alex");
        requestObject.addProperty("family", "smith");

        ErrorResponse actualReturnObject = performRegisterUser(createJSONFromObject(requestObject), HttpStatus.BAD_REQUEST, ErrorResponse.class);

        /////////////////////////////////////////////
        //            Assert Register User Response
        /////////////////////////////////////////////
        TestErrorResponseDTO expectedReturnObject = new TestErrorResponseDTO();
        expectedReturnObject.status = 400;
        expectedReturnObject.errorCode = "3001";
        expectedReturnObject.messages = Collections.singletonList("Email field value is invalid");

        JSONAssert.assertEquals(createJSONFromObject(expectedReturnObject), createJSONFromObject(actualReturnObject), false);

    }

    @Test
    public void test_RegisterUser_When_Email_Is_Invalid() throws Exception {

        /////////////////////////////////////////////
        //           Send Update Object Request
        /////////////////////////////////////////////
        TestRequestObjectDTO requestObject = new TestRequestObjectDTO();
        requestObject.addProperty("email", "dsdsf@d");
        requestObject.addProperty("password", "123456");
        requestObject.addProperty("name", "alex");
        requestObject.addProperty("family", "smith");

        ErrorResponse actualReturnObject = performRegisterUser(createJSONFromObject(requestObject), HttpStatus.BAD_REQUEST, ErrorResponse.class);

        /////////////////////////////////////////////
        //            Assert Register User Response
        /////////////////////////////////////////////
        TestErrorResponseDTO expectedReturnObject = new TestErrorResponseDTO();
        expectedReturnObject.status = 400;
        expectedReturnObject.errorCode = "3001";
        expectedReturnObject.messages = Collections.singletonList("Email field value is invalid");

        JSONAssert.assertEquals(createJSONFromObject(expectedReturnObject), createJSONFromObject(actualReturnObject), false);

    }


    @Test
    public void test_RegisterUser_When_Password_Is_Empty() throws Exception {

        /////////////////////////////////////////////
        //           Send Update Object Request
        /////////////////////////////////////////////
        TestRequestObjectDTO requestObject = new TestRequestObjectDTO();
        requestObject.addProperty("email", "user2@denaplatform.com");
        requestObject.addProperty("password", "");
        requestObject.addProperty("name", "alex");
        requestObject.addProperty("family", "smith");

        ErrorResponse actualReturnObject = performRegisterUser(createJSONFromObject(requestObject), HttpStatus.BAD_REQUEST, ErrorResponse.class);

        /////////////////////////////////////////////
        //            Assert Register User Response
        /////////////////////////////////////////////
        TestErrorResponseDTO expectedReturnObject = new TestErrorResponseDTO();
        expectedReturnObject.status = 400;
        expectedReturnObject.errorCode = "3002";
        expectedReturnObject.messages = Collections.singletonList("Password field is invalid");

        JSONAssert.assertEquals(createJSONFromObject(expectedReturnObject), createJSONFromObject(actualReturnObject), false);

    }

}
