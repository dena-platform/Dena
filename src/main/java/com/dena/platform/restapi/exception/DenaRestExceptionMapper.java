package com.dena.platform.restapi.exception;

import com.dena.platform.restapi.ErrorResponse;
import org.springframework.context.MessageSource;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

/**
 * @author Javad Alimohammadi [<bs.alimohammadi@yahoo.com>]
 */
@ControllerAdvice
public class DenaRestExceptionMapper {

    @Resource(name = "messageSource")
    private MessageSource messageSource;

    @ExceptionHandler(DenaRestException.class)
    @ResponseBody
    public ErrorResponse handleDenaRestException(HttpServletRequest request, HttpServletResponse response, DenaRestException ex) {
        response.setStatus(ex.getStatusCode());

        final Locale locale = ex.getLocale() == null ? Locale.getDefault() : ex.getLocale();

        List<String> errorMessage = ex.getMessages().entrySet()
                .stream()
                .map(messageEntry -> messageSource.getMessage(messageEntry.getKey(), messageEntry.getValue(), locale))
                .collect(Collectors.toList());

        ErrorResponse errorResponse = ErrorResponse.ErrorResponseBuilder.anErrorResponse()
                .withStatus(ex.getStatusCode())
                .withErrorCode(ex.getErrorCode())
                .withMessages(errorMessage)
                .build();

        return errorResponse;
    }

}
