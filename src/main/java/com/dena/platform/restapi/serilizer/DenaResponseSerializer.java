package com.dena.platform.restapi.serilizer;

import com.dena.platform.common.utils.java8Utils.LambdaWrapper;
import com.dena.platform.restapi.dto.response.DenaObjectResponse;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.util.Objects;

/**
 * @author Javad Alimohammadi [<bs.alimohammadi@gmail.com>]
 */
public class DenaResponseSerializer extends JsonSerializer<DenaObjectResponse> {
    @Override
    public void serialize(DenaObjectResponse denaObjectResponse, JsonGenerator gen, SerializerProvider serializers) throws IOException, JsonProcessingException {
        gen.writeStartObject();

        // todo:
        if (!Objects.isNull(denaObjectResponse.getObjectId())) {
            gen.writeStringField("object_id", denaObjectResponse.getObjectId());
            gen.writeStringField("object_uri", denaObjectResponse.getObjectURI());
            gen.writeObjectField("update_time", denaObjectResponse.getUpdateTime());
            gen.writeObjectField("create_time", denaObjectResponse.getCreateTime());
        }
        denaObjectResponse.getAllFields().forEach(LambdaWrapper.uncheckedBiConsumer(gen::writeObjectField));


        gen.writeEndObject();
    }
}
