package com.dena.platform.core;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.HashMap;
import java.util.List;

/**
 * @author Javad Alimohammadi [<bs.alimohammadi@yahoo.com>]
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.NONE)
public class DenaObject {

    @JsonIgnore
    private String appName;

    @JsonIgnore
    private String typeName;

    @JsonProperty(value = "related_objects")
    private List<HashMap> relatedObjects;

    @JsonProperty(value = "object_values")
    private List<HashMap<String, ?>> objectsValues;


    public List<HashMap> getRelatedObjects() {
        return relatedObjects;
    }

    public void setRelatedObjects(List<HashMap> relatedObjects) {
        this.relatedObjects = relatedObjects;
    }

    public List<HashMap<String, ?>> getObjectsValues() {
        return objectsValues;
    }

    public void setObjectsValues(List<HashMap<String, ?>>) {
        this.objectsValues = objectsValues;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }
}


