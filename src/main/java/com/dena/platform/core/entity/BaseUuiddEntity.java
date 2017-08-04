package com.dena.platform.core.entity;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.util.UUID;

/**
 * @author Javad Alimohammadi<bs.alimohammadi@yahoo.com>
 */
@MappedSuperclass
public class BaseUuiddEntity implements BaseEntity<UUID> {
    @Id
    @Column(name = "ID")
    protected UUID id;


    @Override
    public UUID getId() {
        return id;
    }
}
                                        