package com.dena.platform.utils;

/**
 * @author Javad Alimohammadi [<bs.alimohammadi@yahoo.com>]
 */

public interface CommonConfig {
    String ROOT_URL = "/v1/";

    String APP_ID = "denaTestDB";

    String DENA_APLICATION = "DENA_APPLICATIONS";

    String COLLECTION_NAME = "denaTestCollection";

    String RELATION_NAME = "test_relation_name";

    String BASE_URL = ROOT_URL + APP_ID + "/" + COLLECTION_NAME;

    String REGISTER_USER_URL = ROOT_URL + APP_ID + "/users/register";

    String REGISTER_APPLICATION_URL= ROOT_URL  + "/app/register";

    String LOGIN_URL = "login";
}
