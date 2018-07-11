package com.dena.platform.rest.user;

import com.dena.platform.core.feature.security.service.JWTService;
import com.dena.platform.rest.dto.TestDenaResponseDTO;
import com.dena.platform.rest.dto.TestErrorResponseDTO;
import com.dena.platform.rest.dto.TestObjectResponseDTO;
import com.dena.platform.rest.dto.TestRequestObjectDTO;
import com.dena.platform.rest.persistence.AbstractDataStoreTest;
import com.dena.platform.restapi.dto.response.DenaObjectResponse;
import com.dena.platform.restapi.dto.response.DenaResponse;
import com.dena.platform.restapi.dto.response.ErrorResponse;
import com.dena.platform.utils.CommonConfig;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import javax.annotation.Resource;
import java.time.Instant;
import java.util.Collections;

import static com.dena.platform.utils.JSONMapper.createJSONFromObject;
import static com.dena.platform.utils.JSONMapper.createObjectFromJSON;
import static com.dena.platform.utils.TestUtils.isTimeEqualRegardlessOfSecond;
import static org.junit.Assert.*;

/**
 * @author Javad Alimohammadi [<bs.alimohammadi@gmail.com>]
 */
public class UserManagement extends AbstractDataStoreTest {
    @Resource
    private JWTService jwtService;


    @Test
    public void test_RegisterUser() throws Exception {

        /////////////////////////////////////////////
        //           Send Register New User Request
        /////////////////////////////////////////////
        TestRequestObjectDTO requestObject = new TestRequestObjectDTO();
        requestObject.addProperty("email", "user1@dena-platform.com");
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

        expectedObjectResponse.addProperty("is_active", true);
        expectedObjectResponse.addProperty("email", "user1@dena-platform.com");
        expectedObjectResponse.addProperty("name", "alex");
        expectedObjectResponse.addProperty("family", "smith");


        TestDenaResponseDTO expectedReturnObject = new TestDenaResponseDTO();
        expectedReturnObject.timestamp = actualReturnObject.getTimestamp();
        expectedReturnObject.createUserCount = 1;
        expectedReturnObject.setTestObjectResponseDTOList(Collections.singletonList(expectedObjectResponse));

        // assert timestamp
        assertTrue(isTimeEqualRegardlessOfSecond(expectedReturnObject.timestamp, Instant.now().toEpochMilli()));
        assertTrue(isTimeEqualRegardlessOfSecond(expectedObjectResponse.createTime, Instant.now().toEpochMilli()));
        assertEquals("Create user count is not correct", expectedReturnObject.createUserCount,
                actualReturnObject.getCreateUserCount());
        assertNull("Update time in registering user should be null", expectedObjectResponse.updateTime);


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
    public void test_RegisterUser_When_Email_Is_Exist() throws Exception {

        /////////////////////////////////////////////
        //           Send Update Object Request
        /////////////////////////////////////////////
        TestRequestObjectDTO requestObject = new TestRequestObjectDTO();
        requestObject.addProperty("email", "user1@dena-platform.com");
        requestObject.addProperty("password", "123456");
        requestObject.addProperty("name", "alex");
        requestObject.addProperty("family", "smith");

        performRegisterUser(createJSONFromObject(requestObject), HttpStatus.OK, DenaResponse.class);

        ErrorResponse actualReturnObject = performRegisterUser(createJSONFromObject(requestObject), HttpStatus
                .BAD_REQUEST, ErrorResponse.class);

        /////////////////////////////////////////////
        //            Assert Register User Response
        /////////////////////////////////////////////
        TestErrorResponseDTO expectedReturnObject = new TestErrorResponseDTO();
        expectedReturnObject.status = 400;
        expectedReturnObject.errorCode = "3000";
        expectedReturnObject.messages = Collections.singletonList("User with this identity already exist");

        JSONAssert.assertEquals(createJSONFromObject(expectedReturnObject), createJSONFromObject(actualReturnObject), false);

    }

    @Test
    public void test_RegisterUser_When_Password_Is_Empty() throws Exception {

        /////////////////////////////////////////////
        //           Send Update Object Request
        /////////////////////////////////////////////
        TestRequestObjectDTO requestObject = new TestRequestObjectDTO();
        requestObject.addProperty("email", "user1@denaplatform.com");
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


    @Test
    public void test_loginUser() throws Exception {
        /////////////////////////////////////////////
        //           Send Login User Request
        /////////////////////////////////////////////

        DenaResponse actualReturnObject = performLoginUser(createJSONFromObject(user), HttpStatus.OK, DenaResponse.class);

        /////////////////////////////////////////////
        //            Assert Login User Response
        /////////////////////////////////////////////
        TestObjectResponseDTO expectedObjectResponse = new TestObjectResponseDTO();
        expectedObjectResponse.objectId = actualReturnObject.getDenaObjectResponseList().get(0).getObjectId();
        expectedObjectResponse.objectURI = "/DENA_USER/" + expectedObjectResponse.objectId;
        expectedObjectResponse.createTime = actualReturnObject.getDenaObjectResponseList().get(0).getCreateTime();
        expectedObjectResponse.addProperty("is_active", true);
        expectedObjectResponse.addProperty("email", "ali@hotmail.com");

        TestDenaResponseDTO expectedReturnObject = new TestDenaResponseDTO();
        expectedReturnObject.timestamp = actualReturnObject.getTimestamp();
        expectedReturnObject.foundObjectCount = 1;
        expectedReturnObject.setTestObjectResponseDTOList(Collections.singletonList(expectedObjectResponse));


        JSONAssert.assertEquals(createJSONFromObject(expectedReturnObject), createJSONFromObject(actualReturnObject), false);

        // assert token
        String token = String.valueOf(actualReturnObject.getDenaObjectResponseList().get(0).getFields().get("token"));
        assertTrue(jwtService.isTokenValid(token));

    }

    @Test
    public void test_CreateObject_When_User_Is_LoggedIn() throws Exception {
        /////////////////////////////////////////////////////////
        //       Send Create Object Request With Logged_In User
        /////////////////////////////////////////////////////////

        DenaResponse loggedInResponse = performLoginUser(createJSONFromObject(user), HttpStatus.OK, DenaResponse.class);
        String token = String.valueOf(loggedInResponse.getDenaObjectResponseList().get(0).getFields().get("token"));

        TestRequestObjectDTO requestObject = new TestRequestObjectDTO();
        requestObject.addProperty("name", "reza");
        requestObject.addProperty("job", "developer");

        DenaResponse actualReturnObject = performCreateObjectWithToken(createJSONFromObject(requestObject), HttpStatus.OK,
                DenaResponse.class, token);

        DenaObjectResponse denaObjectResponse = actualReturnObject.getDenaObjectResponseList().get(0);
        /////////////////////////////////////////////
        //            Assert Create Object Response
        /////////////////////////////////////////////

        assertNotNull(actualReturnObject);
        assertEquals(denaObjectResponse.getFields().get("name"), "reza");
        assertEquals(denaObjectResponse.getFields().get("job"), "developer");
    }

    @Test
    public void test_logoutUser() throws Exception {
        /////////////////////////////////////////////////////////
        //       Send Logout Request
        /////////////////////////////////////////////////////////

        DenaResponse loggedInResponse = performLoginUser(createJSONFromObject(user), HttpStatus.OK, DenaResponse.class);
        String token = String.valueOf(loggedInResponse.getDenaObjectResponseList().get(0).getFields().get("token"));

        DenaResponse logoutResponse = performLogOutUser(createJSONFromObject(user), HttpStatus.OK, token, DenaResponse.class);
        DenaObjectResponse denaObjectResponse = logoutResponse.getDenaObjectResponseList().get(0);

        assertTrue(StringUtils.isNotBlank((String) denaObjectResponse.getFields().get("Message")));
    }


    /////////////////////////////////////////////
    //            USER MANAGEMENT REQUEST
    /////////////////////////////////////////////
    public <T> T performRegisterUser(String body, HttpStatus httpStatus, Class<T> klass) throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post(CommonConfig.REGISTER_USER_URL)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(body))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().is(httpStatus.value()))
                .andReturn();

