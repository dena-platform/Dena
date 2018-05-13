package com.dena.platform.core.feature.search;

import com.dena.platform.core.dto.DenaObject;
import com.dena.platform.core.feature.persistence.DenaPager;
import com.dena.platform.core.feature.user.domain.User;

import java.util.List;

/**
 * @author Nazarpour.
 */
public interface Search {
    void index(String appId, String collectionName, User user, DenaObject... object);

    void updateIndex(String appId, String collectionName, User user, DenaObject... objects);

    void deleteIndex(String appId, String collectionName, User user, DenaObject... objects);

    void deleteIndexByIds(String appId, User user, String... ids);

    List<DenaObject> query(String appId, String collectionName, User user, String query, DenaPager pager);

    void close();
}
