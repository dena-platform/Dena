package com.dena.platform.core.feature.security;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author Javad Alimohammadi [<bs.alimohammadi@yahoo.com>]
 */

@Service("denaSecurityService")
public class SecurityServiceImpl implements SecurityService {

    @Resource(name = "passwordEncoder")
    private PasswordEncoder passwordEncoder;


    @Override
    public String encodePassword(String rawPassword) {
        return passwordEncoder.encode(rawPassword);
    }

}
