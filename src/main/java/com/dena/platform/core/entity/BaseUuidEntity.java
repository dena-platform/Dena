package com.dena.platform.core.entity;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.util.UUID;

/**
 * Base class for persistent entities with UUID identifier.
 * <p>
 * Inherit from it if you need an entity without optimistic locking, create, update and soft deletion info.
 * 
 * <p> Borrowed from cuba platform project which is licensed under the Apache License, Version 2.0.
 * See http://www.apache.org/licenses/LICENSE-2.0.
 */
@MappedSuperclass
public class BaseUuidEntity implements BaseEntity<UUID> {
    @Id
    @Column(name = "ID")
    protected UUID id;


    @Override
    public UUID getId() {
        return id;
    }
}
