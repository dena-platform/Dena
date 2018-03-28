package com.dena.platform.rest.security;

import com.dena.platform.core.feature.user.domain.User;
import com.dena.platform.rest.dto.*;
import com.dena.platform.rest.persistence.AbstractDataStoreTest;
import com.dena.platform.restapi.dto.response.DenaResponse;
import com.dena.platform.restapi.dto.response.TokenGenResponse;
import com.dena.platform.utils.CommonConfig;
import org.junit.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.http.HttpStatus;

import java.time.Instant;
import java.util.Collections;

import static com.dena.platform.utils.JSONMapper.createJSONFromObject;
import static com.dena.platform.utils.TestUtils.isTimeEqualRegardlessOfSecond;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * @author Javad Alimohammadi [<bs.alimohammadi@yahoo.com>]
 */
public class LoginUserTest extends AbstractDataStoreTest {

    @Test
    public void registerUserAndLogin() throws Exception {
        User user = new User();
        user.setEmail("ali@hotmail.com");
        user.setUnencodedPassword("123");

        DenaResponse response = performRegisterUser(createJSONFromObject(user), HttpStatus.OK, DenaResponse.class);

        TokenGenResponse tokenResp = performLoginUser(createJSONFromObject(user), HttpStatus.OK, TokenGenResponse.class);
        assertNotNull(tokenResp);
        assertNotNull(tokenResp.getToken());
    }

}
