package com.dena.platform.core.feature.security.service;

import com.dena.platform.core.dto.DenaObject;

/**
 * @author Javad Alimohammadi<bs.alimohammadi@yahoo.com>
 */

public interface DenaSecurityService {

    DenaObject authenticateUser(String appId, String userName, String password);

    DenaObject logoutUser(String appId, String userName);

}
