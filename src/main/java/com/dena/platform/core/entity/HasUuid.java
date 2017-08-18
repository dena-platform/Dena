package com.dena.platform.core.entity;

import java.util.UUID;

/**
 * Interface to be implemented by entities that have a persistent attribute of {@link UUID} type.
 * <p> Borrowed from cuba platform project which is licensed under the Apache License, Version 2.0.
 * See http://www.apache.org/licenses/LICENSE-2.0.
 *
 * @author Javad Alimohammadi<bs.alimohammadi@yahoo.com>
 */

public interface HasUuid {
    UUID getUuid();

    void setUuid(UUID uuid);
}
