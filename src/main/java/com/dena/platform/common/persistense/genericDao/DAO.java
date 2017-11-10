package com.dena.platform.common.persistense.genericDao;

import java.io.Serializable;
import java.util.List;

/**
 * @author Javad Alimohammadi [<bs.alimohammadi@yahoo.com>]
 */
public interface DAO<T> {

    public T create(T object);

    /**
     * Find item by id and return it or null otherwise
     *
     * @param id
     * @return
     */
    public T find(Serializable id);

    /**
     * Load lazy item with id if not found throws exception
     *
     * @param id
     * @return
     */
    public T load(Serializable id);

    public List<T> getAll();

    public T saveOrUpdate(T t);

    public void delete(T t);

    public void deleteById(Serializable id);

    public long deleteAll();

    public long count();

    public boolean exists(Serializable id);
}
