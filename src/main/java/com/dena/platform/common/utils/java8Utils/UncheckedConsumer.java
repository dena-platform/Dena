package com.dena.platform.common.utils.java8Utils;

/**
 * @author Javad Alimohammadi [<bs.alimohammadi@gmail.com>]
 */
@FunctionalInterface
public interface UncheckedConsumer<T> {
     void accept(T t) throws Exception;

}
