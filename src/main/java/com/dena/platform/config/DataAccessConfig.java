package com.dena.platform.config;

import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import org.apache.commons.io.FileUtils;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.io.File;
import java.nio.charset.Charset;


/**
 * @author Javad Alimohammadi [<bs.alimohammadi@gmail.com>]
 */
@Configuration
public class DataAccessConfig {
    private final static Logger log = LoggerFactory.getLogger(DataAccessConfig.class);

    @Configuration
    @ConditionalOnClass(Mongo.class)
    static class configMongoDB {

        @Resource
        private MongoClient mongoClient;

        @PostConstruct
        public void init() {
            log.info("Initializing mongodb");
            try {
                // create a sample application
                File appFile = new File(getClass().getClassLoader().getResource("database/DENA_APPLICATIONS/DENA_APPLICATION_INFO.json").toURI());
                String appData = FileUtils.readFileToString(appFile, Charset.forName("UTF8"));
                Document appDocs = Document.parse(appData);
                mongoClient.getDatabase("DENA_APPLICATIONS")
                        .getCollection("DENA_APPLICATION_INFO")
                        .insertOne(appDocs);

                // create a sample user
                File usersFile = new File(getClass().getClassLoader().getResource("database/Dena/DENA_USER.json").toURI());
                String userData = FileUtils.readFileToString(usersFile, Charset.forName("UTF8"));
                Document userDocs = Document.parse(userData);
                mongoClient.getDatabase("Dena")
                        .getCollection("DENA_USER")
                        .insertOne(userDocs);


            } catch (Exception ex) {
                log.error("Error initializing mongodb", ex);
            }
        }
    }


}
