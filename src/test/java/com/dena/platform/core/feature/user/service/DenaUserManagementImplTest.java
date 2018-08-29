package com.dena.platform.core.feature.user.service;

import com.dena.platform.core.dto.DenaObject;
import com.dena.platform.core.feature.persistence.DenaDataStore;
import com.dena.platform.core.feature.persistence.DenaPager;
import com.dena.platform.core.feature.security.SecurityUtil;
import com.dena.platform.core.feature.user.domain.User;
import com.dena.platform.utils.ObjectModelHelper;
import org.junit.Before;
import org.junit.Test;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;

import java.time.Instant;
import java.util.Arrays;
import java.util.Collections;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author Javad Alimohammadi<bs.alimohammadi@yahoo.com>
 */

public class DenaUserManagementImplTest {

    private DenaUserManagementImpl denaUserManagement;

    private final DenaDataStore mockedDenaStore = mock(DenaDataStore.class);

    private final String USER_TABLE_NAME = "user-table";

    @Before
    public void setUp() throws Exception {
        denaUserManagement = new DenaUserManagementImpl();
        denaUserManagement.setUserInfoTableName(USER_TABLE_NAME);

        DenaObject existingUser = new DenaObject();
        existingUser.setObjectId("123");
        existingUser.setObjectURI("/user-table/object1");
        existingUser.setCreateTime(Instant.now().toEpochMilli());

        // change security encoder to not encode password
        SecurityUtil.setPasswordEncoder(NoOpPasswordEncoder.getInstance());

        // mock find all method
        when(mockedDenaStore.findAll("Dena", "user-table", new DenaPager(0, 50)))
                .thenReturn(Collections.emptyList());


        denaUserManagement.denaDataStore = mockedDenaStore;


    }

    @Test
    public void test_registerUser() {

        final String appId = "Dena";
        final User user = ObjectModelHelper.getSampleUser();

        // given
        DenaObject requestToStore = new DenaObject();
        requestToStore.addField("password", "123");
        requestToStore.addField("is_active", true);
        requestToStore.addField("email", "ali@hotmail.com");

        when(mockedDenaStore.store(appId, USER_TABLE_NAME, requestToStore))
                .thenReturn(Arrays.asList(requestToStore));

        // when
        denaUserManagement.registerUser(appId, user);

        // then


    }

    @Test
    public void test_isUserExist() {
    }

    @Test
    public void findUserById() {
    }

    @Test
    public void updateUser() {
    }

}