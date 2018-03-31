package com.dena.platform.rest.security;

import com.dena.platform.core.feature.security.SecurityUtil;
import com.dena.platform.core.feature.user.domain.User;
import com.dena.platform.core.feature.user.service.DenaUserManagement;
import com.dena.platform.rest.dto.*;
import com.dena.platform.rest.persistence.AbstractDataStoreTest;
import com.dena.platform.rest.user.UserManagement;
import com.dena.platform.restapi.dto.response.DenaResponse;
import com.dena.platform.restapi.dto.response.TokenGenResponse;
import com.dena.platform.utils.CommonConfig;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.time.Instant;
import java.util.Collections;

import static com.dena.platform.utils.JSONMapper.createJSONFromObject;
import static com.dena.platform.utils.TestUtils.isTimeEqualRegardlessOfSecond;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * @author Javad Alimohammadi [<bs.alimohammadi@yahoo.com>]
 */

public class LoginUserTest extends AbstractDataStoreTest {
    private final User user = new User();

    @Resource
    private DenaUserManagement userManagement;

    @Before
    public void setUp() throws Exception {
        user.setEmail("ali@hotmail.com");
        user.setUnencodedPassword("123");
        user.setPassword(SecurityUtil.encodePassword("123"));

        userManagement.registerUser(CommonConfig.APP_ID, user);
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

        DenaResponse actualReturnObject = performCreateObjectWithToken(createJSONFromObject(requestObject),
                DenaResponse.class,
                tokenResp.getToken());

        assertNotNull(actualReturnObject);
    }
}
