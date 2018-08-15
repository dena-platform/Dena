package com.dena.platform.common.web;

import javax.servlet.http.HttpServletRequest;
import java.util.Locale;

/**
 * @author Javad Alimohammadi [<bs.alimohammadi@gmail.com>]
 */

public interface DenaClientLocalResolver {
    /**
     * Resolve client local from request
     * @param httpServletRequest
     * @return
     */
    Locale resolveClintLocal(HttpServletRequest httpServletRequest);

}