        String returnContent = result.getResponse().getContentAsString();
        return createObjectFromJSON(returnContent, klass);

    }

    /////////////////////////////////////////////
    //            LOGIN
    /////////////////////////////////////////////
    protected <T> T performLoginUser(String body, HttpStatus httpStatus, Class<T> klass) throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post(CommonConfig.LOGIN_URL)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(body))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().is(httpStatus.value()))
                .andReturn();

        String returnContent = result.getResponse().getContentAsString();
        return createObjectFromJSON(returnContent, klass);

    }

    /////////////////////////////////////////////
    //            LOGOUT
    /////////////////////////////////////////////
    protected <T> T performLogOutUser(String body, HttpStatus httpStatus, String token, Class<T> klass) throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post(CommonConfig.LOGOUT_URL)
                .header("Authorization", token)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(body))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().is(httpStatus.value()))
                .andReturn();

        String returnContent = result.getResponse().getContentAsString();
        return createObjectFromJSON(returnContent, klass);

    }


    protected <T> T performCreateObjectWithToken(String body, HttpStatus httpStatus, Class<T> klass, String token) throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post(CommonConfig.BASE_URL)
                .header("Authorization", token)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(body))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().is(httpStatus.value()))
                .andReturn();

        String returnContent = result.getResponse().getContentAsString();
        if (!StringUtils.isEmpty(returnContent))
            return createObjectFromJSON(returnContent, klass);
        else
            return null;

    }


}
