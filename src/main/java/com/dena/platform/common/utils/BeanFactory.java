package com.dena.platform.common.utils;

import com.dena.platform.common.exception.DenaInternalException;
import org.springframework.beans.BeanUtils;

/**
 * @author Javad Alimohammadi [<bs.alimohammadi@yahoo.com>]
 */
public final class BeanFactory {
    public static Object createInstance(String className) {
        try {
            Class<?> aClass = Class.forName(className);
            return BeanUtils.instantiate(aClass);
        } catch (ClassNotFoundException e) {
            throw new DenaInternalException("Can not create object", e);
        }
    }

}
