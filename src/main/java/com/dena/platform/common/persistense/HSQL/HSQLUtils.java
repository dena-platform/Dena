package com.dena.platform.common.persistense.HSQL;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 * @author Javad Alimohammadi<bs.alimohammadi@yahoo.com>
 */

public class HSQLUtils {
    private final String HSQL_CONNECTION_URL = "jdbc:hsqldb:mem:DENA_PLATFORM";

    public static void craeteTableIfNotExist() {

    }

    private Connection makeConnection() {
        String userName = "sa";
        String password = "";

        try {
            Class.forName("org.hsqldb.jdbc.JDBCDriver");
            Connection connection = DriverManager.getConnection(HSQL_CONNECTION_URL, userName, password);
        } catch (Exception ex) {

        }
    }
}
