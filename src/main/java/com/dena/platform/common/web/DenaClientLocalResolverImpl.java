package com.dena.platform.common.web;

import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Locale;
import java.util.Optional;

/**
 * @author Javad Alimohammadi [<bs.alimohammadi@gmail.com>]
 */
@Service
public class DenaClientLocalResolverImpl implements DenaClientLocalResolver {
    private final static String LOCALE_CODE_PARAM = "denaLocalCode";

    private String defaultLocal = "en_US";


    @Override
    public Locale resolveClintLocal(HttpServletRequest httpServletRequest) {

        Optional<String> localCode = DenaHttpRequestUtils.getRequestURLOrHeaderParameter(httpServletRequest,
                LOCALE_CODE_PARAM);

        if (!localCode.isPresent()) {
            localCode = Optional.of(defaultLocal);
        }


        Locale local = new Locale();

        return null;
    }

    public void setDefaultLocal(String defaultLocal) {
        this.defaultLocal = defaultLocal;
    }
}
