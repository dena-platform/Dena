package com.dena.platform.common.web.filter;

import org.springframework.boot.autoconfigure.security.SecurityProperties;

/**
 * @author Javad Alimohammadi [<bs.alimohammadi@gmail.com>]
 */

public final class FilterOrder {

    public static final int PRE_SECURITY_ORDER = SecurityProperties.DEFAULT_FILTER_ORDER - 5000;

}
