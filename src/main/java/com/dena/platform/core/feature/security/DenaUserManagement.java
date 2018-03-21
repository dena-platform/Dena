package com.dena.platform.core.feature.security;

import com.dena.platform.core.dto.DenaObject;
import com.dena.platform.core.feature.security.domain.User;
import com.dena.platform.core.feature.security.exception.UserManagementException;

/**
 * @author Javad Alimohammadi [<bs.alimohammadi@yahoo.com>]
 */
public interface DenaUserManagement {

    /**
     * Register an user in app.<br>
     * If user exist then throw {@link UserManagementException} exception.
     *
     * @param user
     * @return
     * @throws UserManagementException
     */
    DenaObject registerUser(String appId, User user);

    /**
     * Check if user is registered in dena platform before
     *
     *
     * @param appId
     * @param user
     * @return
     */
    boolean isUserExist(String appId, User user);


}
