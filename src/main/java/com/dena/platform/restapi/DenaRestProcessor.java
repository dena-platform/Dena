package com.dena.platform.restapi;

import com.dena.platform.common.web.DenaRequestContext;
import org.springframework.http.ResponseEntity;

/**
 * @author Javad Alimohammadi [<bs.alimohammadi@yahoo.com>]
 */
public interface DenaRestProcessor {

    ResponseEntity handleCreateObject(DenaRequestContext denaRequestContext);

    ResponseEntity handleUpdateObject(DenaRequestContext denaRequestContext);

    ResponseEntity handleDeleteRelation(DenaRequestContext denaRequestContext);

    ResponseEntity handleDeleteObject(DenaRequestContext denaRequestContext);

    ResponseEntity handleFindObject(DenaRequestContext denaRequestContext);

}
