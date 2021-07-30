package db;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionSetup {
    private final String url = "jdbc:mysql://localhost:3306/module_3_jdbc";
    private final String username;
    private final String password;
    private static final Logger info = LoggerFactory.getLogger("info");
    private static final Logger error = LoggerFactory.getLogger("error");

    public ConnectionSetup(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public Connection getConnection(){
        try {
            info.info("Trying to get connection to db:{}", url);
            Connection connection = DriverManager.getConnection(url, username, password);
            info.info("Receiving connection to db:{} completed successfully", url);
            return connection;

        } catch (SQLException exception) {
            error.error("SQLException in getting connection to db:{}", url);
            throw new RuntimeException(exception);
        }
    }
}