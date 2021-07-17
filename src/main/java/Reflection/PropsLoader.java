package Reflection;

import Config.Paths;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.util.Properties;

public class PropsLoader {
    private static final String PROPERTIES_PATH = Paths.PROPS_PATH.getPath();
    private static final Logger log = LoggerFactory.getLogger(AppProperties.class);

    public static Properties loadProperties(){
        log.info("Try to load properties from file {}", PROPERTIES_PATH);
        Properties properties = new Properties();

        try(InputStream input = PropsLoader.class.getResourceAsStream(PROPERTIES_PATH)) {
            properties.load(input);

        } catch (IOException e) {
            log.error("IOException in method load properties from file {}", PROPERTIES_PATH);
            throw new UncheckedIOException(e);
        }

        log.info("Properties were loaded successfully");
        return properties;
    }
}
