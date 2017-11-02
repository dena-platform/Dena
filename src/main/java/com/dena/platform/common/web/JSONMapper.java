package com.dena.platform.common.web;

import com.dena.platform.common.exception.RestInputInvalidException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;

import java.io.IOException;
import java.util.List;

/**
 * @author Javad Alimohammadi [<bs.alimohammadi@yahoo.com>]
 */
public class JSONMapper {
    private final static ObjectMapper JSON_MAPPER = new ObjectMapper();

    public static <T> String createJSONFromObject(final T object) {
        try {
            return JSON_MAPPER.writeValueAsString(object);
        } catch (JsonProcessingException ex) {
            String errMessage = String.format("Error in converting from Class [%s] to JSON", object.getClass().getSimpleName());
            throw new RestInputInvalidException(errMessage);
        }
    }

    public static <T> List<T> createObjectsFromJSON(final String jsonString, final Class<T> classType) throws IOException {
        return JSON_MAPPER.readValue(jsonString, TypeFactory.defaultInstance().constructCollectionType(List.class, classType));
    }

    public static <T> T createObjectFromJSON(final String jsonString, final Class<T> classType) {
        try {
            return JSON_MAPPER.readValue(jsonString, classType);
        } catch (IOException ex) {
            String errMessage = String.format("Error in converting from JSON [%s] to Class [%s]", jsonString, classType);
            throw new RestInputInvalidException(errMessage);
        }
    }

}
