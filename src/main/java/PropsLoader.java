import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.util.Properties;

public class PropsLoader {
    private static final String PROPS_PATH = "/app.properties";

    public static Properties loadProperties(){
        Properties props = new Properties();
        try(InputStream input = PropsLoader.class.getResourceAsStream(PROPS_PATH)) {
            props.load(input);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
        return props;
    }
}
