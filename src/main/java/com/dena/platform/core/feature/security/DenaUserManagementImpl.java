package com.dena.platform.core.feature.security;

import com.dena.platform.common.exception.ErrorCode;
import com.dena.platform.common.utils.DenaMessageUtils;
import com.dena.platform.core.dto.DenaObject;
import com.dena.platform.core.feature.persistence.DenaDataStore;
import com.dena.platform.core.feature.persistence.DenaPager;
import com.dena.platform.core.feature.security.domain.APPUser;
import com.dena.platform.core.feature.security.exception.UserManagementException;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;

/**
 * @author Javad Alimohammadi [<bs.alimohammadi@yahoo.com>]
 */

@Service("denaDenaUserManagementImpl")
public class DenaUserManagementImpl implements DenaUserManagement {

    private String userTypeName;

    @Resource
    private DenaDataStore denaDataStore;

    @PostConstruct
    public void init() {
        userTypeName = DenaMessageUtils.getMessage("UserManagement.user.type");
    }

    @Override
    public void registerUser(APPUser user) {
        if (isUserExist(user)) {
            throw new UserManagementException(String.format("User with this identity [%s] already exist", user.getEmail()), ErrorCode.USER_ALREADY_EXIST_EXCEPTION);
        }
        DenaObject denaObject = new DenaObject();
        denaObject.addProperty(APPUser.EMAIL_FIELD_NAME, user.getEmail());
        denaObject.addProperty(APPUser.PASSWORD_FIELD_NAME, user.getPassword());
        denaObject.addProperty(APPUser.APPNAME_FIELD_NAME, user.getAppName());

        denaDataStore.store(user.getAppName(), userTypeName, denaObject);
    }

    @Override
    public boolean isUserExist(APPUser appUser) {
        // todo: when we implement search capability in DanaStore module then refactor this method to use it
        List<DenaObject> denaObjects = denaDataStore.findAll(appUser.getAppName(), userTypeName, new DenaPager());
        Optional foundUser = denaObjects.stream()
                .filter(denaObject -> denaObject.hasProperty(APPUser.EMAIL_FIELD_NAME, appUser.getEmail()))
                .findAny();

        return foundUser.isPresent();

    }

    private DenaObject convertToDenaObject(APPUser appUser) {
        return null;
    }
}
