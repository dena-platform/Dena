package com.dena.platform.core.feature.security.service;

import com.dena.platform.common.exception.ErrorCode;
import com.dena.platform.common.utils.DenaMessageUtils;
import com.dena.platform.common.web.DenaRequestContext;
import com.dena.platform.core.dto.DenaObject;
import com.dena.platform.core.feature.app.service.DenaApplicationManagement;
import com.dena.platform.core.feature.security.SecurityUtil;
import com.dena.platform.core.feature.security.exception.DenaSecurityException;
import com.dena.platform.core.feature.user.domain.User;
import com.dena.platform.core.feature.user.service.DenaUserManagement;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
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

    @Resource
    private DenaApplicationManagement denaApplicationManagement;


    @Override
    public DenaObject authenticateUser(String appName, String userName, String password) {
        User retrievedUser = denaUserManagement.findUserById(appName, userName);

        boolean isUserPasswordValid = (retrievedUser != null &&
                SecurityUtil.matchesPassword(password, retrievedUser.getPassword()));


        if (isUserPasswordValid) {
            boolean isUserActive = BooleanUtils.toBoolean(retrievedUser.getActive());

            if (!isUserActive) {
                throw new DenaSecurityException("User is not active", ErrorCode.USER_IS_NOT_ACTIVE);
            }

            String secretId = denaApplicationManagement.getSecretId(appName);

            // is existing token valid?
            if (!jwtService.isTokenValid(retrievedUser.getToken(), secretId)) {
                log.debug("Stored token for user [{}] is invalid", userName);
                log.debug("Generate new token for user [{}], app [{}]", userName, appName);
                String jwtToken = jwtService.generateJWTToken(appName, userName, secretId);
                retrievedUser.setToken(jwtToken);
                denaUserManagement.updateUser(appName, retrievedUser);
            }

            retrievedUser.removeField(User.PASSWORD_FIELD_NAME);

            log.trace("User [{}] logined successfully", userName);
            return retrievedUser;
        } else {
            throw new DenaSecurityException("User name or password is invalid", ErrorCode.BAD_CREDENTIAL);
        }

    }

    @Override
    public DenaObject logoutUser(String appName, String userName) {
        String token = DenaRequestContext.getDenaRequestContext().getToken();

        String secretId = denaApplicationManagement.getSecretId(appName);

        if (jwtService.isTokenValid(token, secretId)) {
            User retrievedUser = denaUserManagement.findUserById(appName, userName);
            retrievedUser.setToken(StringUtils.EMPTY);
            denaUserManagement.updateUser(appName, retrievedUser);
            DenaObject result = new DenaObject();
            result.addField("Message", DenaMessageUtils.getMessage("user.logout.message", " User logout successfully"));

            log.trace("User [{}] logout successfully", userName);
            return result;
        } else {
            throw new DenaSecurityException("Token is invalid", ErrorCode.TOKEN_INVALID);
        }

    }
}
