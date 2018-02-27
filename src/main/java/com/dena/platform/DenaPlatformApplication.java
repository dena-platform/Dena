package com.dena.platform;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DenaPlatformApplication {
    private final static Logger log = LoggerFactory.getLogger(DenaPlatformApplication.class);

    public static void main(String[] args) {

        SpringApplication.run(DenaPlatformApplication.class, args);
    }

}
