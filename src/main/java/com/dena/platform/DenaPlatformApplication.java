package com.dena.platform;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;

@SpringBootApplication
@ImportResource("/spring/hsql_cfg.xml")
public class DenaPlatformApplication {

    public static void main(String[] args) {
        SpringApplication.run(DenaPlatformApplication.class, args);
    }

}
