package com.dena.platform.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import javax.sql.DataSource;


/**
 * @auther Javad Alimohammadi<bs.alimohammadi@yahoo.com>
 */
@Configuration
public class DataAccessConfig {

    @Profile("dev")
    @Bean
    public DataSource dataSourceDev() {
        EmbeddedDatabaseBuilder builder = new EmbeddedDatabaseBuilder();
        EmbeddedDatabase db = builder.setType(EmbeddedDatabaseType.HSQL).build();
        return db;
    }

    // TODO: 2017-07-17 change data source for production config
    @Profile("production")
    @Bean
    public DataSource dataSourceProduction() {
        EmbeddedDatabaseBuilder builder = new EmbeddedDatabaseBuilder();
        EmbeddedDatabase db = builder.setType(EmbeddedDatabaseType.HSQL).build();
        return db;
    }


}
