package com.dena.platform.core.feature.persistence;

import com.dena.platform.core.dto.DenaObject;
import com.dena.platform.core.feature.persistence.mongodb.MongoDBDataStoreImpl;
import org.assertj.core.api.Assertions;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author Javad Alimohammadi<bs.alimohammadi@yahoo.com>
 */

@SpringBootTest
@RunWith(SpringRunner.class)
public class MongoDBDataStoreImplTest {

    @Resource
    private MongoDBDataStoreImpl mongoDBDataStore;


    @Before
    public void setUp() throws Exception {

    }

    @Test
    public void test_store() {
        // given
        DenaObject denaObject = new DenaObject();
        denaObject.addField("name", "alex");
        denaObject.addField("family", "smith");

        // when
        List<DenaObject> storedObject = mongoDBDataStore.store("app1", "table1", denaObject);
        final String storedObjectId = storedObject.get(0).getObjectId();
        DenaObject foundObject = mongoDBDataStore.find("app1", "table1", storedObjectId).get(0);

        // then
        Assertions.assertThat(foundObject.getObjectId()).isNotBlank();
        Assert.assertNotNull("Creation time should not be null", foundObject.getCreateTime());
        Assert.assertNull("Update time should be null", foundObject.getUpdateTime());
        Assert.assertEquals("alex", foundObject.getField("name", String.class));
        Assert.assertEquals("smith", foundObject.getField("family", String.class));

    }

    @Test
    public void test_store_with_bad_input() {
        // given
        DenaObject denaObject = new DenaObject();
        denaObject.addField("name", "alex");
        denaObject.addField("family", "smith");

        // when
        List<DenaObject> storedObject = mongoDBDataStore.store("app1", "table1", denaObject);
        final String storedObjectId = storedObject.get(0).getObjectId();
        DenaObject foundObject = mongoDBDataStore.find("app1", "table1", storedObjectId).get(0);

        // then
        Assertions.assertThat(foundObject.getObjectId()).isNotBlank();
        Assert.assertNotNull("Creation time should not be null", foundObject.getCreateTime());
        Assert.assertNull("Update time should be null", foundObject.getUpdateTime());
        Assert.assertEquals("alex", foundObject.getField("name", String.class));
        Assert.assertEquals("smith", foundObject.getField("family", String.class));

    }


}
