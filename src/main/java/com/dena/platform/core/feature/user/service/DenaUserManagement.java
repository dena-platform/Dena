package com.dena.platform.core.feature.user.service;

import com.dena.platform.core.dto.DenaObject;
import com.dena.platform.core.feature.user.domain.User;
import com.dena.platform.core.feature.user.exception.UserManagementException;

/**
 * @author Javad Alimohammadi [<bs.alimohammadi@gmail.com>]
 */
public interface DenaUserManagement {

    /**
     * Register a user in app.<br>
     * If user exist then throw {@link UserManagementException} exception.
     *
     * @param appId Name of app for registering new user
     * @param user User information for registering in platform
     * @return registered user in platform
     * @throws UserManagementException
     */
    DenaObject registerUser(String appId, User user);

    /**
     * Check if user is registered in dena platform before
     *
     * @param appId app id
     * @param emailAddress
     * @return true if user registered in app.
     */
    boolean isUserExist(String appId, String emailAddress);

    /**
     * get user by email
     *
     * @param appId
     * @param email
     * @return User object if exists or null
     */
    User findUserByEmailAddress(String appId, String email);


    /**
     * updates user which belongs to application with id appId
     * @param appId
     * @param user
     */
    void updateUser(String appId, User user);

}
