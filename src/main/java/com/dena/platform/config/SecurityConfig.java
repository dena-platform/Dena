package com.dena.platform.config;

import com.dena.platform.common.config.DenaConfigReader;
import com.dena.platform.common.utils.BeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * @author Javad Alimohammadi [<bs.alimohammadi@yahoo.com>]
 */

@Configuration
public class SecurityConfig {

    @Bean("passwordEncoder")
    public PasswordEncoder passwordEncoderBean() {
        String passwordEncoderName = DenaConfigReader.readProperty("dena.security.password.encoder", "org.springframework.security.crypto.password.NoOpPasswordEncoder");
        return (PasswordEncoder) BeanFactory.createInstance(passwordEncoderName);
    }

    // @formatter:off

    // @formatter:on


}
