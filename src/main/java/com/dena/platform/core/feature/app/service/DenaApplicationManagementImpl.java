package com.dena.platform.core.feature.app.service;

import com.dena.platform.common.exception.ErrorCode;
import com.dena.platform.core.dto.DenaObject;
import com.dena.platform.core.feature.app.domain.DenaApplication;
import com.dena.platform.core.feature.app.exception.AppManagementException;
import com.dena.platform.core.feature.persistence.DenaDataStore;
import com.dena.platform.core.feature.user.exception.UserManagementException;
import org.apache.commons.validator.GenericValidator;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author Javad Alimohammadi [<bs.alimohammadi@yahoo.com>]
 */
@Service("denaApplicationManagement")
public class DenaApplicationManagementImpl implements DenaApplicationManagement {
    @Resource
    protected DenaDataStore denaDataStore;


    @Override

    public void registerApp(DenaApplication denaApplication) {
        DenaObject denaObject = new DenaObject();
        String creatorId = denaApplication.getCreatorId();
        String applicationName = denaApplication.getApplicationName();

        if (GenericValidator.isBlankOrNull(creatorId)) {
            throw new AppManagementException(String.format("Creator id [%s] is not in correct format", creatorId), ErrorCode.CREATOR_FIELD_IS_INVALID);
        }

        // todo : we should also specify a minimum app name length restriction
        if (GenericValidator.isBlankOrNull(applicationName)) {
            throw new AppManagementException(String.format("Application name [%s] is not in correct format", applicationName), ErrorCode.APP_NAME_FIELD_IS_INVALID);
        }

        if (isApplicationExist(creatorId, applicationName)) {
            throw new AppManagementException(String.format("Application with this name [%s] already exist", applicationName), ErrorCode.USER_ALREADY_EXIST_EXCEPTION);
        }

        denaObject.addProperty(DenaApplication.CREATOR_ID_FIELD, denaApplication.getCreatorId());
        denaObject.addProperty(DenaApplication.APP_NAME_FIELD, denaApplication.getApplicationName());
    }

    @Override
    public boolean isApplicationExist(String creatorId, String applicationName) {
        return false;
    }
}
