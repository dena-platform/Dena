package com.dena.platform.core.feature.security.service;

import com.dena.platform.core.dto.DenaObject;

/**
 * @author Javad Alimohammadi<bs.alimohammadi@gmail.com>
 */

public interface DenaSecurityService {

    DenaObject authenticateUser(String appName, String userName, String password);

    DenaObject logoutUser(String appName, String userName);

}
