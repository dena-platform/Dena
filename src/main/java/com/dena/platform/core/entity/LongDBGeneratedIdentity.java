package com.dena.platform.core.entity;

import javax.persistence.*;

/**
 * Entity with long identity generated value
 *
 * @author Javad Alimohammadi [<bs.alimohammadi@yahoo.com>]
 */
@MappedSuperclass
public abstract class LongDBGeneratedIdentity extends BaseDBGeneratedIdEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id")
    protected Long id;


    @Override
    protected Long getDbGeneratedId() {
        return id;
    }
}
