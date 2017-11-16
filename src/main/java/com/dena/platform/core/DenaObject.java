package com.dena.platform.core;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.HashMap;
import java.util.List;

/**
 * @author Javad Alimohammadi [<bs.alimohammadi@yahoo.com>]
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.NONE)
public class DenaObject {

    @JsonProperty(value = "related_objects", required = true)
    private List<HashMap> relatedObjects;

    @JsonProperty(value = "object_values", required = true)
    private List<HashMap> objectsValue;


    public List<HashMap> getRelatedObjects() {
        return relatedObjects;
    }

    public void setRelatedObjects(List<HashMap> relatedObjects) {
        this.relatedObjects = relatedObjects;
    }

    public List<HashMap> getObjectsValue() {
        return objectsValue;
    }

    public void setObjectsValue(List<HashMap> objectsValue) {
        this.objectsValue = objectsValue;
    }

    @Override
    public String toString() {
        return "EntityDTO{" +
                "relatedObjects=" + relatedObjects +
                ", objectsValue=" + objectsValue +
                '}';
    }
}
