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

public class BsonTypeMapper {
    private final static Logger log = LoggerFactory.getLogger(BsonTypeMapper.class);

    @SuppressWarnings("unchecked")
    public static BsonValue convertObjectToBSONValue(Object fieldValue) {

        if (fieldValue instanceof List) {
            List<Object> listOfValues = (List<Object>) fieldValue;
            if (((List) fieldValue).size() > 0) {
                List<BsonValue> bsonValueList = new ArrayList<>();
                for (Object val : listOfValues) {
                    bsonValueList.add(convertObjectToBSONValue(val));
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

        log.error("Type of java field [{}] not found in mapping", fieldValue.getClass());

        throw new DataStoreException("Type of field not found", ErrorCode.FIELD_NOT_FOUND_EXCEPTION);

    }


    public static ArrayList<Object> convertBSONArrayToJavaArray(BsonArray bsonArray) {
        ArrayList<Object> returnList = new ArrayList<>();
        bsonArray.forEach(bv -> returnList.add(convertBSONToJava(bv)));

        return returnList;
    }

    public static Object convertBSONToJava(BsonValue bsonValue) {
        if (bsonValue.isObjectId()) {
            return bsonValue.asObjectId().getValue();
        } else if (bsonValue.isNull()) {
            return null;
        } else if (bsonValue.isBinary()) {
            return bsonValue.asBinary().getData();
        } else if (bsonValue.isBoolean()) {
            return bsonValue.asBoolean().getValue();
        } else if (bsonValue.isDateTime()) {
            return bsonValue.asDateTime().getValue();
        } else if (bsonValue.isDecimal128()) {
            return bsonValue.asDecimal128().getValue().bigDecimalValue();
        } else if (bsonValue.isDouble()) {
            return bsonValue.asDouble().getValue();
        } else if (bsonValue.isInt32()) {
            return bsonValue.asInt32().getValue();
        } else if (bsonValue.isInt64()) {
            return bsonValue.asInt64().getValue();
        } else if (bsonValue.isString()) {
            return bsonValue.asString().getValue();
        }

        log.error("Type of bson field [{}] not found in mapping", bsonValue.getBsonType());

        throw new DataStoreException("Type of bson field not found", ErrorCode.FIELD_NOT_FOUND_EXCEPTION);

    }

}
