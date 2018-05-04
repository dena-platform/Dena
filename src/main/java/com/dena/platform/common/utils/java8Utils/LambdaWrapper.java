package com.dena.platform.common.utils.java8Utils;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * @author Javad Alimohammadi [<bs.alimohammadi@gmail.com>]
 */

public class LambdaWrapper {
    public static <T> Consumer<T> uncheckedConsumer(UncheckedConsumer<T> consumer) throws RuntimeException {
        return t -> {
            try {
                consumer.accept(t);
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        };
    }

    /**
     * Create an {@link UncheckedBiConsumer} instance
     * @param uncheckedBiConsumer
     * @param <T>
     * @param <E>
     * @return
     * @throws RuntimeException
     */
    public static <T, E> BiConsumer<T, E> uncheckedBiConsumer(UncheckedBiConsumer<T, E> uncheckedBiConsumer) throws RuntimeException {
        return (fieldName, fieldValue) -> {
            try {
                uncheckedBiConsumer.accept(fieldName, fieldValue);
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        };

    }

}
