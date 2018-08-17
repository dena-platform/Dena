package com.dena.platform.config;

import com.dena.platform.common.config.DenaConfigReader;
import com.dena.platform.core.feature.persistence.DenaDataStore;
import com.dena.platform.core.feature.search.Search;
import com.dena.platform.core.feature.search.lucene.LuceneSearch;
import com.dena.platform.restapi.serilizer.DenaResponseModule;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

import javax.annotation.Resource;

/**
 * @author Javad Alimohammadi [<bs.alimohammadi@gmail.com>]
 */
@Configuration
public class ApplicationContextConfig {

    @Value("${messages.cache_seconds}")
    private int messageCacheSeconds;

    @Value("${messages.use_code_as_default_message}")
    private boolean useCodeAsDefaultMessage;

    @Resource
    private DenaConfigReader denaConfigReader;



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

    @Bean(name = "luceneSearch")
    @Autowired
    public Search getLuceneSearch(@Value("${search.lucene.root.dir}") String rootDir,
                                  @Value("${search.lucene.commit.delay}") int commitDelay, DenaDataStore dataStore) {
        return new LuceneSearch(commitDelay, rootDir, dataStore);
    }

}
