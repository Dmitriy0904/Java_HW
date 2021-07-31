package db;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionSetup implements AutoCloseable{
    private final String url = "jdbc:mysql://localhost:3306/module_3_jdbc";
    private static final Logger info = LoggerFactory.getLogger("info");
    private static final Logger error = LoggerFactory.getLogger("error");
    private Connection connection;

    public ConnectionSetup(String username, String password) {
        this.connection = connectToDb(username, password);
    }

    private Connection connectToDb(String username, String password){
        try {
            info.info("Trying to get connection to db:{}", url);
            connection = DriverManager.getConnection(url, username, password);
            info.info("Receiving connection to db:{} completed successfully", url);
            return connection;

        } catch (SQLException exception) {
            error.error("SQLException in getting connection to db:{}", url);
            throw new RuntimeException(exception);
        }
    }

    public Connection getConnection(){
        return connection;
    }

    @Override
    public void close(){
        try {
            info.info("Closing connection to db:{}", url);
            connection.close();
            info.info("Connection to db:{} was closed successfully", url);

        } catch (SQLException exception) {
            error.error("SQLException in closing connection to db:{}", url);
            throw new RuntimeException(exception);
        }
    }
}