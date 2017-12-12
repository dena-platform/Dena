package com.dena.platform.utils;

/**
 * @author Javad Alimohammadi [<bs.alimohammadi@yahoo.com>]
 */

public interface CommonConfig {
    String dbName = "denaTestDB";
    String collectionName = "denaTestCollection";
    String baseURL = "/v1/" + dbName + "/" + collectionName;
}
