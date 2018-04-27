package com.dena.platform.core.feature.search;

import com.dena.platform.core.dto.DenaObject;
import com.dena.platform.core.feature.persistence.DenaPager;
import com.dena.platform.core.feature.search.lucene.LuceneSearch;
import com.dena.platform.rest.dto.ObjectModelHelper;
import com.dena.platform.utils.CommonConfig;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

/**
 * @author Nazarpour.
 */
public class SearchTest {

    private Search search;
    private DenaObject denaObject = ObjectModelHelper.getSampleDenaObject();

    @Before
    public void setUp() throws Exception {
        search = new LuceneSearch(false);
    }

    @After
    public void tearDown() throws Exception {
        search.close();
    }

    @Test
    public void createIndex() throws Exception {
        search.index(CommonConfig.APP_ID, denaObject);

        DenaPager pager = DenaPager.DenaPagerBuilder.aDenaPager()
                .withPageSize(10)
                .withStartIndex(0)
                .build();
        List<DenaObject> results = search.query(CommonConfig.APP_ID, "رضا", "name", pager);


    }
}