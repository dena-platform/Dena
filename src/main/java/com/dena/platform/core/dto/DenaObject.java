package com.dena.platform.core.dto;

import com.fasterxml.jackson.annotation.*;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Javad Alimohammadi [<bs.alimohammadi@yahoo.com>]
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.NONE, getterVisibility = JsonAutoDetect.Visibility.NONE)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class DenaObject {
    private Map<String, Object> fields = new HashMap<>();


    @JsonProperty(value = "related_objects")
    private List<RelatedObject> relatedObjects;

    private String id;

    private String appName;

    private String typeName;

    private String resourceURI;


    public List<RelatedObject> getRelatedObjects() {
        return relatedObjects;
    }

    public void setRelatedObjects(List<RelatedObject> relatedObjects) {
        this.relatedObjects = relatedObjects;
    }


    @JsonAnySetter
    public void addProperty(String name, Object value) {
        if (StringUtils.isNoneBlank(name) && value != null) {
            fields.put(name, value);
        }
    }

    @JsonAnyGetter
    public Map<String, Object> getAllFields() {
        return fields;
    }


    public Map<String, Object> getFields() {
        return fields;
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

    public String getResourceURI() {
        return resourceURI;
    }

    public void setResourceURI(String resourceURI) {
        this.resourceURI = resourceURI;
    }

    public String getTypeNamePlural() {
        if (!typeName.endsWith("s")) {
            return typeName + "s";
        } else {
            return typeName;
        }
    }

    public String getURIForResource() {
        return "/" + typeName + "/" + id;
    }
}


