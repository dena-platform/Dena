package com.dena.platform.config;

import com.dena.platform.common.config.DenaConfigReader;
import com.dena.platform.common.utils.BeanFactory;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * @author Javad Alimohammadi [<bs.alimohammadi@yahoo.com>]
 */

@Configuration
@EnableWebSecurity
public class SecurityConfig {


    @Bean("passwordEncoder")
    public PasswordEncoder passwordEncoderBean() {
        String passwordEncoderName = DenaConfigReader.readProperty("dena.security.password.encoder");

        if (StringUtils.isEmpty(passwordEncoderName)) {
            return NoOpPasswordEncoder.getInstance();
        }

        return (PasswordEncoder) BeanFactory.createInstance(passwordEncoderName);
    }


}
