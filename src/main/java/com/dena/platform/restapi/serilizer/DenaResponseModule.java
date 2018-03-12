package com.dena.platform.restapi.serilizer;

import com.dena.platform.restapi.dto.response.DenaObjectResponse;
import com.fasterxml.jackson.core.util.VersionUtil;
import com.fasterxml.jackson.databind.module.SimpleModule;

/**
 * @author Javad Alimohammadi [<bs.alimohammadi@yahoo.com>]
 */
public class DenaResponseModule extends SimpleModule {
    private static final String NAME = DenaResponseModule.class.getSimpleName();
    private static final VersionUtil VERSION_UTIL = new VersionUtil() {
    };

    public DenaResponseModule() {
        super(NAME, VERSION_UTIL.version());
        addSerializer(DenaObjectResponse.class, new DenaResponseSerializer());
    }
}
