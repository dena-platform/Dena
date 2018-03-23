package com.dena.platform.core.feature.security.login;

import com.dena.platform.core.feature.user.domain.User;

/**
 * @author Javad Alimohammadi [<bs.alimohammadi@yahoo.com>]
 */
public interface LoginService {
    User loginUser(String email, String rawPassword);

    boolean logoutUser(String email);
}
