package com.dena.platform.restapi;

import com.dena.platform.core.DenaRequestContext;
import org.springframework.http.ResponseEntity;

import java.net.URISyntaxException;

/**
 * @author Javad Alimohammadi [<bs.alimohammadi@yahoo.com>]
 */
public interface RestEntityProcessor {

    ResponseEntity processRestRequest(DenaRequestContext denaRequestContext);

    ResponseEntity handlePostRequest(DenaRequestContext denaRequestContext);

    ResponseEntity handlePutRequest(DenaRequestContext denaRequestContext);

    ResponseEntity handleDeleteRelation(DenaRequestContext denaRequestContext);

    ResponseEntity handleDeleteRequest(DenaRequestContext denaRequestContext);

    ResponseEntity handleFindObject(DenaRequestContext denaRequestContext);

}
