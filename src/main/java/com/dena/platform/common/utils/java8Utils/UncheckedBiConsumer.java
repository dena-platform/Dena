package com.dena.platform.common.utils.java8Utils;

/**
 * This is {@link java.util.function.BiConsumer} clone with the difference
 * that functional method in this class can throw exception.
 *
 * @author Javad Alimohammadi [<bs.alimohammadi@gmail.com>]
 */


@FunctionalInterface
public interface UncheckedBiConsumer<T, E> {
    void accept(T t, E u) throws Exception;
}
