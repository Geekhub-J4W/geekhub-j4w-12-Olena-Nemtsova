package edu.geekhub.homework.util;

import org.junit.jupiter.api.Test;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class PropertiesFileReaderTest {

    @Test
    void can_check_valid_of_empty_file_line() throws Exception {
        Method setFieldValueMethod = PropertiesFileReader.class
            .getDeclaredMethod("validFileLine", String.class);
        setFieldValueMethod.setAccessible(true);
        boolean isValid = (boolean) setFieldValueMethod.invoke(null, "");

        assertFalse(isValid);
    }

    @Test
    void can_check_valid_of_wrong_file_line() throws Exception {
        Method setFieldValueMethod = PropertiesFileReader.class
            .getDeclaredMethod("validFileLine", String.class);
        setFieldValueMethod.setAccessible(true);
        boolean isValid = (boolean) setFieldValueMethod.invoke(null, "Property Value");

        assertFalse(isValid);
    }

    @Test
    void can_check_valid_of_correct_file_line() throws Exception {
        Method setFieldValueMethod = PropertiesFileReader.class
            .getDeclaredMethod("validFileLine", String.class);
        setFieldValueMethod.setAccessible(true);
        boolean isValid = (boolean) setFieldValueMethod.invoke(null, "Property=Value");

        assertTrue(isValid);
    }

    @Test
    void can_get_property_name_from_file_line() throws Exception {
        Method setFieldValueMethod = PropertiesFileReader.class
            .getDeclaredMethod("getPropertyName", String.class);
        setFieldValueMethod.setAccessible(true);
        String propertyName = (String) setFieldValueMethod.invoke(null, "gh.inject.Property=Value");

        assertEquals("Property", propertyName);
    }

    @Test
    void can_get_property_value_from_file_line() throws Exception {
        Method setFieldValueMethod = PropertiesFileReader.class
            .getDeclaredMethod("getPropertyValue", String.class);
        setFieldValueMethod.setAccessible(true);
        String propertyValue = (String) setFieldValueMethod.invoke(null, "Property=Value");

        assertEquals("Value", propertyValue);
    }

    @Test
    void can_get_map_of_properties_read_from_file() throws Exception {
        Path tempPropertiesFilePath = generateTempFile("Property = Value");
        Field field = PropertiesFileReader.class.getDeclaredField("propertiesFilePath");
        field.setAccessible(true);
        field.set(null, tempPropertiesFilePath);
        Map<String, String> properties = PropertiesFileReader.readDataFromFile();

        Map<String, String> expectedProperties = Map.of("Property", "Value");

        assertEquals(expectedProperties, properties);
    }

    @Test
    void can_get_empty_map_of_properties_read_from_empty_file() throws Exception {
        Path tempPropertiesFilePath = generateTempFile("");
        Field field = PropertiesFileReader.class.getDeclaredField("propertiesFilePath");
        field.setAccessible(true);
        field.set(null, tempPropertiesFilePath);
        Map<String, String> properties = PropertiesFileReader.readDataFromFile();

        Map<String, String> expectedProperties = new HashMap<>();

        assertEquals(expectedProperties, properties);
    }

    @Test
    void can_get_map_of_properties_read_from_file_contains_wrong_line() throws Exception {
        Path tempPropertiesFilePath = generateTempFile("Property = Value\n Wrong line");
        Field field = PropertiesFileReader.class.getDeclaredField("propertiesFilePath");
        field.setAccessible(true);
        field.set(null, tempPropertiesFilePath);
        Map<String, String> properties = PropertiesFileReader.readDataFromFile();

        Map<String, String> expectedProperties = Map.of("Property", "Value");

        assertEquals(expectedProperties, properties);
    }

    private Path generateTempFile(String dataToWrite) throws IOException {
        Path tempFilePath = Files.createTempFile("properties", ".txt");
        try (FileOutputStream fos = new FileOutputStream(tempFilePath.toFile(), false);
             PrintStream printStream = new PrintStream(fos)) {
            printStream.println(dataToWrite);
        }
        return tempFilePath;
    }
}
