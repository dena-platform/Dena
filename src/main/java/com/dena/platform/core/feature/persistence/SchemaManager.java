package com.dena.platform.core.feature.persistence;

import com.dena.platform.core.dto.DenaObject;

import java.util.List;

/**
 * @author Javad Alimohammadi [<bs.alimohammadi@gmail.com>]
 */
public interface SchemaManager {
    int createSchema(String appName, String schemaName);

    List<DenaObject> findAllSchema(String appName);

    int deleteSchema(String appName, String schemaName);

}
