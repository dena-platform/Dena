package com.dena.platform.core.feature.security.service;

/**
 * @author Nazarpour.
 */
public interface JWTService {

    /**
     * Generate JWT token based on provided information.
     *
     * @param appId
     * @param userName
     * @return token as a string
     */
    String generateJWTToken(String appId, String userName);

    /**
     * Check if provided token is valid
     *
     * @param token
     * @return True if provided token is valid.
     */
    boolean isTokenValid(String token);

}
