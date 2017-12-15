package com.dena.platform.utils;

/**
 * @author Javad Alimohammadi [<bs.alimohammadi@yahoo.com>]
 */

public interface CommonConfig {
    String DB_NAME = "denaTestDB";
    String COLLECTION_NAME = "denaTestCollection";
    String BASE_URL = "/v1/" + DB_NAME + "/" + COLLECTION_NAME;
}
