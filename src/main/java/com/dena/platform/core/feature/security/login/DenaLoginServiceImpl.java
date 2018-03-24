package com.dena.platform.core.feature.security.login;

import com.dena.platform.core.feature.user.domain.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * @author Javad Alimohammadi [<bs.alimohammadi@yahoo.com>]
 */
@Service("denaLoginService")
public class DenaLoginServiceImpl implements DenaLoginService {
    private final static Logger log = LoggerFactory.getLogger(DenaLoginServiceImpl.class);

    @Override
    public User loginUser(String email, String rawPassword) {
        log.info("Login user email to system");
        return null;
    }

    @Override
    public boolean logoutUser(String email) {
        return false;
    }
}
