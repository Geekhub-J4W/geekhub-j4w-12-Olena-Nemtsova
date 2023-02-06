package edu.geekhub.homework.inject;

import edu.geekhub.homework.GeekHubCourse;
import edu.geekhub.homework.util.PropertiesFileReader;
import edu.geekhub.homework.util.TypeParser;

import java.lang.reflect.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class InjectProcessor {
    private final Map<String, String> propertiesFromFile = PropertiesFileReader.readDataFromFile();

    public void process(Object object) throws IllegalAccessException {
        validateObject(object);
        Class<GeekHubCourse> clazz = GeekHubCourse.class;

        List<String> notSetProperties = new ArrayList<>();
        for (Field field : clazz.getDeclaredFields()) {
            if (field.isAnnotationPresent(Injectable.class)) {
                Injectable injectable = field.getAnnotation(Injectable.class);
                String propertyName = injectable.propertyName();

                if (!propertyName.isBlank() && propertiesFromFile.containsKey(propertyName)) {
                    setFieldValue(object, field, propertiesFromFile.get(propertyName));
                    propertiesFromFile.remove(propertyName);
                } else if (propertyName.isBlank() && propertiesFromFile.containsKey(field.getName())) {
                    setFieldValue(object, field, propertiesFromFile.get(field.getName()));
                    propertiesFromFile.remove(propertyName);
                } else if (!propertyName.isBlank()) {
                    notSetProperties.add(propertyName);
                } else {
                    notSetProperties.add(field.getName());
                }
            }
        }

        Logger logger = Logger.getGlobal();
        for (String notSetProperty : notSetProperties) {
            String message = getMessageAboutNotSetInjectableProperty(notSetProperty);
            logger.log(Level.INFO, message);
        }
    }

    private void validateObject(Object object) {
        if (object == null) {
            throw new IllegalArgumentException("Object was null");
        }
        if (object.getClass() != GeekHubCourse.class) {
            throw new IllegalArgumentException("Wrong object class");
        }
    }

    protected void setFieldValue(Object object, Field field, String stringValue) throws IllegalAccessException {
        Type type = field.getGenericType();
        var value = TypeParser.parseByType(type, stringValue);
        field.setAccessible(true);
        field.set(object, value);
    }

    protected String getMessageAboutNotSetInjectableProperty(String notSetProperty) {
        String key = getSimilarKeyToNotSetProperty(propertiesFromFile.keySet().stream().toList(), notSetProperty);
        if (!key.isBlank()) {
            return "Couldn't set file property: '" + key + "', actual: '" + notSetProperty + "'";
        } else {
            return "Couldn't found property '" + notSetProperty + "' at file";
        }
    }

    private String getSimilarKeyToNotSetProperty(List<String> keys, String notSetProperty) {
        return keys.stream()
            .filter((String property) -> property.toLowerCase().contains(notSetProperty.toLowerCase())
                || notSetProperty.toLowerCase().contains(property.toLowerCase()))
            .findFirst()
            .orElse("");
    }
}
