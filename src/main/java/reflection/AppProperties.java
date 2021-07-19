package reflection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Properties;

public class AppProperties {
    private static final Logger log = LoggerFactory.getLogger(AppProperties.class);

    public <T> T createObject(Class<T> objectClass){
        T newObject;
        try {
            log.info("Try to create new object");
            newObject = objectClass.getDeclaredConstructor().newInstance();

            log.info("Loading object properties");
            PropsLoader propsLoader = new PropsLoader();
            Properties objectProps = propsLoader.loadProperties();

            log.info("Get object fields");
            Field[] objectFields = objectClass.getDeclaredFields();

            for(Field field : objectFields){
                if(field.isAnnotationPresent(PropertyKey.class)){
                    field.setAccessible(true);
                    PropertyKey propertyKey = field.getAnnotation(PropertyKey.class);
                    String annotationValue = objectProps.getProperty(propertyKey.value());
                    log.info("Parsing object field: {}", field.toString());
                    parseField(newObject, field, annotationValue);
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

        log.info("Object was created successfully");
        return newObject;
    }


    private <T> void parseField(T object, Field fieldToParse, String annotationValue){
        Class<?> typeOfField = fieldToParse.getType();
        try {

            if(typeOfField.isPrimitive()){

                if(typeOfField.isAssignableFrom(byte.class))
                    fieldToParse.set(object, Byte.parseByte(annotationValue));

                else if(typeOfField.isAssignableFrom(short.class))
                    fieldToParse.set(object, Short.parseShort(annotationValue));

                else if(typeOfField.isAssignableFrom(int.class))
                    fieldToParse.set(object, Integer.parseInt(annotationValue));

                else if(typeOfField.isAssignableFrom(long.class))
                    fieldToParse.set(object, Long.parseLong(annotationValue));

                else if(typeOfField.isAssignableFrom(float.class))
                    fieldToParse.set(object, Float.parseFloat(annotationValue));

                else if(typeOfField.isAssignableFrom(double.class))
                    fieldToParse.set(object, Double.parseDouble(annotationValue));

                else if(typeOfField.isAssignableFrom(boolean.class))
                    fieldToParse.set(object, Boolean.parseBoolean(annotationValue));

                else if(typeOfField.isAssignableFrom(char.class))
                    fieldToParse.set(object, annotationValue.charAt(0));
            }
            else {
                fieldToParse.set(object, annotationValue);
            }

        } catch (IllegalAccessException exception){
            throw new RuntimeException(exception);
        }

    }
}