package com.dena.platform.core.entity;

import javax.persistence.Column;
import javax.persistence.Id;

/**
 * @author Javad Alimohammadi<bs.alimohammadi@yahoo.com>
 */
public class BaseLongIdEntity implements BaseEntity<Long> {
    @Id
    @Column(name = "ID")
    protected Long id;


    @Override
    public Long getId() {
        return id;
    }
}
