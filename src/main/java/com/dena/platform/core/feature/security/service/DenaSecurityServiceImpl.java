package com.dena.platform.core.feature.security.service;

import com.dena.platform.core.dto.DenaObject;
import com.dena.platform.core.feature.security.JWTService;
import com.dena.platform.core.feature.security.SecurityUtil;
import com.dena.platform.core.feature.user.domain.User;
import com.dena.platform.core.feature.user.service.DenaUserManagement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.BadCredentialsException;
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
            String jwtToken = jwtService.generateJWTToken(appId, retrievedUser);

            DenaObject denaObject = new DenaObject();
            denaObject.addField(User.EMAIL_FIELD_NAME, userName);
            denaObject.addField(User.JWT_TOKEN, jwtToken);
            denaObject.addField(User.IS_ACTIVE, retrievedUser.getActive());
            denaObject.addFields(retrievedUser.getOtherFields());

            log.trace("User [{}] successfully logined", userName);
            return denaObject;
        } else {
            throw new BadCredentialsException("User name or password is invalid");
        }

    }
}
