package com.dena.platform.core.feature.user.service;

import com.dena.platform.common.config.DenaConfigReader;
import com.dena.platform.common.exception.ErrorCode;
import com.dena.platform.core.dto.DenaObject;
import com.dena.platform.core.feature.persistence.DenaDataStore;
import com.dena.platform.core.feature.persistence.DenaPager;
import com.dena.platform.core.feature.security.SecurityUtil;
import com.dena.platform.core.feature.user.domain.User;
import com.dena.platform.core.feature.user.exception.UserManagementException;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.validator.GenericValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;

/**
 * @author Javad Alimohammadi [<bs.alimohammadi@gmail.com>]
 */

@Service("denaUserManagement")
public class DenaUserManagementImpl implements DenaUserManagement {
    private final static Logger log = LoggerFactory.getLogger(DenaUserManagementImpl.class);

    private String userInfoTableName;

    private boolean isActive;

    @Resource
    protected DenaDataStore denaDataStore;

    @PostConstruct
    public void init() {
        userInfoTableName = DenaConfigReader.readProperty("dena.UserManagement.user.table");
        isActive = DenaConfigReader.readBooleanProperty("dena.UserManagement.register.default_status", true);
    }

    @Override
    public DenaObject registerUser(final String appId, final User user) {

        if (!GenericValidator.isEmail(user.getEmail())) {
            throw new UserManagementException(String.format("Email [%s] is not in correct format", user.getEmail()), ErrorCode.EMAIL_FIELD_IS_INVALID);
        }

        if (GenericValidator.isBlankOrNull(user.getUnencodedPassword())) {
            throw new UserManagementException(String.format("Password [%s] is not in correct format", user.getUnencodedPassword()), ErrorCode.PASSWORD_FIELD_IS_INVALID);
        }

        if (isUserExist(appId, user)) {
            throw new UserManagementException(String.format("User with this identity [%s] already exist", user.getEmail()), ErrorCode.USER_ALREADY_EXIST_EXCEPTION);
        }


        String encodedPassword = SecurityUtil.encodePassword(user.getUnencodedPassword());

        if (BooleanUtils.isTrue(user.getActive())) {
            user.setActive(isActive);
        }

        log.debug("Registering new user identifier [{}] in Dena Platform", user.getEmail());
        DenaObject denaObject = new DenaObject();
        denaObject.addField(User.EMAIL_FIELD_NAME, user.getEmail());
        denaObject.addField(User.PASSWORD_FIELD_NAME, encodedPassword);
        denaObject.addField(User.IS_ACTIVE_FIELD_NAME, user.getActive());
        denaObject.addFields(user.getOtherFields());

        return denaDataStore.store(appId, userInfoTableName, denaObject).get(0);
    }

    @Override
    public boolean isUserExist(String appId, User user) {
        // todo: when we implement search capability in DanaStore module, then refactor this method to use that
        List<DenaObject> denaObjects = denaDataStore.findAll(appId, userInfoTableName, new DenaPager(0, DenaConfigReader.readIntProperty("dena.pager.max.results", 50)));
        Optional<DenaObject> foundUser = denaObjects.stream()
                .filter(denaObject -> denaObject.hasProperty(User.EMAIL_FIELD_NAME, user.getEmail()))
                .findAny();

        return foundUser.isPresent();
    }

    @Override
    public User findUserById(String appId, String email) {
        // todo: when we implement search capability in DanaStore module, then refactor this method to use that

        List<DenaObject> denaObjects = denaDataStore.findAll(appId, userInfoTableName, new DenaPager(0, DenaConfigReader.readIntProperty("dena.pager.max.results", 50)));

        Optional<DenaObject> foundDenaObject = denaObjects.stream()
                .filter(denaObject -> denaObject.hasProperty(User.EMAIL_FIELD_NAME, email))
                .findAny();

        User foundUser = new User();

        if (foundDenaObject.isPresent()) {
            foundUser.setObjectId(foundDenaObject.get().getObjectId());
            foundUser.setObjectURI(foundDenaObject.get().getObjectURI());
            foundUser.setCreateTime(foundDenaObject.get().getCreateTime());
            foundUser.setUpdateTime(foundDenaObject.get().getUpdateTime());
            foundUser.addFields(foundDenaObject.get().getOtherFields());
        }


        return foundUser;
    }


    @Override
    public void updateUser(String appId, User user) {
        List<DenaObject> denaObjects = denaDataStore.findAll(appId, userInfoTableName, new DenaPager(0, DenaConfigReader.readIntProperty("dena.pager.max.results", 50)));

        Optional<DenaObject> foundUser = denaObjects.stream()
                .filter(denaObject -> denaObject.hasProperty(User.EMAIL_FIELD_NAME, user.getEmail()))
                .findAny();

        if (!foundUser.isPresent())
            throw new UserManagementException(String.format("no user with email %s found for mergeUpdate ", user.getEmail()),
                    ErrorCode.NO_USER_WITH_THIS_EMAIL_FOUND);

        DenaObject denaObject = foundUser.get();

        denaObject.addField(User.EMAIL_FIELD_NAME, user.getEmail());
        denaObject.addField(User.IS_ACTIVE_FIELD_NAME, user.getActive());
        denaObject.addField(User.TOKEN_FIELD_NAME, user.getToken());
        denaObject.addFields(user.getOtherFields());

        denaDataStore.mergeUpdate(appId, userInfoTableName, denaObject).get(0);
    }

    public void setUserInfoTableName(String userInfoTableName) {
        this.userInfoTableName = userInfoTableName;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

}
