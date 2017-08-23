package com.dena.platform.common.persistense.HSQL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.util.Optional;

/**
 * @author Javad Alimohammadi<bs.alimohammadi@yahoo.com>
 */

public class HSQLUtils {
    private final static Logger log = LoggerFactory.getLogger(HSQLUtils.class);

    private final static String HSQL_CONNECTION_URL = "jdbc:hsqldb:mem:DENA_PLATFORM";

    public static void createTableIfNotExist() {
        makeConnection();
    }

    private static Optional<Connection> makeConnection() {
        String userName = "sa";
        String password = "";

        try {
            Class.forName("org.hsqldb.jdbc.JDBCDriver");
            Connection connection = DriverManager.getConnection(HSQL_CONNECTION_URL, userName, password);
            DatabaseMetaData dbMetaData = connection.getMetaData();
            log.info(dbMetaData.getDatabaseProductName());
            return Optional.of(connection);
        } catch (Exception ex) {
            log.error("exception in connecting to database", ex);
            return Optional.empty();
        }
    }
}
