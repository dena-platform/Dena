package com.dena.platform.restapi;

import org.springframework.http.ResponseEntity;

/**
 * @author Javad Alimohammadi [<bs.alimohammadi@gmail.com>]
 */
public interface DenaRestProcessor {

    ResponseEntity handleCreateObject();

    ResponseEntity handleUpdateObject();

    ResponseEntity handleDeleteRelation();

    ResponseEntity handleDeleteObject();

    ResponseEntity handleFindObject();

    ResponseEntity handleRegisterUser();

    ResponseEntity handleGetAllSchema();

    ResponseEntity login();

    ResponseEntity logout();

    ResponseEntity handleRegisterApplication();

    ResponseEntity handleCreateSchema();
}
