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
    private static final Logger log = LoggerFactory.getLogger(ConnectionSetup.class);

    public static Connection getConnection(){
        Connection connectionToDb;
        Properties props = loadProperties();
        String url = props.getProperty("url");

        log.info("Try to get connection to the database {}", url);

        try {
            connectionToDb = DriverManager.getConnection(url, props);
        } catch (SQLException exception) {
            log.error("SQLException in connection to database {}", url);
            throw new RuntimeException(exception);
        }

        log.info("Successfully connected");
        return connectionToDb;
    }


    private static Properties loadProperties() {
        log.info("Try to load properties from {}",  PROPS_PATH);
        Properties props = new Properties();

        try(InputStream input = ConnectionSetup.class.getResourceAsStream(PROPS_PATH)) {
            props.load(input);
        } catch (IOException e) {
            log.error("IOException in load properties method");
            throw new UncheckedIOException(e);
        }

        log.info("Properties were loaded successfully");
        return props;
    }

}