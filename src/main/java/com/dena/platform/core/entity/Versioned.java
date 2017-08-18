package com.dena.platform.core.entity;

/**
 * Interface to be implemented by optimistically locked entities.
 * <p> Borrowed from cuba platform project which is licensed under the Apache License, Version 2.0.
 * See http://www.apache.org/licenses/LICENSE-2.0.
 *
 * @author Javad Alimohammadi<bs.alimohammadi@yahoo.com>
 */

public interface Versioned {
    Integer getVersion();

}
