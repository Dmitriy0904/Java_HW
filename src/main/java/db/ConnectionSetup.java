package db;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class ConnectionSetup {
    public static Connection getConnection(){
        Connection connectionToDb;
        Properties props = loadProperties();
        String url = props.getProperty("url");
        try {
            System.out.println("Try to connect");
            connectionToDb = DriverManager.getConnection(url, props);
            System.out.println("Successfully connected");
        } catch (SQLException exception) {
            throw new RuntimeException(exception);
        }
        return connectionToDb;
    }


    private static Properties loadProperties() {
        Properties props = new Properties();
        try(InputStream input = ConnectionSetup.class.getResourceAsStream("/jdbc.properties")) {
            props.load(input);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }

        return props;
    }


}
