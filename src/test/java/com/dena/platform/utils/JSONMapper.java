package com.dena.platform.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

/**
 * @author Javad Alimohammadi [<bs.alimohammadi@gmail.com>]
 */
public class JSONMapper {
    private final static ObjectMapper JSON_MAPPER = new ObjectMapper().enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);

    public static <T> String createJSONFromObject(final T object) throws JsonProcessingException {
        return JSON_MAPPER.writeValueAsString(object);
    }

    public static <T> List<T> createListObjectsFromJSON(final String jsonString, final Class<T> classType) throws IOException {
        return JSON_MAPPER.readValue(jsonString, TypeFactory.defaultInstance().constructCollectionType(List.class, classType));
    }

    public static <T> T createObjectFromJSON(final String jsonString, final Class<T> classType) throws IOException {
        return JSON_MAPPER.readValue(jsonString, classType);
    }

    public static HashMap<String, Object> createMapFromJSON(final String jsonString) throws IOException {
        HashMap<String, Object> map;
        map = JSON_MAPPER.readValue(jsonString, new TypeReference<HashMap<String, Object>>() {
        });
        return map;

    }

}
