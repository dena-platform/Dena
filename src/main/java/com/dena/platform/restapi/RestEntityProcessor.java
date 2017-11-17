package com.dena.platform.restapi;

import com.dena.platform.core.DenaRequestContext;

/**
 * @author Javad Alimohammadi [<bs.alimohammadi@yahoo.com>]
 */
public interface RestEntityProcessor {

    void processRestRequest(DenaRequestContext denaRequestContext);

}
