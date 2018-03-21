package com.dena.platform.core.feature.security;

import com.dena.platform.core.dto.DenaObject;
import com.dena.platform.core.feature.security.domain.User;

/**
 * @author Javad Alimohammadi [<bs.alimohammadi@yahoo.com>]
 */
public interface DenaUserManagement {
    DenaObject registerUser(User user);

    boolean isUserExist(User user);


}
