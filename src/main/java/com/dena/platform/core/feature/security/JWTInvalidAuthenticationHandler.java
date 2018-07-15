package com.dena.platform.core.feature.security;

import com.dena.platform.common.exception.ErrorCode;
import com.dena.platform.common.web.JSONMapper;
import com.dena.platform.restapi.dto.response.ErrorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Locale;

/**
 * @author Nazarpour.
 */
@Component
public class JWTInvalidAuthenticationHandler implements AuthenticationEntryPoint {
    private final static Logger log = LoggerFactory.getLogger(JWTInvalidAuthenticationHandler.class);

    @Resource(name = "denaMessageSource")
    private MessageSource messageSource;


    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        log.error("Error in authenticate", authException);
        final Locale locale = Locale.getDefault();
        String message = messageSource.getMessage(ErrorCode.TOKEN_INVALID.getMessageCode(), null, locale);

        ErrorResponse errorResponse = ErrorResponse.ErrorResponseBuilder.anErrorResponse()
                .withStatus(HttpServletResponse.SC_UNAUTHORIZED)
                .withErrorCode(ErrorCode.TOKEN_INVALID.getErrorCode())
                .withMessages(message)
                .build();

        response.setStatus(HttpStatus.FORBIDDEN.value());
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        response.getWriter().print(JSONMapper.createJSON(errorResponse));

    }
}
