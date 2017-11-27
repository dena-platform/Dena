package com.dena.platform.common.utils;

/**
 * @author Javad Alimohammadi [<bs.alimohammadi@yahoo.com>]
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
        return System.currentTimeMillis();
    }
}
