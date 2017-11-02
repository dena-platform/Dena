package com.dena.platform.common.persistense.HSQL;

import com.dena.platform.common.utils.java8Utils.LambdaWrapper;
import com.dena.platform.common.web.JSONMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;

/**
 * @author Javad Alimohammadi [<bs.alimohammadi@yahoo.com>]
 */

public class HSQLUtils {
    private final static Logger log = LoggerFactory.getLogger(HSQLUtils.class);

    private final static String HSQL_CONNECTION_URL = "jdbc:hsqldb:mem:DENA_PLATFORM";

    public static void createTableIfNotExist(final String tableName) throws SQLException, ClassNotFoundException {
        Optional<Connection> connection = makeConnection();
        Consumer<Connection> connectionConsumer = LambdaWrapper.uncheckedConsumer(conn -> {
                    if (!isTableExist(conn, tableName)) {
                        createTable(conn, tableName);
                    }
                }
        );
        connection
                .ifPresent(connectionConsumer
                        .andThen(LambdaWrapper.uncheckedConsumer(HSQLUtils::closeConnection)));

    }

    public static void storeObjectInTable(String tableName, Integer id, Map<String, Object> properties) throws SQLException, ClassNotFoundException {
        Optional<Connection> connection = makeConnection();
        Consumer<Connection> connectionConsumer = LambdaWrapper.uncheckedConsumer(conn -> {
                    insertRecord(conn, tableName, id, JSONMapper.createJSONFromObject(properties));
                }
        );

        connection
                .ifPresent(connectionConsumer
                        .andThen(LambdaWrapper.uncheckedConsumer(HSQLUtils::closeConnection)));


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
            log.debug("table [{}] exist", tableName);
            return true;
        } else {
            log.debug("table [{}] not exist", tableName);
            return false;
        }
    }

    private static void createTable(Connection connection, String tableName) throws SQLException {
        Statement statement = connection.createStatement();

        String createTableStatement = String.format("CREATE TABLE \"%s\" (ID INTEGER NOT NULL PRIMARY KEY , DATA varchar(200))", tableName);
        statement.executeUpdate(createTableStatement);
        log.debug("Table [{}] created successfully.", tableName);

    }

    private static void insertRecord(Connection connection, String tableName, Integer id, String value) throws SQLException {
        Statement statement = connection.createStatement();
        String insertStatement = String.format("INSERT INTO \"%s\" (ID , DATA) VALUES (%d , '%s')", tableName, id, value);
        statement.executeUpdate(insertStatement);
        log.debug("statement [{}] executed successfully.", insertStatement);

    }

}
