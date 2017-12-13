package com.dena.platform.rest;

import com.dena.platform.rest.dto.ExpectedReturnedObject;
import com.dena.platform.utils.CommonConfig;
import com.dena.platform.utils.JSONMapper;
import com.mongodb.MongoClient;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
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
import java.time.LocalDateTime;

import static com.dena.platform.utils.TestUtils.truncateTimestampToMinute;
import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RestTest {

    private final static Logger log = LoggerFactory.getLogger(RestTest.class);


    private MockMvc mockMvc;

    @Resource
    private WebApplicationContext wac;

    @Resource
    private MongoClient mongoClient;


    @Before
    public void setup() throws IOException {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();

        Document document1 = new Document();
        document1.put("_id", new ObjectId());
        document1.put("name", "javad");
        document1.put("job", "developer");

        Document document2 = new Document();
        document2.put("name", "ali");
        document2.put("job", "seller");

        Document document3 = new Document();
        document3.put("name", "mahdi");
        document3.put("job", "developer");


    }


//    @Test
//    public void testCreateSingleObject() {
//
//    }

    @Test
    public void testFindSingleObject() throws Exception {

        Document document1 = new Document();
        String objectId = "5a316b1b4e5f450104c31909";
        document1.put("_id", new ObjectId(objectId));
        document1.put("name", "javad");
        document1.put("job", "developer");

        mongoClient.getDatabase(CommonConfig.dbName)
                .getCollection(CommonConfig.collectionName)
                .insertOne(document1);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get(CommonConfig.baseURL + "/" + objectId))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        String returnContent = result.getResponse().getContentAsString();
        ExpectedReturnedObject actualReturnObject = JSONMapper.createObjectFromJSON(returnContent, ExpectedReturnedObject.class);

        ExpectedReturnedObject expectedReturnObject = new ExpectedReturnedObject();
        expectedReturnObject.setCount(1L);
        expectedReturnObject.setTimestamp(String.valueOf(Instant.now().toEpochMilli()));
        expectedReturnObject.setDenaObjectsList();

        assertEquals(expectedReturnObject, actualReturnObject);


    }
}
