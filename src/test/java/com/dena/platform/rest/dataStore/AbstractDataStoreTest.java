package com.dena.platform.rest.dataStore;

import com.dena.platform.rest.dto.ReturnedObject;
import com.dena.platform.utils.CommonConfig;
import com.mongodb.MongoClient;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.junit.Before;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;

import static com.dena.platform.utils.JSONMapper.createObjectFromJSON;

/**
 * @author Javad Alimohammadi [<bs.alimohammadi@yahoo.com>]
 */
public class AbstractDataStoreTest {
    protected String objectId1 = "5a316b1b4e5f450104c31909";
    protected String objectId2 = "5a1bd6176f017921441d4a50";
    protected String objectId3 = "5a206dafcc2a9b26e483d663";
    protected final String randomObjectId = ObjectId.get().toHexString();


    protected MockMvc mockMvc;

    @Resource
    protected WebApplicationContext wac;

    @Resource
    protected MongoClient mongoClient;


    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();


        //////////////////////////////////////////////////////
        //       Initialize database
        //////////////////////////////////////////////////////

        mongoClient.getDatabase(CommonConfig.APP_ID).drop();

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
        document3.put(CommonConfig.COLLECTION_NAME, Arrays.asList(new ObjectId(objectId1), new ObjectId(objectId2)));


        mongoClient.getDatabase(CommonConfig.APP_ID)
                .getCollection(CommonConfig.COLLECTION_NAME)
                .insertOne(document1);

        mongoClient.getDatabase(CommonConfig.APP_ID)
                .getCollection(CommonConfig.COLLECTION_NAME)
                .insertOne(document2);

        mongoClient.getDatabase(CommonConfig.APP_ID)
                .getCollection(CommonConfig.COLLECTION_NAME)
                .insertOne(document3);

    }



    protected ReturnedObject performFindRequest(String objectId1) throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get(CommonConfig.BASE_URL + "/" + objectId1))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        String returnContent = result.getResponse().getContentAsString();
        return createObjectFromJSON(returnContent, ReturnedObject.class);
    }

    protected ReturnedObject performFindRelationRequest(String objectId1, String targetType) throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get(CommonConfig.BASE_URL + "/" + objectId1 + "/relation/" + targetType))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        String returnContent = result.getResponse().getContentAsString();
        return createObjectFromJSON(returnContent, ReturnedObject.class);
    }

    protected <T> T performDeleteRequest(List<String> objectList, int status, Class<T> klass) throws Exception {
        return performDeleteRequest(objectList, CommonConfig.BASE_URL + "/", status, klass);
    }

    protected <T> T performDeleteRequest(List<String> objectList, String urlRequest, int status, Class<T> klass) throws Exception {
        String objectIds = String.join(",", objectList);
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.delete(urlRequest + objectIds))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().is(status))
                .andReturn();

        String returnContent = result.getResponse().getContentAsString();
        return createObjectFromJSON(returnContent, klass);
    }

    protected <T> T performDeleteRelationWithObject(String parentObjectId, String relationName, int status, String targetObjectId, Class<T> klass) throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.delete(CommonConfig.BASE_URL + "/" + parentObjectId + "/relation/" + relationName + "/" + targetObjectId))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().is(status))
                .andReturn();

        String returnContent = result.getResponse().getContentAsString();
        return createObjectFromJSON(returnContent, klass);

    }

    protected ReturnedObject performDeleteRelation(String type1, String relationName) throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.delete(CommonConfig.BASE_URL + "/" + type1 + "/relation/" + relationName))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        String returnContent = result.getResponse().getContentAsString();
        return createObjectFromJSON(returnContent, ReturnedObject.class);

    }

    protected ReturnedObject performUpdateObject(String body) throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.put(CommonConfig.BASE_URL)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(body))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        String returnContent = result.getResponse().getContentAsString();
        return createObjectFromJSON(returnContent, ReturnedObject.class);

    }

    protected ReturnedObject performCreateObject(String body) throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post(CommonConfig.BASE_URL)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(body))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        String returnContent = result.getResponse().getContentAsString();
        return createObjectFromJSON(returnContent, ReturnedObject.class);

    }




}
