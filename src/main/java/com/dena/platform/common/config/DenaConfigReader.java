package com.dena.platform.common.config;

import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author Javad Alimohammadi [<bs.alimohammadi@gmail.com>]
 */
@Component("denaConfigReader")
public class DenaConfigReader {


    private static Environment env;

    @Resource
    public void setEnv(Environment env) {
        DenaConfigReader.env = env;
    }

    public static String readProperty(final String povertyName) {
        return readProperty(povertyName, "");
    }

    public static String readProperty(final String povertyName, final String defaultValue) {
        return env.getProperty(povertyName, defaultValue);
    }

    public static boolean readBooleanProperty(final String povertyName) {
        return readBooleanProperty(povertyName, false);
    }

    public static boolean readBooleanProperty(final String povertyName, final boolean defaultValue) {
        return env.getProperty(povertyName, Boolean.class, defaultValue);
    }

    public static long readLongProperty(final String povertyName) {
        return readLongProperty(povertyName, 0L);
    }

    public static long readLongProperty(final String povertyName, long defaultValue) {
        return env.getProperty(povertyName, Long.class, defaultValue);
    }

    public static int readIntProperty(final String povertyName) {
        return readIntProperty(povertyName, 0);
    }

    public static int readIntProperty(final String povertyName, int defaultValue) {
        return env.getProperty(povertyName, Integer.class, defaultValue);
    }


}
