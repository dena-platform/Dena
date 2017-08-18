package com.dena.platform.core.entity;

import com.dena.platform.common.utils.UUIDGenerator;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Version;
import java.util.UUID;

/**
 * Base class for persistent entities with UUID identifier.
 * <p>
 * Inherit from it if you need an entity without optimistic locking, create, update and soft deletion info.
 * <p>
 * <p> Borrowed from cuba platform project which is licensed under the Apache License, Version 2.0.
 * See http://www.apache.org/licenses/LICENSE-2.0.
 */
@MappedSuperclass
public class BaseUuidEntity implements BaseEntity<UUID>, HasUuid, Versioned {
    @Id
    @Column(name = "ID")
    protected UUID id;

    @Version
    @Column(name = "VERSION", nullable = false)
    protected Integer version;

    public BaseUuidEntity() {
        id = UUIDGenerator.createNewUUID();
    }

    @Override
    public UUID getId() {
        return id;
    }

    @Override
    public UUID getUuid() {
        return id;
    }

    @Override
    public void setUuid(UUID uuid) {
        this.id = uuid;
    }

    @Override
    public Integer getVersion() {
        return version;
    }

}
