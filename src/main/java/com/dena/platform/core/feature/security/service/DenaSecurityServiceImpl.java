package com.dena.platform.core.feature.security.service;

import com.dena.platform.common.exception.ErrorCode;
import com.dena.platform.core.dto.DenaObject;
import com.dena.platform.core.feature.security.JWTService;
import com.dena.platform.core.feature.security.SecurityUtil;
import com.dena.platform.core.feature.security.exception.SecurityException;
import com.dena.platform.core.feature.user.domain.User;
import com.dena.platform.core.feature.user.service.DenaUserManagement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author Javad Alimohammadi<bs.alimohammadi@yahoo.com>
 */

@Service("denaSecurityServiceImpl")
public class DenaSecurityServiceImpl implements DenaSecurityService {
    private final static Logger log = LoggerFactory.getLogger(DenaSecurityServiceImpl.class);

    @Resource
    private JWTService jwtService;

    @Resource
    private DenaUserManagement denaUserManagement;


    @Override
    public DenaObject authenticateUser(String appId, String userName, String password) {
        User retrievedUser = denaUserManagement.findUserById(appId, userName);

        if (retrievedUser != null && SecurityUtil.matchesPassword(password, retrievedUser.getPassword())) {

            if (!jwtService.isTokenValid(retrievedUser.getToken())) {
                log.debug("Stored token for user [{}] is invalid", userName);
                log.debug("Generate new token for user [{}], app [{}]", userName, appId);
                String jwtToken = jwtService.generateJWTToken(appId, retrievedUser);
                retrievedUser.setToken(jwtToken);
                denaUserManagement.updateUser(appId, retrievedUser);
            }

            retrievedUser.removeField(User.PASSWORD_FIELD_NAME);

            log.trace("User [{}] successfully logined", userName);
            return retrievedUser;
        } else {
            throw new SecurityException("User name or password is invalid", ErrorCode.BAD_CREDENTIAL);
        }

    }
}
