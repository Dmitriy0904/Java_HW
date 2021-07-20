package mapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.lang.reflect.Field;


public class Parser {
    private static final Logger log = LoggerFactory.getLogger(Parser.class);

    public <T> void parseField(T object, Field fieldToParse, String annotationValue){
        log.info("Starting to parse field {}", fieldToParse);
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
            log.error("IllegalAccessException in parsing field {}", fieldToParse);
            throw new RuntimeException(exception);
        }
        log.info("Field was parsed successfully");
    }
}
