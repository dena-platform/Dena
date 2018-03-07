package com.dena.platform.core.feature.persistence.mongodb;

import org.bson.*;
import org.bson.types.ObjectId;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Javad Alimohammadi [<bs.alimohammadi@yahoo.com>]
 */

public class BsonValueTypeMapper {

    private final static Map<Class<?>, Class<? extends BsonValue>> mapping = new HashMap<>();


    static {
        mapping.put(List.class, BsonArray.class);
        mapping.put(String.class, BsonString.class);
        mapping.put(Integer.class, BsonInt32.class);
        mapping.put(Long.class, BsonInt32.class);
        mapping.put(BigDecimal.class, BsonDecimal128.class);
        mapping.put(Double.class, BsonDouble.class);
        mapping.put(Boolean.class, BsonBoolean.class);
        mapping.put(ObjectId.class, BsonObjectId.class);
    }

    public static BsonValue createBsonValue(Object fieldValue) {

        if (fieldValue instanceof List) {
            new BsonArray();
        }

    }


}
