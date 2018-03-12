package com.dena.platform.config;

import com.dena.platform.restapi.Serilizer.DenaResponseModule;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.web.filter.CommonsRequestLoggingFilter;

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

    @Bean(name = "jacksonObjectMapper")
    public ObjectMapper objectMapper() {
        final ObjectMapper JSON_MAPPER = new ObjectMapper();

        JSON_MAPPER.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
        JSON_MAPPER.registerModule(new DenaResponseModule());


        return JSON_MAPPER;
    }
}
