package com.dena.platform.rest.persistence;

import com.dena.platform.rest.dto.TestDenaResponseDTO;
import com.dena.platform.utils.CommonConfig;
import com.mongodb.MongoClient;
import junitparams.JUnitParamsRunner;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.collections4.keyvalue.DefaultMapEntry;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.rules.SpringClassRule;
import org.springframework.test.context.junit4.rules.SpringMethodRule;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.util.UriComponentsBuilder;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.dena.platform.utils.JSONMapper.createObjectFromJSON;

/**
 * @author Javad Alimohammadi [<bs.alimohammadi@yahoo.com>]
 */

@AutoConfigureMockMvc
@RunWith(JUnitParamsRunner.class)
@SpringBootTest
public class AbstractDataStoreTest {
    protected final String objectId1 = "5a316b1b4e5f450104c31909";
    protected final String objectId2 = "5a1bd6176f017921441d4a50";
    protected final String objectId3 = "5a206dafcc2a9b26e483d663";
    protected final String objectId4 = "5aaa11d2ecb1ef188094eed6";
    protected final String objectId5 = "5aaa445ebb19df061c79f8f0";
    protected final String objectId6 = "5aaa445ebb19df061c79f8f1";
    protected final String objectId7 = "5aaa4460bb19df061c79f8f2";
    protected final String objectId8 = "5aaa4460bb19df061c79f8f3";
    protected final String objectId9 = "5aaa8234bb19df25acce463d";
    protected final String objectId10 = "5aaa8234bb19df25acce463e";
    protected final String objectId11 = "5ab557484611681f7c07a6dd";

    protected final String randomObjectId = ObjectId.get().toHexString();


    // for parametrize test runner
    @ClassRule
    public static final SpringClassRule SPRING_CLASS_RULE = new SpringClassRule();

    @Rule
    public final SpringMethodRule springMethodRule = new SpringMethodRule();

    @Resource
    protected MockMvc mockMvc;

    @Resource
    protected MongoClient mongoClient;

    @Before
    public void setup() {


        //////////////////////////////////////////////////////
        //       Initialize database
        //////////////////////////////////////////////////////

        mongoClient.getDatabase(CommonConfig.APP_ID).drop();
        mongoClient.getDatabase(CommonConfig.DENA_APPLICATION).drop();

        Document document1 = createDocument(objectId1, MapUtils.putAll(new HashMap<>(), new Map.Entry[]{
                        new DefaultMapEntry<>("name", "javad"),
                        new DefaultMapEntry<>("job", "developer")
                }
        ));

        Document document2 = createDocument(objectId2, MapUtils.putAll(new HashMap<>(), new Map.Entry[]{
                        new DefaultMapEntry<>("name", "javad"),
                        new DefaultMapEntry<>("job", "developer")
                }
        ));


        Document document3 = createDocument(objectId3, MapUtils.putAll(new HashMap<>(), new Map.Entry[]{
                        new DefaultMapEntry<>("name", "javad"),
                        new DefaultMapEntry<>("job", "developer")
                }
        ));

        Document relatedDocument = new Document("relation_type", "ONE-TO-ONE")
                .append("target_name", CommonConfig.COLLECTION_NAME)
                .append("ids", Arrays.asList(new ObjectId(objectId1), new ObjectId(objectId2)));

        document3.put(CommonConfig.RELATION_NAME, relatedDocument);


        Document document4 = createDocument(objectId4, MapUtils.putAll(new HashMap<>(), new Map.Entry[]{
                        new DefaultMapEntry<>("name", "javad"),
                        new DefaultMapEntry<>("job", "developer")
                }
        ));

        Document document5 = createDocument(objectId5, MapUtils.putAll(new HashMap<>(), new Map.Entry[]{
                        new DefaultMapEntry<>("name", "javad"),
                        new DefaultMapEntry<>("job", "developer")
                }
        ));

        Document document6 = createDocument(objectId6, MapUtils.putAll(new HashMap<>(), new Map.Entry[]{
                        new DefaultMapEntry<>("name", "javad"),
                        new DefaultMapEntry<>("job", "developer")
                }
        ));

        Document document7 = createDocument(objectId7, MapUtils.putAll(new HashMap<>(), new Map.Entry[]{
                        new DefaultMapEntry<>("name", "javad"),
                        new DefaultMapEntry<>("job", "developer")
                }
        ));

        Document document8 = createDocument(objectId8, MapUtils.putAll(new HashMap<>(), new Map.Entry[]{
                        new DefaultMapEntry<>("name", "javad"),
                        new DefaultMapEntry<>("job", "developer")
                }
        ));

        Document document9 = createDocument(objectId9, MapUtils.putAll(new HashMap<>(), new Map.Entry[]{
                        new DefaultMapEntry<>("name", "javad"),
                        new DefaultMapEntry<>("job", "developer")
                }
        ));

        Document document10 = createDocument(objectId10, MapUtils.putAll(new HashMap<>(), new Map.Entry[]{
                        new DefaultMapEntry<>("name", "javad"),
                        new DefaultMapEntry<>("job", "developer")
                }
        ));
        Document document11 = createDocument(objectId11, MapUtils.putAll(new HashMap<>(), new Map.Entry[]{
                        new DefaultMapEntry<>("name", "javad"),
                        new DefaultMapEntry<>("job", "developer")
                }
        ));


        mongoClient.getDatabase(CommonConfig.APP_ID)
                .getCollection(CommonConfig.COLLECTION_NAME)
                .insertMany(Arrays.asList(document1, document2, document3, document4,
                        document5, document6, document7, document8,
                        document9, document10, document11
                ));

    }

