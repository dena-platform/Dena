package com.dena.platform.core.feature.persistence.mongodb;

import com.dena.platform.common.exception.ErrorCode;
import com.dena.platform.core.feature.persistence.exception.DataStoreException;
import org.bson.*;
import org.bson.types.Decimal128;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author Javad Alimohammadi [<bs.alimohammadi@yahoo.com>]
 */

public class BsonValueTypeMapper {
    private final static Logger log = LoggerFactory.getLogger(BsonValueTypeMapper.class);

    @SuppressWarnings("unchecked")
    public static BsonValue createBsonValue(Object fieldValue) {

        if (fieldValue instanceof List) {
            List<Object> listOfValues = (List<Object>) fieldValue;
            if (((List) fieldValue).size() > 0) {
                List<BsonValue> bsonValueList = new ArrayList<>();
                for (Object val : listOfValues) {
                    bsonValueList.add(createBsonValue(val));
                }
                return new BsonArray(bsonValueList);
            }
        } else if (fieldValue instanceof String) {
            return new BsonString((String) fieldValue);
        } else if (fieldValue instanceof Integer) {
            return new BsonInt32((Integer) fieldValue);
        } else if (fieldValue instanceof Long) {
            return new BsonInt64((Long) fieldValue);
        } else if (fieldValue instanceof BigDecimal) {
            return new BsonDecimal128(new Decimal128((BigDecimal) fieldValue));
        } else if (fieldValue instanceof Double) {
            return new BsonDouble((Double) fieldValue);
        } else if (fieldValue instanceof Boolean) {
            return new BsonBoolean((Boolean) fieldValue);
        } else if (fieldValue instanceof ObjectId) {
            return new BsonObjectId((ObjectId) fieldValue);
        } else if (Objects.isNull(fieldValue)) {
            return new BsonNull();
        }

        log.error("Type of field [{}] not found in mapping", fieldValue.getClass());

        throw new DataStoreException("Type of field not found", ErrorCode.FIELD_NOT_FOUND_EXCEPTION);


    }


}
