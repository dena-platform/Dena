package com.dena.platform.restapi;

import org.springframework.http.ResponseEntity;

/**
 * @author Javad Alimohammadi [<bs.alimohammadi@yahoo.com>]
 */
public interface DenaRestProcessor {

    ResponseEntity handleCreateObject();

    ResponseEntity handleUpdateObject();

    ResponseEntity handleDeleteRelation();

    ResponseEntity handleDeleteObject();

    ResponseEntity handleFindObject();

    ResponseEntity handleRegisterAPPUser();

}
