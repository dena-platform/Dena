package com.dena.platform.core.entity;

import javax.persistence.MappedSuperclass;

/**
 * @author Javad Alimohammadi [<bs.alimohammadi@yahoo.com>]
 */

/**
 * This is base entity for generating database identifier
 * <p>
 * <p> Borrowed from cuba platform project which is licensed under the Apache License, Version 2.0.
 * See http://www.apache.org/licenses/LICENSE-2.0.
 */
@MappedSuperclass
public abstract class BaseDBGeneratedIdEntity<T extends Number> implements BaseEntity<T> {

    @Override
    public T getId() {
        return getDbGeneratedId();
    }

    protected abstract T getDbGeneratedId();
}
