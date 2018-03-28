package com.dena.platform.core.feature.app.service;

import com.dena.platform.common.exception.ErrorCode;
import com.dena.platform.core.dto.DenaObject;
import com.dena.platform.core.feature.app.domain.DenaApplication;
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

        if (GenericValidator.isBlankOrNull(denaApplication.getCreatorId())) {
            throw new UserManagementException(String.format("Creator id [%s] is not in correct format", denaApplication.getCreatorId()), ErrorCode.CREATOR_FIELD_IS_INVALID);
        }

        // todo : we can also specify a mnimume app name length
        if (GenericValidator.isBlankOrNull(denaApplication.getAppName())) {
            throw new UserManagementException(String.format("Application name [%s] is not in correct format", denaApplication.getAppName()), ErrorCode.APP_NAME_FIELD_IS_INVALID);
        }

        denaObject.addProperty(DenaApplication.CREATOR_ID_FIELD, denaApplication.getAppName());


        denaObject.addProperty(DenaApplication.APP_NAME_FIELD, denaApplication.getAppName());
    }

    @Override
    public boolean isAppExist(String creatorId, String appName) {
        return false;
    }
}
