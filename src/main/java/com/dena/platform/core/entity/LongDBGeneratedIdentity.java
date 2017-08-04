package com.dena.platform.core.entity;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * Entity with int identity generated value
 *
 * @author Javad Alimohammadi<bs.alimohammadi@yahoo.com>
 */
public class LongDBGeneratedIdentity<E extends Number> extends BaseDBGeneratedIdentity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id")
    protected Long id;


    @Override
    protected Long getDbGeneratedId() {
        return id;
    }
}
