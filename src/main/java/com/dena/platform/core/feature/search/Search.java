package com.dena.platform.core.feature.search;

import com.dena.platform.core.dto.DenaObject;
import com.dena.platform.core.feature.persistence.DenaPager;
import com.dena.platform.core.feature.user.domain.User;

import java.util.List;

/**
 * @author Nazarpour.
 */
public interface Search {
    void index(String appId, User user, DenaObject object);

    void updateIndex(String appId, User user, DenaObject objects);

    void deleteIndex(String appId, User user, DenaObject... objects);

    List<DenaObject> query(String appId, User user, String query, DenaPager pager);

    void close();
}
