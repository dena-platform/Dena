package com.dena.platform.rest.security;

import com.dena.platform.core.feature.security.SecurityUtil;
import com.dena.platform.core.feature.user.domain.User;
import com.dena.platform.core.feature.user.service.DenaUserManagement;
import com.dena.platform.rest.dto.TestRequestObjectDTO;
import com.dena.platform.rest.persistence.AbstractDataStoreTest;
import com.dena.platform.restapi.dto.response.DenaResponse;
import com.dena.platform.restapi.dto.response.TokenGenResponse;
import com.dena.platform.utils.CommonConfig;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpStatus;

import javax.annotation.Resource;

import static com.dena.platform.utils.JSONMapper.createJSONFromObject;
import static org.junit.Assert.*;

/**
 * @author Nazarpour.
 */

public class LoginUserTest extends AbstractDataStoreTest {
    private final User user = User.UserBuilder.anUser()
            .withEmail("ali@hotmail.com")
            .withPassword(SecurityUtil.encodePassword("123"))
            .withUnencodedPassword("123")
            .build();

    @Resource
    private DenaUserManagement userManagement;

    @Before
    public void setUp() throws Exception {
        userManagement.registerUser(CommonConfig.APP_ID, this.user);
    }

    @Test
    public void registerUserAndLogin() throws Exception {
        TokenGenResponse tokenResp = performLoginUser(createJSONFromObject(user), HttpStatus.OK, TokenGenResponse.class);
        assertNotNull(tokenResp);
        assertNotNull(tokenResp.getToken());
    }

    @Test
    public void tryCreateObjectWithAnLoggedInUser() throws Exception {
        TokenGenResponse tokenResp = performLoginUser(createJSONFromObject(user), HttpStatus.OK, TokenGenResponse.class);

        TestRequestObjectDTO requestObject = new TestRequestObjectDTO();
        requestObject.addProperty("name", "reza");
        requestObject.addProperty("job", "developer");

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

        DenaResponse actualReturnObject = performCreateObjectWithToken(createJSONFromObject(requestObject), HttpStatus.UNAUTHORIZED.value(),
                DenaResponse.class,
                loginResponse.getToken());

        assertNull(actualReturnObject);
    }

}
