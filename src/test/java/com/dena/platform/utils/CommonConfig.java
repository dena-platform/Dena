package com.dena.platform.utils;

/**
 * @author Javad Alimohammadi [<bs.alimohammadi@yahoo.com>]
 */

public interface CommonConfig {
    String ROOT_URL = "/v1/";

    String APP_ID = "denaTestDB";

    String COLLECTION_NAME = "denaTestCollection";

    String RELATION_NAME = "test_relation_name";

    String BASE_URL = ROOT_URL + APP_ID + "/" + COLLECTION_NAME;

    String REGISTER_URL = ROOT_URL + APP_ID + "/users/register";

    String LOGIN_URL = "login";
}
