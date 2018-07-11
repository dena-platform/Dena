package com.dena.platform.rest.security;

import com.dena.platform.core.feature.security.service.JWTService;
import com.dena.platform.rest.dto.TestDenaResponseDTO;
import com.dena.platform.rest.dto.TestObjectResponseDTO;
import com.dena.platform.rest.dto.TestRequestObjectDTO;
import com.dena.platform.rest.persistence.AbstractDataStoreTest;
import com.dena.platform.restapi.dto.response.DenaObjectResponse;
import com.dena.platform.restapi.dto.response.DenaResponse;
import com.dena.platform.utils.CommonConfig;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import javax.annotation.Resource;
import java.util.Collections;

import static com.dena.platform.utils.JSONMapper.createJSONFromObject;
import static com.dena.platform.utils.JSONMapper.createObjectFromJSON;
import static org.junit.Assert.*;

/**
 * @author Nazarpour.
 */

@ActiveProfiles("auth")
public class LoginUserTest extends AbstractDataStoreTest {
    @Resource
    private JWTService jwtService;

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
