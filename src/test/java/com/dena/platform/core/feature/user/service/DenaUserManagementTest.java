package com.dena.platform.core.feature.user.service;

import com.dena.platform.core.feature.user.domain.User;
import com.dena.platform.utils.ObjectModelHelper;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author Javad Alimohammadi<bs.alimohammadi@yahoo.com>
 */

public class DenaUserManagementTest {

    private DenaUserManagement denaUserManagement;


    @Before
    public void setUp() throws Exception {
        denaUserManagement = new DenaUserManagementImpl();
    }

    @Test
    public void test_registerUser() {
        String appId = "Dena";
        User user = ObjectModelHelper.getSampleUser();

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