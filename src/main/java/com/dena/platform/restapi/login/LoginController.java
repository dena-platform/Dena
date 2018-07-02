package com.dena.platform.restapi.login;

import com.dena.platform.restapi.DenaRestProcessor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author Nazarpour.
 */
@RestController

@RequestMapping(produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
public class LoginController {

    @Resource(name = "denaRestEntityProcessorImpl")
    protected DenaRestProcessor denaRestProcessor;

    @PostMapping(path = {"/{app-id}/users/{app-id}"}, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity login() {
        return denaRestProcessor.login();
    }

    @PostMapping(path = {"/logout/{app-id}"}, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity logout() {
        return denaRestProcessor.logout();
    }
}
