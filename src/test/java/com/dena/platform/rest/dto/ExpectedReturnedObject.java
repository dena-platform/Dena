package com.dena.platform.rest.dto;

import com.dena.platform.utils.TestUtils;
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
    private Long timestamp;

    @JsonProperty("count")
    private long count;

    @JsonProperty("objects")
    private List<DenaObject> denaObjectList = new ArrayList<>();


    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }

    public List<DenaObject> getDenaObjectList() {
        return denaObjectList;
    }

    public void setDenaObjectList(List<DenaObject> denaObjectList) {
        this.denaObjectList = denaObjectList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ExpectedReturnedObject that = (ExpectedReturnedObject) o;

        if (count != that.count) return false;
        if (!TestUtils.isTimeEqualRegardlessOfMinute(Long.valueOf(timestamp), Long.valueOf(that.getTimestamp()))) {
            return false;
        }
        return denaObjectList != null ? denaObjectList.equals(that.denaObjectList) : that.denaObjectList == null;
    }

    @Override
    public int hashCode() {
        int result = timestamp.hashCode();
        result = 31 * result + (int) (count ^ (count >>> 32));
        result = 31 * result + (denaObjectList != null ? denaObjectList.hashCode() : 0);
        return result;
    }
}
