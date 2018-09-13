package com.dena.platform.core.feature.search;

import com.dena.platform.core.dto.DenaObject;
import com.dena.platform.core.feature.persistence.DenaPager;
import com.dena.platform.core.feature.search.lucene.LuceneSearch;
import com.dena.platform.core.feature.user.domain.User;
import com.dena.platform.test.ObjectModelHelper;
import com.dena.platform.utils.CommonConfig;
import org.junit.*;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * @author Nazarpour.
 */
@Ignore
public class SearchTest {

    private Search search;
    private DenaObject denaObject1 = ObjectModelHelper.getSampleDenaObject();
    private DenaObject denaObject2 = ObjectModelHelper.getSecondSampleDenaObject();
    private User user = ObjectModelHelper.getSampleUser();
    private File baseDir = new File(System.getProperty("java.io.tmpdir"));

    private DenaPager pager = DenaPager.DenaPagerBuilder.aDenaPager()
            .withPageSize(1)
            .withStartIndex(0)
            .build();

    public SearchTest() throws IOException {
    }

    @Before
    public void setUp() throws Exception {
        MockDataStore dataStore = new MockDataStore();
        search = new LuceneSearch(0, baseDir.getAbsolutePath(), dataStore);
    }

    @After
    public void tearDown() throws Exception {
        search.close();
    }

    @Test
    public void createIndex_thenSearchForIt() throws Exception {
        search.index(CommonConfig.APP_NAME, CommonConfig.TABLE_NAME, user, denaObject1);
        List<DenaObject> r1 = search.query(CommonConfig.APP_NAME, CommonConfig.TABLE_NAME, user, "name:رضا", pager);

        Assert.assertTrue(r1.size() > 0);
    }

    @Test
    public void searchForNotExistingItem() throws Exception {
        List<DenaObject> r1 = search.query(CommonConfig.APP_NAME, CommonConfig.TABLE_NAME, user, "name:اکبر", pager);

        Assert.assertTrue(r1.size() == 0);
    }

    @Test
    public void twoAddIndexAndTwoSearchWillCauseDirtyBitTrue() {
        search.index(CommonConfig.APP_NAME, CommonConfig.TABLE_NAME, user, denaObject1);
        List<DenaObject> r1 = search.query(CommonConfig.APP_NAME, CommonConfig.TABLE_NAME, user, "name:رضا", pager);

        Assert.assertTrue(r1.size() == 1);

        search.index(CommonConfig.APP_NAME, CommonConfig.TABLE_NAME, user, denaObject2);
        r1 = search.query(CommonConfig.APP_NAME, CommonConfig.TABLE_NAME, user, "name:علی", pager);

        Assert.assertTrue(r1.size() > 0);
    }

    @Test
    public void deletedIndexWillCauseZeroSizeResults() throws Exception {
        search.index(CommonConfig.APP_NAME, CommonConfig.TABLE_NAME, user, denaObject1);
        search.deleteIndex(CommonConfig.APP_NAME, CommonConfig.TABLE_NAME, user, denaObject1);
        List<DenaObject> res = search.query(CommonConfig.APP_NAME, CommonConfig.TABLE_NAME, user, "name:رضا", pager);
        Assert.assertEquals(0, res.size());

    }

    @Test
    public void updateIndexWillCauseToSeeUpdatedFieldInSearch() throws Exception {
        search.deleteIndex(CommonConfig.APP_NAME, CommonConfig.TABLE_NAME, user, denaObject1);//clear up

        search.index(CommonConfig.APP_NAME, CommonConfig.TABLE_NAME, user, denaObject1);

        List<DenaObject> res = search.query(CommonConfig.APP_NAME, CommonConfig.TABLE_NAME, user, "name:رضا", pager);
        Assert.assertEquals(1, res.size());

        List<DenaObject> res2 = search.query(CommonConfig.APP_NAME, CommonConfig.TABLE_NAME, user, "name:احمد", pager);
        Assert.assertEquals(0, res2.size());

        denaObject1.addField("name", "احمد");
        search.updateIndex(CommonConfig.APP_NAME, CommonConfig.TABLE_NAME, user, denaObject1);

        res = search.query(CommonConfig.APP_NAME, CommonConfig.TABLE_NAME, user, "name:رضا", pager);
        Assert.assertEquals(0, res.size());

        res2 = search.query(CommonConfig.APP_NAME, CommonConfig.TABLE_NAME, user, "name:احمد", pager);
        Assert.assertEquals(1, res2.size());
    }

    @Test
    public void moreComplicatedQuery() throws Exception {
        search.index(CommonConfig.APP_NAME, CommonConfig.TABLE_NAME, user, denaObject1);

        List<DenaObject> res = search.query(CommonConfig.APP_NAME, CommonConfig.TABLE_NAME, user, "age:[* TO 30]", pager);
        Assert.assertEquals(1, res.size());
    }
}