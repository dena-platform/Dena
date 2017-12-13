package com.dena.platform.rest.dto;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Javad Alimohammadi [<bs.alimohammadi@yahoo.com>]
 */

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.NONE, getterVisibility = JsonAutoDetect.Visibility.NONE)
public class ExpectedReturnedObject {

    @JsonProperty("timestamp")
    private String timestamp;

    @JsonProperty("count")
    private long count;

    @JsonProperty("objects")
    private List<DenaObjects> denaObjectsList = new ArrayList<>();


    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }

    public List<DenaObjects> getDenaObjectsList() {
        return denaObjectsList;
    }

    public void setDenaObjectsList(List<DenaObjects> denaObjectsList) {
        this.denaObjectsList = denaObjectsList;
    }
}
