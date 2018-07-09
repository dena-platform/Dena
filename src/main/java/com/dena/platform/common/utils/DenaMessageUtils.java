package com.dena.platform.common.utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

/**
 * @author Javad Alimohammadi [<bs.alimohammadi@gmail.com>]
 */
@Component("denaMessageUtils")
public final class DenaMessageUtils implements ApplicationContextAware {
    private static ApplicationContext applicationContext;

    public static String getMessage(String code) {
        return getMessage(code, (Object) null);
    }

    public static String getMessage(String code, String defaultMessage) {
        return getMessage(code, defaultMessage, (Object) null);
    }

    // todo: change default local to get it from user request
    public static String getMessage(String code, Object... args) {
        return getMessageSource().getMessage(code, args, Locale.getDefault());
    }

    public static String getMessage(String code, String defaultMessage, Object... args) {
        return getMessageSource().getMessage(code, args, defaultMessage, Locale.getDefault());
    }


    protected static MessageSource getMessageSource() {
        return (MessageSource) applicationContext.getBean("messageSource");
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        DenaMessageUtils.applicationContext = applicationContext;
    }
}
