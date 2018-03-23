package com.dena.platform.utils;

/**
 * @author Javad Alimohammadi [<bs.alimohammadi@yahoo.com>]
 */

public interface CommonConfig {
    String APP_ID = "denaTestDB";

    String COLLECTION_NAME = "denaTestCollection";

    String RELATION_NAME = "test_relation_name";

    String BASE_URL = "/v1/" + APP_ID + "/" + COLLECTION_NAME;

    String REGISTER_URL = "/v1/" + APP_ID + "/users/register";
}
