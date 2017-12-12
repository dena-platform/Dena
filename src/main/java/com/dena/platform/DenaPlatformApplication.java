package com.dena.platform;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.mongo.embedded.EmbeddedMongoAutoConfiguration;

@SpringBootApplication(exclude = {EmbeddedMongoAutoConfiguration.class})
public class DenaPlatformApplication {

    public static void main(String[] args) {
        SpringApplication.run(DenaPlatformApplication.class, args);
    }

}
