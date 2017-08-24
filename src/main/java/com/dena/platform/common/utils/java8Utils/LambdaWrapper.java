package com.dena.platform.common.utils.java8Utils;

import java.util.function.Consumer;

/**
 * @author Javad Alimohammadi<bs.alimohammadi@yahoo.com>
 */

public class LambdaWrapper {
    public static <T> Consumer<T> uncheckedConsumer(UncheckedConsumer<T> consumer) {
        return t -> {
            try {
                consumer.accept(t);
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        };

    }

}
