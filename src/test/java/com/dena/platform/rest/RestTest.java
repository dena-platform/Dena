package com.dena.platform.rest;

import de.flapdoodle.embed.mongo.MongodExecutable;
import de.flapdoodle.embed.mongo.MongodProcess;
import de.flapdoodle.embed.mongo.MongodStarter;
import de.flapdoodle.embed.mongo.config.MongodConfigBuilder;
import de.flapdoodle.embed.mongo.config.Net;
import de.flapdoodle.embed.mongo.distribution.Version;
import de.flapdoodle.embed.process.runtime.Network;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.annotation.Resource;
import java.io.IOException;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RestTest {

    private MockMvc mockMvc;

    @Resource
    private WebApplicationContext wac;

    @Before
    public void setupClass() throws IOException {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();

        MongodStarter starter = MongodStarter.getDefaultInstance();
        MongodExecutable mongodBExe = starter.prepare(new MongodConfigBuilder()
                .version(Version.Main.V3_2)
                .net(new Net("localhost", 12345, Network.localhostIsIPv6()))
                .build());

        MongodProcess mongodProcess = mongodBExe.start();

    }

//    @Test
//    public void testCreateSingleObject() {
//
//    }

    @Test
    public void testFindObjects() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/v1/denaBlogger/posts/5a206dc2cc2a9b26e483d664"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

}
