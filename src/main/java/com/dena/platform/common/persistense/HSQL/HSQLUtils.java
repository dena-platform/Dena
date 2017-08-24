package com.dena.platform.common.persistense.HSQL;

import com.dena.platform.common.utils.java8Utils.LambdaWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.Optional;

/**
 * @author Javad Alimohammadi<bs.alimohammadi@yahoo.com>
 */

public class HSQLUtils {
    private final static Logger log = LoggerFactory.getLogger(HSQLUtils.class);

    private final static String HSQL_CONNECTION_URL = "jdbc:hsqldb:mem:DENA_PLATFORM";

    public static void createTableIfNotExist(String tableName) throws SQLException, ClassNotFoundException {
        Optional<Connection> connection = makeConnection();

        // todo: can use optional in better manner
        if (connection.isPresent()) {
            if (!isTableExist(connection.get(), tableName)) {
                createTable(connection.get(), tableName);
            }
            closeConnection(connection.get());
        }
    }

    private static Optional<Connection> makeConnection() throws SQLException, ClassNotFoundException {
        String userName = "sa";
        String password = "";

        try {
            Class.forName("org.hsqldb.jdbc.JDBCDriver");
            Connection connection = DriverManager.getConnection(HSQL_CONNECTION_URL, userName, password);
            return Optional.of(connection);
        } catch (final Exception ex) {
            log.error("Exception in connecting to database", ex);
            throw ex;
        }
    }

    private static void closeConnection(Connection connection) throws SQLException {
        try {
            connection.close();
        } catch (SQLException e) {
            log.error("Error in closing connection", e);
            throw e;
        }
    }

    private static boolean isTableExist(Connection connection, String tableName) throws SQLException {
        DatabaseMetaData dbm = connection.getMetaData();
        // check if "employee" table is there
        ResultSet tables = dbm.getTables(null, null, tableName, null);
        if (tables.next()) {
            log.info("table [{}] exist", tableName);
            return true;
        } else {
            log.info("table [{}] not exist", tableName);
            return false;
        }
    }

    private static void createTable(Connection connection, String tableName) throws SQLException {
        Statement statement = connection.createStatement();
        //todo: change id column
        statement.executeUpdate("CREATE TABLE " + tableName + " (id INTEGER NOT NULL PRIMARY KEY)");
        log.debug("Table [{}] created successfully.", tableName);

    }

}
