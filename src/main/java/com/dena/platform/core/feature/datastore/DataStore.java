package com.dena.platform.core.feature.datastore;

import com.dena.platform.restapi.EntityDTO;

/**
 * @author Javad Alimohammadi<bs.alimohammadi@yahoo.com>
 */

public interface DataStore {
    void storeObject(EntityDTO entityDTO);
    
}
