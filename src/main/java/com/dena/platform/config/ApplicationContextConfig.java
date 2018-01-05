package com.dena.platform.config;

import com.dena.platform.common.config.DenaConfigReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

/**
 * @author Javad Alimohammadi [<bs.alimohammadi@yahoo.com>]
 */
@Configuration
public class ApplicationContextConfig {

    @Value("${messages.cache_seconds}")
    private int messageCacheSeconds;

    @Value("${messages.use_code_as_default_message}")
    private boolean useCodeAsDefaultMessage;


    @Bean("denaMessageSource")
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasename("classpath:messages/ErrorCodeMessages");
        messageSource.setCacheSeconds(messageCacheSeconds);
        messageSource.setUseCodeAsDefaultMessage(useCodeAsDefaultMessage);
        return messageSource;
    }
}