    /////////////////////////////////////////////
    //            DATA ACCESS REQUEST
    /////////////////////////////////////////////

    protected TestDenaResponseDTO performFindRequestByObjectId(String objectId1) throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get(CommonConfig.BASE_URL + "/" + objectId1))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        String returnContent = result.getResponse().getContentAsString();
        return createObjectFromJSON(returnContent, TestDenaResponseDTO.class);
    }

    protected TestDenaResponseDTO performFindRequestInTable(String tableName, int startIndex, int pageSize) throws Exception {
        String URITemplate = CommonConfig.ROOT_URL + CommonConfig.APP_ID + "/" + tableName;
        UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder
                .fromUriString(URITemplate)
                .queryParam("startIndex", startIndex)
                .queryParam("pageSize", pageSize);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get(uriComponentsBuilder.toUriString()))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        String returnContent = result.getResponse().getContentAsString();
        return createObjectFromJSON(returnContent, TestDenaResponseDTO.class);
    }


    protected TestDenaResponseDTO performFindRelationRequest(String objectId, String relationName, int startIndex, int pageSize) throws Exception {
        String URITemplate = CommonConfig.BASE_URL + "/" + objectId + "/relation/" + relationName;
        UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder
                .fromUriString(URITemplate)
                .queryParam("startIndex", startIndex)
                .queryParam("pageSize", pageSize);


        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get(uriComponentsBuilder.toUriString()))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        String returnContent = result.getResponse().getContentAsString();
        return createObjectFromJSON(returnContent, TestDenaResponseDTO.class);
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

    protected TestDenaResponseDTO performDeleteRelation(String type1, String relationName) throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.delete(CommonConfig.BASE_URL + "/" + type1 + "/relation/" + relationName))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        String returnContent = result.getResponse().getContentAsString();
        return createObjectFromJSON(returnContent, TestDenaResponseDTO.class);

    }

    protected <T> T performUpdateObject(String body, Class<T> klass) throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.put(CommonConfig.BASE_URL)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(body))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        String returnContent = result.getResponse().getContentAsString();
        return createObjectFromJSON(returnContent, klass);

    }

    protected <T> T performUpdateObject(String body, int status, Class<T> klass) throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.put(CommonConfig.BASE_URL)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(body))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().is(status))
                .andReturn();

        String returnContent = result.getResponse().getContentAsString();
        return createObjectFromJSON(returnContent, klass);

    }

    protected <T> T performCreateObject(String body, Class<T> klass) throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post(CommonConfig.BASE_URL)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(body))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        String returnContent = result.getResponse().getContentAsString();
        return createObjectFromJSON(returnContent, klass);

    }

    protected <T> T performCreateObject(String body, int status, Class<T> klass) throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post(CommonConfig.BASE_URL)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(body))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().is(status))
                .andReturn();

        String returnContent = result.getResponse().getContentAsString();
        return createObjectFromJSON(returnContent, klass);

    }

    protected <T> T performCreateObjectWithToken(String body, int status, Class<T> klass, String token) throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post(CommonConfig.BASE_URL)
                .header("token", token)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(body))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().is(status))
                .andReturn();

        String returnContent = result.getResponse().getContentAsString();
        return createObjectFromJSON(returnContent, klass);

    }


    /////////////////////////////////////////////
    //            LOGIN
    /////////////////////////////////////////////
    protected <T> T performLoginUser(String body, HttpStatus httpStatus, Class<T> klass) throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post(CommonConfig.LOGIN_URL)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(body))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().is(httpStatus.value()))
                .andReturn();

        String returnContent = result.getResponse().getContentAsString();
        return createObjectFromJSON(returnContent, klass);

    }

    private Document createDocument(String objectId, Map<String, ?> parameters) {
        Document document = new Document();
        document.put("_id", new ObjectId(objectId));
        document.putAll(parameters);
        document.put("object_uri", "/" + CommonConfig.COLLECTION_NAME + "/" + objectId);

        return document;

    }


}
