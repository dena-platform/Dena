package com.dena.platform.core.feature.user.service;

import com.dena.platform.core.feature.user.domain.User;
import com.dena.platform.utils.ObjectModelHelper;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Javad Alimohammadi<bs.alimohammadi@yahoo.com>
 */

public class DenaUserManagementImplTest {

    private DenaUserManagement denaUserManagement;


    @Before
    public void setUp() throws Exception {
        denaUserManagement = new DenaUserManagementImpl();
    }

    @Test
    public void test_registerUser_happy_path() {
        final String appId = "Dena";
        final User user = ObjectModelHelper.getSampleUser();

        denaUserManagement.registerUser(appId, user);


    }

    @Test
    public void isUserExist() {
    }

    @Test
    public void findUserById() {
    }

    @Test
    public void updateUser() {
    }
}