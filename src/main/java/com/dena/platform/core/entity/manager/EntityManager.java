package com.dena.platform.core.entity.manager;

import com.dena.platform.core.entity.BaseEntity;

/**
 * @auther Javad Alimohammadi<bs.alimohammadi@yahoo.com>
 */
public interface EntityManager {
    BaseEntity saveOrUpdateEntity(BaseEntity baseEntity);

    BaseEntity findEntity(BaseEntity baseEntity);

    boolean removeEntityById(long id);


}
