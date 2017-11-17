package com.dena.platform.restapi;

import com.dena.platform.restapi.exception.DenaRestException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Javad Alimohammadi [<bs.alimohammadi@yahoo.com>]
 */
@ControllerAdvice
public class DenaRestExceptionMapper {

    @ExceptionHandler(DenaRestException.class)
    @ResponseBody
    public ErrorResponse handleDenaRestException(HttpServletRequest request, HttpServletResponse response, DenaRestException ex) {
        response.setStatus(ex.getStatusCode());

        ErrorResponse errorResponse = ErrorResponse.ErrorResponseBuilder.anErrorResponse()
                .withStatus(ex.getStatusCode())
                .withMessage()
                .build();


    }

}
