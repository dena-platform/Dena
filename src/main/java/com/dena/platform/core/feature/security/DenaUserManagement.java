package com.dena.platform.core.feature.security;

import com.dena.platform.core.feature.security.domain.APPUser;

/**
 * @author Javad Alimohammadi [<bs.alimohammadi@yahoo.com>]
 */
public interface DenaUserManagement {
    void registerUser(APPUser appUser);

    boolean isUserExist(APPUser appUser);


}
