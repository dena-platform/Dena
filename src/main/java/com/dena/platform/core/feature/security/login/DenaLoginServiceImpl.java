package com.dena.platform.core.feature.security.login;

import com.dena.platform.core.feature.user.domain.User;
import org.springframework.stereotype.Service;

/**
 * @author Javad Alimohammadi [<bs.alimohammadi@yahoo.com>]
 */
@Service("denaLoginService")
public class DenaLoginServiceImpl implements DenaLoginService {
    @Override
    public User loginUser(String email, String rawPassword) {
        return null;
    }

    @Override
    public boolean logoutUser(String email) {
        return false;
    }
}
