package com.dena.platform.core.feature.app.service;

import com.dena.platform.core.feature.app.domain.DenaApplication;

/**
 * @author Javad Alimohammadi [<bs.alimohammadi@yahoo.com>]
 */
public interface DenaApplicationManagement {
    void registerApp(DenaApplication denaApplication);

    boolean isAppExist(String creatorId, String appName);
}
