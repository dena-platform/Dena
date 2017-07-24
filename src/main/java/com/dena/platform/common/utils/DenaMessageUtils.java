package com.dena.platform.common.utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.util.Locale;

/**
 * @auther Javad Alimohammadi<bs.alimohammadi@yahoo.com>
 */
@Service("denaMessageUtils")
public final class DenaMessageUtils implements ApplicationContextAware {
    private static ApplicationContext applicationContext;

    public static String getMessage(String code) {
        return getMessage(code, (Object) null);
    }

    // todo: change default local to get it from user request
    public static String getMessage(String code, Object... args) {
        return getMessageSource().getMessage(code, args, Locale.getDefault());
    }

    protected static MessageSource getMessageSource() {
        return (MessageSource) applicationContext.getBean("messageSource");
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        DenaMessageUtils.applicationContext = applicationContext;
    }
}
