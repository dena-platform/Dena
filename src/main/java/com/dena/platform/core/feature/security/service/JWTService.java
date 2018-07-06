package com.dena.platform.core.feature.security.service;

import com.dena.platform.core.feature.user.domain.User;

/**
 * @author Nazarpour.
 */
public interface JWTService {

    /**
     * Generate JWT token based on provided information.
     *
     * @param appId
     * @param claimedUser
     * @return token as a string
     */
    String generateJWTToken(String appId, User claimedUser);

    /**
     * Check if provided token is valid
     *
     * @param token
     * @return True if provided token is valid.
     */
    boolean isTokenValid(String token);

    /**
     * Invalidate token
     *
     * @param appId
     * @param token
     */
    void expireToken(String appId, String token);

}
