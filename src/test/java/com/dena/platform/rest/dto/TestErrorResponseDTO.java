package com.dena.platform.rest.dto;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Javad Alimohammadi [<bs.alimohammadi@gmail.com>]
 */
@JsonAutoDetect
public class TestErrorResponseDTO {
    @JsonProperty("status")
    public int status;

    @JsonProperty("error_code")
    public String errorCode;

    @JsonProperty("messages")
    public List<String> messages;


}
