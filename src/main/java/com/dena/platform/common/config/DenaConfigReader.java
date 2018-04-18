package com.dena.platform.common.config;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author Javad Alimohammadi [<bs.alimohammadi@gmail.com>]
 */
@Service("denaConfigReader")
@Lazy(value = false)
public class DenaConfigReader implements ApplicationContextAware {


    private static Environment env;

    private static ApplicationContext applicationContext;

    @Resource
    public void setEnv(Environment env) {
        DenaConfigReader.env = env;
    }

    public static String readProperty(final String povertyName) {
        return readProperty(povertyName, "");
    }

    public static String readProperty(final String povertyName, final String defaultValue) {
        return getDenaConfigReader().env.getProperty(povertyName, defaultValue);
    }

    public static boolean readBooleanProperty(final String povertyName) {
        return readBooleanProperty(povertyName, false);
    }

    public static boolean readBooleanProperty(final String povertyName, final boolean defaultValue) {
        return getDenaConfigReader().env.getProperty(povertyName, Boolean.class, defaultValue);
    }

    public static long readLongProperty(final String povertyName) {
        return readLongProperty(povertyName, 0L);
    }

    public static long readLongProperty(final String povertyName, long defaultValue) {
        return getDenaConfigReader().env.getProperty(povertyName, Long.class, defaultValue);
    }

    public static int readIntProperty(final String povertyName) {
        return readIntProperty(povertyName, 0);
    }

    public static int readIntProperty(final String povertyName, int defaultValue) {
        return getDenaConfigReader().env.getProperty(povertyName, Integer.class, defaultValue);
    }

    private static DenaConfigReader getDenaConfigReader() {
        return applicationContext.getBean(DenaConfigReader.class);
    }


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        DenaConfigReader.applicationContext = applicationContext;
    }
}
