package com.dena.platform.core.feature.user.service;

import com.dena.platform.core.dto.DenaObject;
import com.dena.platform.core.feature.persistence.DenaDataStore;
import com.dena.platform.core.feature.persistence.DenaPager;
import com.dena.platform.core.feature.security.SecurityUtil;
import com.dena.platform.core.feature.user.domain.User;
import com.dena.platform.core.feature.user.exception.UserManagementException;
import com.dena.platform.utils.ObjectModelHelper;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;

import java.time.Instant;
import java.util.Arrays;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author Javad Alimohammadi<bs.alimohammadi@yahoo.com>
 */

public class DenaUserManagementImplTest {

    private DenaUserManagementImpl denaUserManagement;

    private final DenaDataStore mockedDenaStore = mock(DenaDataStore.class);

    private final String USER_TABLE_NAME = "user-table";

    private final String APP_ID = "Dena";

    @Before
    public void setUp() throws Exception {
        denaUserManagement = new DenaUserManagementImpl();
        denaUserManagement.setUserInfoTableName(USER_TABLE_NAME);


        // change security encoder to not encode password
        SecurityUtil.setPasswordEncoder(NoOpPasswordEncoder.getInstance());


        denaUserManagement.denaDataStore = mockedDenaStore;


    }

    @Test
    public void test_registerUser() {


        final User user = ObjectModelHelper.getSampleUser();

        // given
        DenaObject requestToStore = new DenaObject();
        requestToStore.addField("password", "123");
        requestToStore.addField("is_active", true);
        requestToStore.addField("email", "ali@hotmail.com");

        when(mockedDenaStore.store(APP_ID, USER_TABLE_NAME, requestToStore))
                .thenReturn(Arrays.asList(requestToStore));

        // when
        DenaObject responseDenaObject = denaUserManagement.registerUser(APP_ID, user);

        // then
        Assert.assertNotNull(responseDenaObject);
        Assert.assertEquals(responseDenaObject.getOtherFields().get("password"), "123");
        Assert.assertEquals(responseDenaObject.getOtherFields().get("is_active"), true);
        Assert.assertEquals(responseDenaObject.getOtherFields().get("email"), "ali@hotmail.com");

    }


    @Test
    public void test_registerUser_With_Bad_Input() {

        User user = ObjectModelHelper.getSampleUser();

        // invalid password
        try {
            user.setUnencodedPassword("");
            denaUserManagement.registerUser(APP_ID, user);
            Assert.fail(String.format("Empty password is not ok"));
        } catch (UserManagementException e) {

        }

        try {
            user.setUnencodedPassword(null);
            denaUserManagement.registerUser(APP_ID, user);
            Assert.fail(String.format("Null password is not ok"));
        } catch (UserManagementException e) {

        }

        // invalid email address

        try {
            user.setEmail(null);
            denaUserManagement.registerUser(APP_ID, user);
            Assert.fail(String.format("Null password is not ok"));
        } catch (UserManagementException e) {

        }

        try {
            user.setEmail("sdfdf");
            denaUserManagement.registerUser(APP_ID, user);
            Assert.fail(String.format("Null password is not ok"));
        } catch (UserManagementException e) {

        }

        // existing user
        DenaObject existingUser = new DenaObject();
        existingUser.setObjectId("123");
        existingUser.setObjectURI("/user-table/object1");
        existingUser.addField("email", "ali@hotmail.com");
        existingUser.setCreateTime(Instant.now().toEpochMilli());

        when(mockedDenaStore.findAll("Dena", "user-table", new DenaPager(0, 50)))
                .thenReturn(Arrays.asList(existingUser));

        user = ObjectModelHelper.getSampleUser();
        try {
            user.setUnencodedPassword("");
            denaUserManagement.registerUser(APP_ID, user);
            Assert.fail(String.format("Existing user is not ok"));
        } catch (UserManagementException e) {

        }
    }


}