package com.dena.platform.core.feature.datastore.hsql;

import com.dena.platform.common.web.JSONMapper;
import com.dena.platform.core.DenaObject;
import org.apache.commons.lang3.NotImplementedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.List;
import java.util.Map;

/**
 * @author Javad Alimohammadi [<bs.alimohammadi@yahoo.com>]
 */

public class HSQLUtils {
    private final static Logger log = LoggerFactory.getLogger(HSQLUtils.class);

    private final static String HSQL_CONNECTION_URL = "jdbc:hsqldb:mem:DENA_PLATFORM";

    public static void createTableIfNotExist(final String tableName) throws SQLException, ClassNotFoundException {
        if (!isTableExist(tableName)) {
            createTable(tableName);
        }

    }

    public static void storeObjectInTable(String tableName, Integer id, Map<String, Object> properties) throws SQLException, ClassNotFoundException {
        insertRecord(tableName, id, JSONMapper.createJSONFromObject(properties));

    }

    public String findObjectInTable(Integer id) {
        throw new NotImplementedException("This method not implemented yet");
    }

    private static Connection makeConnection() throws SQLException, ClassNotFoundException {
        String userName = "sa";
        String password = "";

        try {
            Class.forName("org.hsqldb.jdbc.JDBCDriver");
            Connection connection = DriverManager.getConnection(HSQL_CONNECTION_URL, userName, password);
            return connection;
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

    private static boolean isTableExist(String tableName) throws SQLException, ClassNotFoundException {
        Connection connection = makeConnection();
        DatabaseMetaData dbm = connection.getMetaData();

        // check if 'tableName' exist in database
        ResultSet tables = dbm.getTables(null, null, tableName, null);
        boolean isTableExist = tables.next();

        if (isTableExist) {
            log.debug("table [{}] exist", tableName);
        } else {
            log.debug("table [{}] not exist", tableName);
        }

        closeConnection(connection);
        return isTableExist;

    }

    private static void createTable(String tableName) throws SQLException, ClassNotFoundException {
        Connection connection = makeConnection();
        Statement statement = connection.createStatement();

        String createTableStatement = String.format("CREATE TABLE \"%s\" (ID INTEGER NOT NULL PRIMARY KEY , DATA varchar(200))", tableName);
        statement.executeUpdate(createTableStatement);
        closeConnection(connection);
        log.debug("Table [{}] created successfully.", tableName);

    }

    private static void insertRecord(String tableName, Integer id, String value) throws SQLException, ClassNotFoundException {
        Connection connection = makeConnection();
        Statement statement = connection.createStatement();
        String insertStatement = String.format("INSERT INTO \"%s\" (ID , DATA) VALUES (%d , '%s')", tableName, id, value);
        statement.executeUpdate(insertStatement);
        closeConnection(connection);
        log.debug("statement [{}] executed successfully.", insertStatement);

    }

    private static List<DenaObject> findRecord(String tableName, Integer id) throws SQLException, ClassNotFoundException {
//        List<EntityDTO> result = new ArrayList<>();
//        Connection connection = makeConnection();
//        String findStatement = String.format("SELECT DATA FROM  \"%s\" WHERE ID=?", tableName);
//        PreparedStatement statement = connection.prepareStatement(findStatement);
//        statement.setInt(1, id);
//
//        ResultSet resultSet = statement.executeQuery(findStatement);
//
//        while (resultSet.next()) {
//
//        }
//
//        closeConnection(connection);
//        log.debug("statement [{}] executed successfully.", findStatement);
        throw new NotImplementedException("This method not implemented yet");

    }

}
