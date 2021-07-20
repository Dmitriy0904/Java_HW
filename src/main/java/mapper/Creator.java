package mapper;

import annotation.Mapped;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;


public class Creator {
    private static final Logger log = LoggerFactory.getLogger(Creator.class);
    private final Parser parser = new Parser();

    public <T> T createObject(Class<T> objectClass, Map<String, String> csvValues){
        T newObject;
        log.info("Try to create new object");
        try {
            newObject = objectClass.getDeclaredConstructor().newInstance();

            log.info("Get object fields");
            Field[] objectFields = objectClass.getDeclaredFields();


            for(Field field : objectFields){
                if(field.isAnnotationPresent(Mapped.class)){
                    field.setAccessible(true);
                    Mapped fieldAnnotation = field.getAnnotation(Mapped.class);
                    String annotationValue = csvValues.get(fieldAnnotation.value());
                    parser.parseField(newObject, field, annotationValue);
                }
            }

        } catch (NoSuchMethodException exception) {
            log.error("NoSuchMethodException in method create object");
            throw new RuntimeException(exception);
        } catch (IllegalAccessException exception) {
            log.error("IllegalAccessException in method create object");
            throw new RuntimeException(exception);
        } catch (InstantiationException exception) {
            log.error("InstantiationException in method create object");
            throw new RuntimeException(exception);
        } catch (InvocationTargetException exception) {
            log.error("InvocationTargetException in method create object");
            throw new RuntimeException(exception);
        }

        log.info("Object {} was created successfully", newObject.toString());
        return newObject;
    }
}
