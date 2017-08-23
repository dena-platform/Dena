package com.dena.platform.core.feature.datastore;

import com.dena.platform.restapi.EntityDTO;

/**
 * @author Javad Alimohammadi<bs.alimohammadi@yahoo.com>
 */

public interface DenaDataStore {
    void storeObject(EntityDTO entityDTO);

    EntityDTO findObject(long objectId);

}
