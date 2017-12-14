package com.dena.platform.rest;

import com.dena.platform.rest.dto.DenaObject;
import com.dena.platform.rest.dto.ExpectedReturnedObject;
import com.dena.platform.utils.CommonConfig;
import com.dena.platform.utils.JSONMapper;
import com.mongodb.MongoClient;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.skyscreamer.jsonassert.JSONAssert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.annotation.Resource;
import java.io.IOException;
import java.time.Instant;
import java.util.Arrays;
import java.util.Collections;

import static com.dena.platform.utils.JSONMapper.createJSONFromObject;
import static com.dena.platform.utils.TestUtils.isTimeEqualRegardlessOfMinute;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RestTest {

    private final static Logger log = LoggerFactory.getLogger(RestTest.class);

    private String objectId1 = "5a316b1b4e5f450104c31909";
    private String objectId2 = "5a1bd6176f017921441d4a50";
    private String objectId3 = "5a206dafcc2a9b26e483d663";


    private MockMvc mockMvc;

    @Resource
    private WebApplicationContext wac;

    @Resource
    private MongoClient mongoClient;


    @Before
    public void setup() throws IOException {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();

        // clean database
        mongoClient.getDatabase(CommonConfig.dbName).drop();

        // init database
        Document document1 = new Document();

        document1.put("_id", new ObjectId(objectId1));
        document1.put("name", "javad");
        document1.put("job", "developer");

        Document document2 = new Document();

        document2.put("_id", new ObjectId(objectId2));
        document2.put("name", "javad");
        document2.put("job", "developer");


        Document document3 = new Document();
        document3.put("_id", new ObjectId(objectId3));
        document3.put("name", "javad");
        document3.put("job", "developer");
        document3.put(CommonConfig.collectionName, Arrays.asList(objectId1, objectId2));


        mongoClient.getDatabase(CommonConfig.dbName)
                .getCollection(CommonConfig.collectionName)
                .insertOne(document1);

        mongoClient.getDatabase(CommonConfig.dbName)
                .getCollection(CommonConfig.collectionName)
                .insertOne(document2);

        mongoClient.getDatabase(CommonConfig.dbName)
                .getCollection(CommonConfig.collectionName)
                .insertOne(document3);

    }


//    @Test
//    public void testCreateSingleObject() {
//
//    }

    @Test
    public void testFindObjectWhenObjectExist() throws Exception {

        //////////////////////////////////////////////////////
        //       FIND OBJECT WITH NO RELATION
        //////////////////////////////////////////////////////
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get(CommonConfig.baseURL + "/" + objectId1))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        String returnContent = result.getResponse().getContentAsString();
        ExpectedReturnedObject actualReturnObject = JSONMapper.createObjectFromJSON(returnContent, ExpectedReturnedObject.class);

        ExpectedReturnedObject expectedReturnObject = new ExpectedReturnedObject();
        expectedReturnObject.setCount(1L);
        expectedReturnObject.setTimestamp(actualReturnObject.getTimestamp());

        DenaObject denaObject = new DenaObject();
        denaObject.setObjectId(objectId1);
        denaObject.setObjectURI("/" + CommonConfig.collectionName + "/" + objectId1);
        denaObject.addProperty("name", "javad");
        denaObject.addProperty("job", "developer");
        expectedReturnObject.setDenaObjectList(Collections.singletonList(denaObject));

        // check timestamp field of returned object
        assertTrue(isTimeEqualRegardlessOfMinute(actualReturnObject.getTimestamp(), Instant.now().toEpochMilli()));
        JSONAssert.assertEquals(createJSONFromObject(expectedReturnObject), createJSONFromObject(actualReturnObject), true);


        //////////////////////////////////////////////////////
        //       FIND OBJECT WITH RELATION
        //////////////////////////////////////////////////////
        result = mockMvc.perform(MockMvcRequestBuilders.get(CommonConfig.baseURL + "/" + objectId3))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        returnContent = result.getResponse().getContentAsString();
        actualReturnObject = JSONMapper.createObjectFromJSON(returnContent, ExpectedReturnedObject.class);

        expectedReturnObject = new ExpectedReturnedObject();
        expectedReturnObject.setCount(1L);
        expectedReturnObject.setTimestamp(actualReturnObject.getTimestamp());

        denaObject = new DenaObject();
        denaObject.setObjectId(objectId1);
        denaObject.setObjectURI("/" + CommonConfig.collectionName + "/" + objectId3);
        denaObject.addProperty("name", "javad");
        denaObject.addProperty("job", "developer");
        expectedReturnObject.setDenaObjectList(Collections.singletonList(denaObject));

        // check timestamp field of returned object
        assertTrue(isTimeEqualRegardlessOfMinute(actualReturnObject.getTimestamp(), Instant.now().toEpochMilli()));
        JSONAssert.assertEquals(createJSONFromObject(expectedReturnObject), createJSONFromObject(actualReturnObject), true);


    }

    @Test
    public void testFindObjectWhenObjectNotExist() throws Exception {
        String objectId = ObjectId.get().toHexString();

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get(CommonConfig.baseURL + "/" + objectId))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        String returnContent = result.getResponse().getContentAsString();
        ExpectedReturnedObject actualReturnObject = JSONMapper.createObjectFromJSON(returnContent, ExpectedReturnedObject.class);

        ExpectedReturnedObject expectedReturnObject = new ExpectedReturnedObject();
        expectedReturnObject.setCount(0L);
        expectedReturnObject.setTimestamp(actualReturnObject.getTimestamp());

        // check timestamp field of returned object
        assertTrue(isTimeEqualRegardlessOfMinute(actualReturnObject.getTimestamp(), Instant.now().toEpochMilli()));
        JSONAssert.assertEquals(createJSONFromObject(expectedReturnObject), createJSONFromObject(actualReturnObject), true);

    }
}
