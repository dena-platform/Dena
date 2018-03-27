package com.dena.platform.core.feature.security;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author Nazarpour.
 */
public class SecurityUtilTest {
    @Test
    public void matchPassWithStoredEncodedPassword() throws Exception {
        String encodedBefore = "$2a$10$y9m.QUrmfR7zVT3bN.faZOAOPpGdH8pGkYorBbyJ5epJGRM5YXCoC";
        assertTrue(SecurityUtil.matchesPassword("123", encodedBefore));
    }

    @Test
    public void matchPassWithEncodedVersion() throws Exception {
        assertTrue(SecurityUtil.matchesPassword("123", SecurityUtil.encodePassword("123")));
    }
}