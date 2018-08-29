package com.dena.platform.core.feature.security;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * @author Nazarpour.
 */
public abstract class SecurityUtil {
    private static PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public static String encodePassword(String rawPassword) {
        return passwordEncoder.encode(rawPassword);
    }

    public static boolean matchesPassword(String claimedRawPassword, String encodedPassword) {
        return passwordEncoder.matches(claimedRawPassword, encodedPassword);
    }

    public static void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        SecurityUtil.passwordEncoder = passwordEncoder;
    }
}
