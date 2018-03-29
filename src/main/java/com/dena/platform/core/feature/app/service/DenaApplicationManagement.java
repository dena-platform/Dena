package com.dena.platform.core.feature.app.service;

import com.dena.platform.core.feature.app.domain.DenaApplication;

/**
 * @author Javad Alimohammadi [<bs.alimohammadi@yahoo.com>]
 */
public interface DenaApplicationManagement {
    void registerApplication(DenaApplication denaApplication);

    boolean isApplicationExist(String creatorId, String applicationName);
}
