package com.dena.platform.common.config;

import org.apache.commons.lang3.StringUtils;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author Javad Alimohammadi [<bs.alimohammadi@yahoo.com>]
 */
@Component("denaDenaConfigReader")
public class DenaConfigReader {
    @Resource
    private static Environment env;


    public static String readProperty(String povertyName) {
        return readProperty(povertyName, "");
    }


    public static String readProperty(String povertyName, String defaultValue) {
        return env.getProperty(povertyName, defaultValue);
    }


    public static boolean readBooleanProperty(String povertyName) {
        return readBooleanProperty(povertyName, false);
    }

    public static boolean readBooleanProperty(String povertyName, boolean defaultValue) {
        return env.getProperty(povertyName, Boolean.class, defaultValue);
    }


}
