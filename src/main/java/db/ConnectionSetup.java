package db;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class ConnectionSetup {
    private static final String PROPS_PATH = "/jdbc.properties";
    private static final Logger infoLog = LoggerFactory.getLogger("info");
    private static final Logger errorLog = LoggerFactory.getLogger("error");


    public static Connection getConnection(){
        Connection connectionToDb;
        Properties props = loadProperties();
        String url = props.getProperty("url");

        infoLog.info("Try to get connection to the database {}", url);

        try {
            connectionToDb = DriverManager.getConnection(url, props);
        } catch (SQLException exception) {
            errorLog.error("SQLException in connection to database {}", url);
            throw new RuntimeException(exception);
        }

        infoLog.info("Successfully connected");
        return connectionToDb;
    }


    private static Properties loadProperties() {
        infoLog.info("Try to load properties from {}",  PROPS_PATH);
        Properties props = new Properties();

        try(InputStream input = ConnectionSetup.class.getResourceAsStream(PROPS_PATH)) {
            props.load(input);
        } catch (IOException e) {
            errorLog.error("IOException in load properties method");
            throw new UncheckedIOException(e);
        }

        infoLog.info("Properties were loaded successfully");
        return props;
    }

}