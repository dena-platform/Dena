package com.dena.platform.core.feature.datastore.utils;

/**
 * @author Javad Alimohammadi [<bs.alimohammadi@yahoo.com>]
 */
public class DataStoreUtils {
    public static String makeTypeNamePlural(final String typeName) {
        if (!typeName.endsWith("s")) {
            return typeName + "s";
        } else {
            return typeName;
        }
    }

    public static String makeURIForResource(final String typeName, final String resourceId) {
        return "/" + typeName + "/" + resourceId;
    }
}
