package com.dena.platform.core.feature.search;

import com.dena.platform.core.dto.DenaObject;
import com.dena.platform.core.feature.persistence.DenaPager;

import java.util.List;

/**
 * @author Nazarpour.
 */
public interface Search {
    void index(String appId, DenaObject object);

    List<DenaObject> query(String appId, String query, String field, DenaPager pager);

    void close();
}
