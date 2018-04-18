package com.dena.platform.restapi.serilizer;

import com.dena.platform.common.utils.java8Utils.LambdaWrapper;
import com.dena.platform.restapi.dto.response.DenaObjectResponse;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

/**
 * @author Javad Alimohammadi [<bs.alimohammadi@gmail.com>]
 */
public class DenaResponseSerializer extends JsonSerializer<DenaObjectResponse> {
    @Override
    public void serialize(DenaObjectResponse value, JsonGenerator gen, SerializerProvider serializers) throws IOException, JsonProcessingException {
        gen.writeStartObject();

        gen.writeStringField("object_id", value.getObjectId());
        gen.writeStringField("object_uri", value.getObjectURI());
        gen.writeObjectField("update_time", value.getUpdateTime());
        gen.writeObjectField("create_time", value.getCreateTime());
        value.getAllFields().forEach(LambdaWrapper.uncheckedBiConsumer(gen::writeObjectField));


        gen.writeEndObject();
    }
}
