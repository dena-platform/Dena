package com.dena.platform.restapi.endpoint;

import com.dena.platform.common.exception.InvalidFormatException;
import com.dena.platform.core.DenaRequestContext;
import com.dena.platform.core.feature.datastore.exception.RelationInvalidException;
import com.dena.platform.core.feature.datastore.exception.DataStoreException;
import com.dena.platform.restapi.RestEntityProcessor;
import com.dena.platform.restapi.exception.DenaRestException.DenaRestExceptionBuilder;
import com.dena.platform.restapi.exception.ErrorCodes;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Javad Alimohammadi [<bs.alimohammadi@yahoo.com>]
 */
@RestController
@RequestMapping(value = API.API_PATH, produces = {MediaType.APPLICATION_JSON_VALUE}, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class API {

    public final static String API_PATH = "/v1/";

    public static final String APP_ID = "app-id";
    public static final String TYPE_NAME = "type-name";

    @Resource(name = "denaRestEntityProcessorImpl")
    private RestEntityProcessor restEntityProcessor;


    @PostMapping(path = "{app-id}/{type-name}")
    public ResponseEntity createObjects(HttpServletRequest request) {
        DenaRequestContext denaRequestContext = new DenaRequestContext(request);
        try {
            return restEntityProcessor.processRestRequest(denaRequestContext);

        } catch (InvalidFormatException ex) {
            throw DenaRestExceptionBuilder.aDenaRestException()
                    .withStatusCode(HttpServletResponse.SC_BAD_REQUEST)
                    .withErrorCode(ErrorCodes.INVALID_REQUEST.getErrorCode())
                    .addMessageCode(ErrorCodes.INVALID_REQUEST.getMessageCode(), null)
                    .withCause(ex.getCause())
                    .build();

        } catch (DataStoreException ex) {
            if (ex.getCause() instanceof RelationInvalidException) {
                throw DenaRestExceptionBuilder.aDenaRestException()
                        .withStatusCode(HttpServletResponse.SC_BAD_REQUEST)
                        .withErrorCode(ErrorCodes.RELATION_INVALID_EXCEPTION.getErrorCode())
                        .addMessageCode(ErrorCodes.RELATION_INVALID_EXCEPTION.getMessageCode(), null)
                        .withCause(ex.getCause())
                        .build();
            }
            throw DenaRestExceptionBuilder.aDenaRestException()
                    .withStatusCode(HttpServletResponse.SC_BAD_REQUEST)
                    .withErrorCode(ErrorCodes.GENERAL_DATA_STORE_EXCEPTION.getErrorCode())
                    .addMessageCode(ErrorCodes.GENERAL_DATA_STORE_EXCEPTION.getMessageCode(), null)
                    .withCause(ex.getCause())
                    .build();
        }
    }


    @GetMapping
    public String findEntity(HttpServletRequest request) {
        DenaRequestContext denaRequestContext = new DenaRequestContext(request);
        restEntityProcessor.processRestRequest(denaRequestContext);

        return null;
    }
}
