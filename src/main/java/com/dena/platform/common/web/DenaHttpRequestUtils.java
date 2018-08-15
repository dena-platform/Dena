package com.dena.platform.common.web;

import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

/**
 * @author Javad Alimohammadi [<bs.alimohammadi@gmail.com>]
 */

public class DenaHttpRequestUtils {

    public static Optional<String> getRequestURLOrHeaderParameter(HttpServletRequest httpServletRequest,
                                                                  final String parameterName) {
        String parameterValue = httpServletRequest.getParameter(parameterName);

        if (StringUtils.isBlank(parameterValue)) {
            parameterValue = httpServletRequest.getHeader(parameterName);
        }

        return Optional.ofNullable(parameterValue);

    }
}
