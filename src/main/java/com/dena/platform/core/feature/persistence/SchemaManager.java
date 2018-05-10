package com.dena.platform.core.feature.persistence;

/**
 * @author Javad Alimohammadi [<bs.alimohammadi@gmail.com>]
 */
public interface SchemaManager {
    int createSchema(String appName, String schemaName);
}
