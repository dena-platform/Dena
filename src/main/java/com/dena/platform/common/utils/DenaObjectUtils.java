package com.dena.platform.common.utils;

import java.time.Instant;

/**
 * @author Javad Alimohammadi [<bs.alimohammadi@gmail.com>]
 */
public class DenaObjectUtils {
    public static String getTypeNamePlural(final String typeName) {
        if (!typeName.endsWith("s")) {
            return typeName + "s";
        } else {
            return typeName;
        }
    }

    public static String getURIForResource(final String typeName, final String id) {
        return "/" + typeName + "/" + id;
    }


    public static long timeStamp() {
        return Instant.now().toEpochMilli();
    }
}
