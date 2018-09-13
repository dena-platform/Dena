package com.dena.platform.core.feature.user.service;

import com.dena.platform.core.dto.DenaObject;
import com.dena.platform.core.feature.persistence.DenaDataStore;
import com.dena.platform.core.feature.persistence.DenaPager;
import com.dena.platform.core.feature.security.SecurityUtil;
import com.dena.platform.core.feature.user.domain.User;
import com.dena.platform.core.feature.user.exception.UserManagementException;
import com.dena.platform.test.ObjectModelHelper;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;

import java.time.Instant;
import java.util.Arrays;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * @author Javad Alimohammadi<bs.alimohammadi@gmail.com>
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
    public void test_registerUser_with_bad_input() {

        User user = ObjectModelHelper.getSampleUser();

        // invalid password
        try {
            user.setUnencodedPassword("");
            denaUserManagement.registerUser(APP_ID, user);
            fail(String.format("Empty password is not ok"));
        } catch (UserManagementException e) {

        }

        try {
            user.setUnencodedPassword(null);
            denaUserManagement.registerUser(APP_ID, user);
            fail(String.format("Null password is not ok"));
        } catch (UserManagementException e) {

        }

        // invalid email address

        try {
            user.setEmail(null);
            denaUserManagement.registerUser(APP_ID, user);
            fail(String.format("Null password is not ok"));
        } catch (UserManagementException e) {

        }

        try {
            user.setEmail("sdfdf");
            denaUserManagement.registerUser(APP_ID, user);
            fail(String.format("Null password is not ok"));
        } catch (UserManagementException e) {

        }

        // existing user
        DenaObject existingUser = new DenaObject();
        existingUser.setObjectId("123");
        existingUser.setObjectURI("/user-table/object1");
        existingUser.addField("email", "ali@hotmail.com");
        existingUser.setCreateTime(Instant.now().toEpochMilli());

        when(mockedDenaStore.findAll("Dena", "user-table", new DenaPager()))
                .thenReturn(Arrays.asList(existingUser));

        user = ObjectModelHelper.getSampleUser();
        try {
            denaUserManagement.registerUser(APP_ID, user);
            fail(String.format("Existing user is not ok"));
        } catch (UserManagementException e) {

        }
    }

    @Test
    public void test_isUserExist() {
        final String existingEmail = "ali@hotmail.com";
        final String notExistingEmail = "notexist@notexist.com";

        // existing user
        DenaObject existingUser = new DenaObject();
        existingUser.setObjectId("123");
        existingUser.setObjectURI("/user-table/object1");
        existingUser.addField("email", "ali@hotmail.com");
        existingUser.setCreateTime(Instant.now().toEpochMilli());

        when(mockedDenaStore.findAll("Dena", "user-table", new DenaPager()))
                .thenReturn(Arrays.asList(existingUser));

        assertTrue("Email exist so return, so it must return true", denaUserManagement.isUserExist(APP_ID, existingEmail));
        assertFalse("Email not exist so return, so it must return false", denaUserManagement.isUserExist(APP_ID, notExistingEmail));
    }

    @Test
    public void test_findUserById() {
        final String existingEmail = "ali@hotmail.com";
        final String notExistingEmail = "notexist@notexist.com";

        // existing user
        DenaObject existingUser = new DenaObject();
        existingUser.setObjectId("123");
        existingUser.setObjectURI("/user-table/object1");
        existingUser.addField("email", "ali@hotmail.com");
        existingUser.setCreateTime(1535725446418L);
        existingUser.setUpdateTime(1535725446430L);

        when(mockedDenaStore.findAll("Dena", "user-table", new DenaPager()))
                .thenReturn(Arrays.asList(existingUser));

        User foundUser = denaUserManagement.findUserByEmailAddress(APP_ID, existingEmail);
        assertNotNull("A user should return in response", foundUser);
        Assert.assertEquals(foundUser.getObjectId(), "123");
        Assert.assertEquals(foundUser.getObjectURI(), "/user-table/object1");
        Assert.assertEquals(foundUser.getCreateTime(), Long.valueOf(1535725446418L));
        Assert.assertEquals(foundUser.getUpdateTime(), Long.valueOf(1535725446430L));
        Assert.assertEquals(foundUser.getOtherFields().get("email"), "ali@hotmail.com");


        assertNull("Email not exist so return, so it must return false", denaUserManagement.findUserByEmailAddress(APP_ID, notExistingEmail));

    }

    @Test
    public void test_updateUser() {

        // existing user
        DenaObject existingUser = new DenaObject();
        existingUser.setObjectId("123");
        existingUser.setObjectURI("/user-table/object1");
        existingUser.addField("email", "ali@hotmail.com");
        existingUser.addField("is_active", false);
        existingUser.setCreateTime(1535725446418L);
        existingUser.setUpdateTime(1535725446430L);

        User updateUserRequest = new User();
        updateUserRequest.setObjectId("123");
        updateUserRequest.setObjectURI("/user-table/object1");
        updateUserRequest.addField("email", "ali@hotmail.com");
        updateUserRequest.addField("is_active", true);
        updateUserRequest.setCreateTime(1535725446418L);
        updateUserRequest.setUpdateTime(1535725446430L);

        DenaObject expectedCallDenaStore = new DenaObject();
        expectedCallDenaStore.setObjectId("123");
        expectedCallDenaStore.setObjectURI("/user-table/object1");
        expectedCallDenaStore.addField("email", "ali@hotmail.com");
        expectedCallDenaStore.addField("is_active", true);
        expectedCallDenaStore.addField("token", null);
        expectedCallDenaStore.setCreateTime(1535725446418L);
        expectedCallDenaStore.setUpdateTime(1535725446430L);


        when(mockedDenaStore.findAll("Dena", "user-table", new DenaPager()))
                .thenReturn(Arrays.asList(existingUser));

        denaUserManagement.updateUser(APP_ID, updateUserRequest);
        verify(mockedDenaStore).mergeUpdate(APP_ID, USER_TABLE_NAME, expectedCallDenaStore);

    }


}