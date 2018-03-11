package com.dena.platform.restapi.Serilizer;

import com.dena.platform.restapi.dto.response.DenaObjectResponse;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

/**
 * @author Javad Alimohammadi [<bs.alimohammadi@yahoo.com>]
 */
public class DenaResponseSerializer extends JsonSerializer<DenaObjectResponse> {
    @Override
    public void serialize(DenaObjectResponse value, JsonGenerator gen, SerializerProvider serializers) throws IOException, JsonProcessingException {
        gen.writeStartObject();

        gen.writeStringField("object_id", value.getObjectId());
        gen.writeStringField("object_uri", value.getObjectURI());
        gen.writeObjectField("update_time", value.getUpdateTime());
        gen.writeObjectField("creation_time", value.getCreateTime());
        gen.writeEndObject();
    }
}
