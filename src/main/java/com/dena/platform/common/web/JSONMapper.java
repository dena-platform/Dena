package com.dena.platform.common.web;

import com.dena.platform.common.exception.ErrorCode;
import com.dena.platform.common.exception.InvalidJSONException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

/**
 * @author Javad Alimohammadi [<bs.alimohammadi@gmail.com>]
 */
@Component
public class JSONMapper {


    private static ObjectMapper JSON_MAPPER;

    @Autowired
    public JSONMapper(ObjectMapper objectMapper) {
        JSONMapper.JSON_MAPPER = objectMapper;
    }

    public static String createJSON(final Object object) throws InvalidJSONException {
        try {

            return JSON_MAPPER.writeValueAsString(object);
        } catch (JsonProcessingException ex) {
            String errMessage = String.format("Error in converting from Class [%s] to JSON", object.getClass().getSimpleName());
            throw new InvalidJSONException(errMessage, ErrorCode.INVALID_REQUEST);
        }
    }

    public static <T> List<T> createListObjectsFromJSON(final String jsonString, final Class<T> classType) throws InvalidJSONException {
        try {
            return JSON_MAPPER.readValue(jsonString, TypeFactory.defaultInstance().constructCollectionType(List.class, classType));
        } catch (IOException ex) {
            String errMessage = String.format("Error in converting from JSON [%s] to class [%s]", jsonString, classType);
            throw new InvalidJSONException(errMessage, ErrorCode.INVALID_REQUEST);
        }
    }

    public static <T> T createObjectFromJSON(final String jsonString, final Class<T> classType) throws InvalidJSONException {
        try {
            return JSON_MAPPER.readValue(jsonString, classType);
        } catch (IOException ex) {
            String errMessage = String.format("Error in converting from JSON [%s] to class [%s]", jsonString, classType);
            throw new InvalidJSONException(errMessage, ErrorCode.INVALID_REQUEST, ex);
        }
    }

    public static HashMap<String, Object> createHashMapFromObject(final Object object) {
        HashMap<String, Object> map;
        try {
            map = JSON_MAPPER.convertValue(object, new TypeReference<HashMap<String, Object>>() {
            });
            return map;
        } catch (IllegalArgumentException ex) {
            String errMessage = String.format("Error in converting from Object [%s] to class [%s]", object, HashMap.class);
            throw new InvalidJSONException(errMessage, ErrorCode.INVALID_REQUEST);
        }
    }

    public static HashMap<String, Object> createHashMapFromJSON(final String jsonString) throws InvalidJSONException {
        HashMap<String, Object> map;
        try {
            map = JSON_MAPPER.readValue(jsonString, new TypeReference<HashMap<String, Object>>() {
            });
            return map;
        } catch (IOException ex) {
            String errMessage = String.format("Error in converting from JSON [%s] to class [%s]", jsonString, HashMap.class);
            throw new InvalidJSONException(errMessage, ErrorCode.INVALID_REQUEST);
        }

    }

}
