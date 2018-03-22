package com.dena.platform.core.feature.security.service;

/**
 * @author Javad Alimohammadi [<bs.alimohammadi@yahoo.com>]
 */
public interface SecurityService {
    /**
     * Generate an encoded password from a raw password, salting is handled internally by the {@link org.springframework.security.crypto.password.PasswordEncoder}.
     *
     * @param rawPassword
     * @return
     */
    String encodePassword(String rawPassword);
}
