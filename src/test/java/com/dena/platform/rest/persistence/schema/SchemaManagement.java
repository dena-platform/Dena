package com.dena.platform.rest.persistence.schema;

import com.dena.platform.rest.persistence.AbstractDataStoreTest;
import com.dena.platform.restapi.dto.response.DenaResponse;
import com.dena.platform.utils.CommonConfig;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static com.dena.platform.utils.JSONMapper.createObjectFromJSON;

/**
 * @author Javad Alimohammadi [<bs.alimohammadi@gmail.com>]
 */

public class SchemaManagement extends AbstractDataStoreTest {
    @Test
    public void test_Create_Table() throws Exception {
        /////////////////////////////////////////////
        //         Send Create New Table Request
        /////////////////////////////////////////////

        DenaResponse actualReturnObject = performCreateTable("tabel1", DenaResponse.class);

        System.out.println("");

    }


    /////////////////////////////////////////////
    //            Register User
    /////////////////////////////////////////////
    protected <T> T performCreateTable(String tableName, Class<T> klass) throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post(CommonConfig.CREATE_TABLE_URL + tableName)
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        String returnContent = result.getResponse().getContentAsString();
        return createObjectFromJSON(returnContent, klass);

    }

}
