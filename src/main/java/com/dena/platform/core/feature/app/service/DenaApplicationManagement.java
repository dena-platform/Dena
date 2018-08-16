package com.dena.platform.core.feature.app.service;

import com.dena.platform.core.dto.DenaObject;
import com.dena.platform.core.feature.app.domain.DenaApplication;
import com.dena.platform.core.feature.app.exception.ApplicationManagementException;

import java.util.Optional;

/**
 * @author Javad Alimohammadi [<bs.alimohammadi@gmail.com>]
 */
public interface DenaApplicationManagement {
    DenaObject registerApplication(DenaApplication denaApplication);

    Optional<DenaObject> findApplicationByName(String creatorId, String appName);

    Optional<DenaObject> findApplicationById(String appId);

    /**
     * Find secret field of application.
     *
     * @param appId Application Id
     * @return
     * @throws ApplicationManagementException If application can not be found.
     */
    String getSecretId(String appId) throws ApplicationManagementException;

    boolean isApplicationExist(String creatorId, String applicationName);
}
