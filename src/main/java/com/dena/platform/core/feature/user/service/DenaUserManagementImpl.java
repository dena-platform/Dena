package com.dena.platform.core.feature.user.service;

import com.dena.platform.common.config.DenaConfigReader;
import com.dena.platform.common.exception.ErrorCode;
import com.dena.platform.core.dto.DenaObject;
import com.dena.platform.core.feature.persistence.DenaDataStore;
import com.dena.platform.core.feature.persistence.DenaPager;
import com.dena.platform.core.feature.security.SecurityUtil;
import com.dena.platform.core.feature.user.domain.User;
import com.dena.platform.core.feature.user.exception.UserManagementException;
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

@Service("denaDenaUserManagement")
public class DenaUserManagementImpl implements DenaUserManagement {
    private final static Logger log = LoggerFactory.getLogger(DenaUserManagementImpl.class);

    private String userInfoTableName;

    @Resource
    private DenaDataStore denaDataStore;

    //todo: initialize this bean in another class another way
    @Resource
    private DenaConfigReader denaConfigReader;

    @PostConstruct
    public void init() {
        userInfoTableName = DenaConfigReader.readProperty("dena.UserManagement.user.table");
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
        user.setPassword(encodedPassword);

        if (user.getActive() == null) {
            boolean isActive = DenaConfigReader.readBooleanProperty("dena.UserManagement.register.default_status", false);
            user.setActive(isActive);
        }

        log.debug("Registering new user [{}] in Dena Platform", user.getEmail());
        DenaObject denaObject = new DenaObject();
        denaObject.addProperty(User.EMAIL_FIELD_NAME, user.getEmail());
        denaObject.addProperty(User.PASSWORD_FIELD_NAME, user.getPassword());
        denaObject.addProperty(User.IS_ACTIVE, user.getActive());
        denaObject.addFields(user.getOtherFields());

        DenaObject returnObject = denaDataStore.store(appId, userInfoTableName, denaObject).get(0);
        return returnObject;
    }

    @Override
    public boolean isUserExist(String appId, User user) {
        // todo: when we implement search capability in DanaStore module, then refactor this method to use it
        List<DenaObject> denaObjects = denaDataStore.findAll(appId, userInfoTableName, new DenaPager(0, DenaConfigReader.readIntProperty("dena.pager.max.results", 50)));
        Optional<DenaObject> foundUser = denaObjects.stream()
                .filter(denaObject -> denaObject.hasProperty(User.EMAIL_FIELD_NAME, user.getEmail()))
                .findAny();

        return foundUser.isPresent();
    }

    @Override
    public User getUserById(String appId, String email) {
        // todo: when we implement search capability in DanaStore module, then refactor this method to use it

        List<DenaObject> denaObjects = denaDataStore.findAll(appId, userInfoTableName, new DenaPager(0, DenaConfigReader.readIntProperty("dena.pager.max.results", 50)));

        Optional<DenaObject> foundUser = denaObjects.stream()
                .filter(denaObject -> denaObject.hasProperty(User.EMAIL_FIELD_NAME, email))
                .findAny();

        User found = new User();
        foundUser.ifPresent(x -> found.setActive((Boolean) x.getOtherFields().get(User.IS_ACTIVE)));
        foundUser.ifPresent(x -> found.setEmail((String) x.getOtherFields().get(User.EMAIL_FIELD_NAME)));
        foundUser.ifPresent(x -> found.setPassword((String) x.getOtherFields().get(User.PASSWORD_FIELD_NAME)));
        foundUser.ifPresent(x -> found.setLastValidToken((String) x.getOtherFields().get(User.LAST_VALID_TOKEN)));
        return found;
    }


    @Override
    public void updateUser(String appId, User user) {
        List<DenaObject> denaObjects = denaDataStore.findAll(appId, userInfoTableName, new DenaPager(0, DenaConfigReader.readIntProperty("dena.pager.max.results", 50)));

        Optional<DenaObject> foundUser = denaObjects.stream()
                .filter(denaObject -> denaObject.hasProperty(User.EMAIL_FIELD_NAME, user.getEmail()))
                .findAny();

        if (!foundUser.isPresent())
            throw new UserManagementException(String.format("no user with email %s found for update ", user.getEmail()),
                    ErrorCode.NO_USER_WITH_THIS_EMAIL_FOUND);

        DenaObject denaObject = foundUser.get();

        denaObject.addProperty(User.EMAIL_FIELD_NAME, user.getEmail());
//        denaObject.addProperty(User.PASSWORD_FIELD_NAME, user.getPassword());
        denaObject.addProperty(User.IS_ACTIVE, user.getActive());
        denaObject.addProperty(User.LAST_VALID_TOKEN, user.getLastValidToken());
        denaObject.addFields(user.getOtherFields());

        denaDataStore.store(appId, userInfoTableName, denaObject).get(0);
    }

}
