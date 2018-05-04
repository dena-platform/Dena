package com.dena.platform.core.feature.app.service;

import com.dena.platform.core.dto.DenaObject;
import com.dena.platform.core.feature.app.domain.DenaApplication;

/**
 * @author Javad Alimohammadi [<bs.alimohammadi@gmail.com>]
 */
public interface DenaApplicationManagement {
    DenaObject registerApplication(DenaApplication denaApplication);

    boolean isApplicationExist(String creatorId, String applicationName);
}
