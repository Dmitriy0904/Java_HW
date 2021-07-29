package db;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionSetup {
    //ДОБАВИТЬ ЛОГИ
    private final String url = "jdbc:mysql://localhost:3306/module_3_jdbc";
    private final String username;
    private final String password;

    public ConnectionSetup(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public Connection getConnection(){
        try {
            Connection connection = DriverManager.getConnection(url, username, password);
            return connection;
        } catch (SQLException exception) {
            throw new RuntimeException(exception);
        }
    }
}