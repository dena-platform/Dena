package com.dena.platform.core.feature.security;

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

    // todo: change this method to isValid and return boolean

    /**
     * validate token
     *
     * @param token
     * @return corresponding user
     */
    User validate(String token);

    /**
     * Invalidate token
     *
     * @param appId
     * @param token
     */
    void expireToken(String appId, String token);

}