package com.dena.platform.core.feature.security.service;

/**
 * @author Nazarpour.
 */
public interface JWTService {

    String APP_ID = "app_id";

    String USER_NAME = "userName";

    String ROLE = "role";

    String CREATION_DATE = "creation_date";

    /**
     * Generate JWT token based on provided information.
     *
     * @param appId
     * @param userName
     * @param secret
     * @return token as a string
     */
    String generateJWTToken(String appId, String userName, String secret);

    /**
     * Check if provided token is valid
     *
     * @param token
     * @param secret
     * @return True if provided token is valid.
     */
    boolean isTokenValid(String token, String secret);

}
