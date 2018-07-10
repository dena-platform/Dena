package com.dena.platform.rest.security;

import com.dena.platform.core.feature.security.service.JWTService;
import com.dena.platform.rest.dto.*;
import com.dena.platform.rest.persistence.AbstractDataStoreTest;
import com.dena.platform.restapi.dto.response.DenaResponse;
import com.dena.platform.utils.CommonConfig;
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
    public void loginUser() throws Exception {
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
    public void tryCreateObjectWithAnLoggedInUser() throws Exception {
        TokenGenResponse tokenResp = performLoginUser(createJSONFromObject(user), HttpStatus.OK, TokenGenResponse.class);

        TestRequestObjectDTO requestObject = new TestRequestObjectDTO();
        requestObject.addProperty("name", "reza");
        requestObject.addProperty("job", "developer");
        requestObject.addProperty("actor_user", this.user.getEmail());

        DenaResponse actualReturnObject = performCreateObjectWithToken(createJSONFromObject(requestObject), HttpStatus.OK.value(),
                DenaResponse.class,
                tokenResp.getToken());

        assertNotNull(actualReturnObject);
    }

    @Test
    public void twoLogInWillCreateNotEqualTokens() throws Exception {
        TokenGenResponse firstLogin = performLoginUser(createJSONFromObject(user), HttpStatus.OK, TokenGenResponse.class);
        TokenGenResponse secondLogin = performLoginUser(createJSONFromObject(user), HttpStatus.OK, TokenGenResponse.class);
        assertNotEquals(firstLogin.getToken(), secondLogin.getToken());
    }

    @Test
    public void performLogout_tokenIsNotValidAnyMore() throws Exception {
        TokenGenResponse loginResponse = performLoginUser(createJSONFromObject(user), HttpStatus.OK, TokenGenResponse.class);


        TokenGenResponse logoutResponse = performLogOutUser(createJSONFromObject(user),
                HttpStatus.OK,
                loginResponse.getToken(),
                TokenGenResponse.class);

        TestRequestObjectDTO requestObject = new TestRequestObjectDTO();
        requestObject.addProperty("name", "reza");
        requestObject.addProperty("job", "developer");
        requestObject.addProperty("actor_user", this.user.getEmail());

        DenaResponse actualReturnObject = performCreateObjectWithToken(createJSONFromObject(requestObject), HttpStatus.UNAUTHORIZED.value(),
                DenaResponse.class,
                loginResponse.getToken());

        assertNull(actualReturnObject);
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


}
