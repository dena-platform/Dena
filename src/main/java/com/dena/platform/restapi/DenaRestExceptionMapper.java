package com.dena.platform.restapi;

import com.dena.platform.restapi.exception.DenaRestException;
import org.springframework.context.MessageSource;
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


    private MessageSource messageSource;

    @ExceptionHandler(DenaRestException.class)
    @ResponseBody
    public ErrorResponse handleDenaRestException(HttpServletRequest request, HttpServletResponse response, DenaRestException ex) {
        response.setStatus(ex.getStatusCode());

        

        ErrorResponse errorResponse = ErrorResponse.ErrorResponseBuilder.anErrorResponse()
                .withStatus(ex.getStatusCode())
                .withErrorCode(ex.getMessageCode())
                .withMessage(ex.getMessage())
                .build();


    }

}
