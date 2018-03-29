package com.dena.platform.core.feature.app.service;

import com.dena.platform.common.config.DenaConfigReader;
import com.dena.platform.common.exception.ErrorCode;
import com.dena.platform.core.dto.DenaObject;
import com.dena.platform.core.feature.app.domain.DenaApplication;
import com.dena.platform.core.feature.app.exception.AppManagementException;
import com.dena.platform.core.feature.persistence.DenaDataStore;
import com.dena.platform.core.feature.persistence.DenaPager;
import org.apache.commons.validator.GenericValidator;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * @author Javad Alimohammadi [<bs.alimohammadi@yahoo.com>]
 */
@Service("denaApplicationManagement")
public class DenaApplicationManagementImpl implements DenaApplicationManagement {

    @Resource
    protected DenaDataStore denaDataStore;

    private String applicationDatabaseName;

    private String applicationInfoTableName;


    @PostConstruct
    public void init() {
        applicationDatabaseName = DenaConfigReader.readProperty("dena.application.database", "DENA_APPLICATIONS");
        applicationInfoTableName = DenaConfigReader.readProperty("dena.application.info.table", "DENA_APPLICATION_INFO");
    }


    @Override
    public DenaObject registerApplication(DenaApplication denaApplication) {
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
            throw new AppManagementException(String.format("Application name [%s] already exist", applicationName), ErrorCode.APPLICATION_ALREADY_EXIST);
        }


        String applicationId = generateApplicationId();
        String secretId = generateSecretId();

        denaObject.addProperty(DenaApplication.CREATOR_ID_FIELD, denaApplication.getCreatorId());
        denaObject.addProperty(DenaApplication.APP_NAME_FIELD, denaApplication.getApplicationName());
        denaObject.addProperty(DenaApplication.APP_ID_FIELD, applicationId);
        denaObject.addProperty(DenaApplication.SECRET_KEY_FIELD, secretId);

        DenaObject returnObject = denaDataStore.store(applicationDatabaseName, applicationInfoTableName, denaObject).get(0);
        return returnObject;


    }

    @Override
    public boolean isApplicationExist(final String creatorId, final String applicationName) {
        // todo: when we implement search capability in DanaStore module, then refactor this method to use it
        List<DenaObject> denaObjects = denaDataStore.findAll(applicationDatabaseName, applicationInfoTableName, new DenaPager());
        Optional<DenaObject> foundApplication = denaObjects.stream()
                .filter(denaObject -> denaObject.hasProperty(DenaApplication.CREATOR_ID_FIELD, creatorId))
                .filter(denaObject -> denaObject.hasProperty(DenaApplication.APP_NAME_FIELD, applicationName))
                .findAny();

        return foundApplication.isPresent();

    }

    // todo: use a better approach to generate unique id
    private String generateApplicationId() {
        UUID appId = UUID.randomUUID();
        return appId.toString();
    }

    // todo: use a better approach to generate unique id
    private String generateSecretId() {
        UUID appId = UUID.randomUUID();
        return appId.toString();
    }


}
