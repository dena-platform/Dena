package com.dena.platform.config;

import com.dena.platform.common.config.DenaConfigReader;
import com.fasterxml.jackson.databind.MapperFeature;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

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

    @Bean
    public Jackson2ObjectMapperBuilder objectMapperBuilder() {

        Jackson2ObjectMapperBuilder builder = new Jackson2ObjectMapperBuilder();
        builder.featuresToEnable(MapperFeature.SORT_PROPERTIES_ALPHABETICALLY);

        return builder;
    }
}
