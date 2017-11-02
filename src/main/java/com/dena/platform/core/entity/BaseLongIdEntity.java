package com.dena.platform.core.entity;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

/**
 * @author Javad Alimohammadi [<bs.alimohammadi@yahoo.com>]
 */
@MappedSuperclass
public class BaseLongIdEntity implements BaseEntity<Long> {
    @Id
    @Column(name = "ID")
    protected Long id;


    @Override
    public Long getId() {
        return id;
    }
}
