package com.dena.platform.core.feature.security;

import com.dena.platform.common.config.DenaConfigReader;
import com.dena.platform.common.exception.ErrorCode;
import com.dena.platform.core.dto.DenaObject;
import com.dena.platform.core.feature.persistence.DenaDataStore;
import com.dena.platform.core.feature.persistence.DenaPager;
import com.dena.platform.core.feature.security.domain.User;
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
        userTypeName = DenaConfigReader.readProperty("UserManagement.user.type");
    }

    @Override
    public DenaObject registerUser(User user) {
        if (isUserExist(user)) {
            throw new UserManagementException(String.format("User with this identity [%s] already exist", user.getEmail()), ErrorCode.USER_ALREADY_EXIST_EXCEPTION);
        }
        DenaObject denaObject = new DenaObject();
        denaObject.addProperty(User.EMAIL_FIELD_NAME, user.getEmail());
        denaObject.addProperty(User.PASSWORD_FIELD_NAME, user.getPassword());
        denaObject.addProperty(User.APPNAME_FIELD_NAME, user.getAppName());

        return denaDataStore.store(user.getAppName(), userTypeName, denaObject).get(0);
    }

    @Override
    public boolean isUserExist(User user) {
        // todo: when we implement search capability in DanaStore module, then refactor this method to use it
        List<DenaObject> denaObjects = denaDataStore.findAll(user.getAppName(), userTypeName, new DenaPager());
        Optional foundUser = denaObjects.stream()
                .filter(denaObject -> denaObject.hasProperty(User.EMAIL_FIELD_NAME, user.getEmail()))
                .findAny();

        return foundUser.isPresent();

    }

    private DenaObject convertToDenaObject(User user) {
        return null;
    }
}
