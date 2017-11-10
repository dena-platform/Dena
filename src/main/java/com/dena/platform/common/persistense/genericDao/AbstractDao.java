package com.dena.platform.common.persistense.genericDao;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.List;

/**
 * @author Javad Alimohammadi [<bs.alimohammadi@yahoo.com>]
 */
public abstract class AbstractDao<T> implements DAO<T> {

    @PersistenceContext(type = PersistenceContextType.TRANSACTION)
    protected EntityManager entityManager;


    private Class<T> domainClass;

    @SuppressWarnings("unchecked")
    private Class<T> getDomainClass() {
        if (domainClass == null) {
            ParameterizedType thisType = (ParameterizedType) getClass()   //  Gets the current concrete class Class instance.
                    .getGenericSuperclass(); // Get superclass type parameter
            domainClass = (Class<T>) thisType.getActualTypeArguments()[0]; // Gets the array of types mapped into our generics
        }

        return domainClass;
    }

    private String getDomainClassName() {
        Class<T> domainClassTemp = getDomainClass();
        return domainClassTemp.getName();
    }

    public EntityManager getEntityManager() {
        return entityManager;
    }


    @Override
    public T create(T object) {
        return entityManager.merge(object);
    }

    @Override
    public T find(Serializable id) {
        return entityManager.find(getDomainClass(), id);
    }

    @Override
    public T load(Serializable id) {
        return entityManager.getReference(getDomainClass(), id);
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<T> getAll() {
        return entityManager.createQuery("FROM " + getDomainClassName()).getResultList();
    }

    @Override
    public T saveOrUpdate(T t) {
        return entityManager.merge(t);
    }

    @Override
    public void delete(T t) {
        entityManager.remove(t);
    }

    @Override
    public void deleteById(Serializable id) {
        delete(load(id));
    }

    @Override
    public long deleteAll() {
        return entityManager.createQuery("DELETE " + getDomainClassName()).executeUpdate();
    }

    @Override
    public long count() {
        return (Long) entityManager.createQuery("SELECT COUNT(*) FROM " + getDomainClassName()).getSingleResult();
    }

    @Override
    public boolean exists(Serializable id) {
        return find(id) == null;
    }
}

