package com.dena.platform.core.feature.security;

import com.dena.platform.common.utils.DenaMessageUtils;
import com.dena.platform.core.dto.DenaObject;
import com.dena.platform.core.feature.persistence.DenaDataStore;
import com.dena.platform.core.feature.persistence.DenaPager;
import com.dena.platform.core.feature.security.domain.APPUser;
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

    private String databaseName;

    private String appUserTypeName;

    @Resource
    private DenaDataStore denaDataStore;

    @PostConstruct
    public void init() {
        databaseName = DenaMessageUtils.getMessage("UserManagement.databaseName");
        appUserTypeName = DenaMessageUtils.getMessage("UserManagement.appUser.type");
    }

    @Override
    public void registerUser(APPUser appUser) {
        DenaObject denaObject = new DenaObject();
        denaObject.addProperty("email", appUser.getEmail());
        denaObject.addProperty("password", appUser.getPassword());
        denaObject.addProperty("app_name", appUser.getAppName());

        denaDataStore.store(databaseName, appUserTypeName, denaObject);
    }

    @Override
    public boolean isUserExist(APPUser appUser) {
        // todo: when we implement search capability in DanaStore module then refactor this method to use it
        List<DenaObject> denaObjects = denaDataStore.findAll(databaseName, appUserTypeName, new DenaPager());
        Optional foundUser = denaObjects.stream()
                .filter(denaObject -> denaObject.hasProperty("email", appUser.getEmail()))
                .findAny();

        return foundUser.isPresent();

    }

    private DenaObject convertToDenaObject(APPUser appUser) {
        return null;
    }
}
