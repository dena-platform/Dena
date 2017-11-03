package com.dena.platform.core.feature.datastore;

import com.dena.platform.core.EntityDTO;

/**
 * @author Javad Alimohammadi [<bs.alimohammadi@yahoo.com>]
 */

public interface DenaDataStore {
    void storeObject(EntityDTO entityDTO);

    EntityDTO findObject(Integer objectId);

}
