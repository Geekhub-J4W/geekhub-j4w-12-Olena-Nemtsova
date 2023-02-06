package edu.geekhub.homework.inject;

import edu.geekhub.homework.GeekHubCourse;
import edu.geekhub.homework.util.PropertiesFileReader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


class InjectProcessorTest {
    private GeekHubCourse geekHubCourse;
    private InjectProcessor injectProcessor;

    @BeforeEach
    void setUp() {
        injectProcessor = new InjectProcessor();
        geekHubCourse = new GeekHubCourse();
    }

    @Test
    void can_not_process_with_null_object() {
        IllegalArgumentException thrown = assertThrows(
            IllegalArgumentException.class,
            () -> injectProcessor.process(null)
        );

        assertEquals("Object was null", thrown.getMessage());
    }

    @Test
    void can_not_process_with_object_is_not_GeekHubCourse_class() {
        IllegalArgumentException thrown = assertThrows(
            IllegalArgumentException.class,
            () -> injectProcessor.process(injectProcessor)
        );

        assertEquals("Wrong object class", thrown.getMessage());
    }

    @Test
    void can_set_injectable_value_by_property_name_at_annotation() throws IllegalAccessException {
        Map<String, String> dataFromFile = new HashMap<>(
            Map.of("courseName", "Some Name")
        );
        try (MockedStatic<PropertiesFileReader> fileReaderMock = Mockito.mockStatic(PropertiesFileReader.class)) {
            fileReaderMock.when(PropertiesFileReader::readDataFromFile).thenReturn(dataFromFile);
            injectProcessor = new InjectProcessor();
        }
        InjectProcessor injectProcessor = spy(this.injectProcessor);
        injectProcessor.process(geekHubCourse);

        verify(injectProcessor, times(1)).setFieldValue(any(), any(), any());
    }

    @Test
    void can_set_injectable_value_by_field_name() throws IllegalAccessException {
        Map<String, String> dataFromFile = new HashMap<>(
            Map.of("registrations", "2000")
        );
        try (MockedStatic<PropertiesFileReader> fileReaderMock = Mockito.mockStatic(PropertiesFileReader.class)) {
            fileReaderMock.when(PropertiesFileReader::readDataFromFile).thenReturn(dataFromFile);
            injectProcessor = new InjectProcessor();
        }
        InjectProcessor injectProcessor = spy(this.injectProcessor);
        injectProcessor.process(geekHubCourse);

        verify(injectProcessor, times(1)).setFieldValue(any(), any(), any());
    }

    @Test
    void can_set_field_value() throws IllegalAccessException, NoSuchFieldException {
        Field field = GeekHubCourse.class.getDeclaredField("name");
        field.setAccessible(true);
        injectProcessor.setFieldValue(geekHubCourse, field, "Some Name");

        GeekHubCourse expectedGeekHubCourse = new GeekHubCourse();
        field.set(expectedGeekHubCourse, "Some Name");

        assertThat(field.get(geekHubCourse)).isEqualTo("Some Name");
        assertEquals(expectedGeekHubCourse, geekHubCourse);
    }

    @Test
    void can_get_message_about_not_found_injectable_property_at_file() {
        Map<String, String> dataFromFile = new HashMap<>();
        try (MockedStatic<PropertiesFileReader> fileReaderMock = Mockito.mockStatic(PropertiesFileReader.class)) {
            fileReaderMock.when(PropertiesFileReader::readDataFromFile).thenReturn(dataFromFile);
            injectProcessor = new InjectProcessor();
        }
        String message = injectProcessor.getMessageAboutNotSetInjectableProperty("courseName");

        assertThat(message).isEqualTo("Couldn't found property 'courseName' at file");
    }

    @Test
    void can_get_message_about_wrong_named_injectable_property_at_file() {
        Map<String, String> dataFromFile = new HashMap<>(
            Map.of("courseDuration", "15")
        );
        try (MockedStatic<PropertiesFileReader> fileReaderMock = Mockito.mockStatic(PropertiesFileReader.class)) {
            fileReaderMock.when(PropertiesFileReader::readDataFromFile).thenReturn(dataFromFile);
            injectProcessor = new InjectProcessor();
        }
        String message = injectProcessor.getMessageAboutNotSetInjectableProperty("duration");

        assertThat(message).isEqualTo("Couldn't set file property: 'courseDuration', actual: 'duration'");
    }
}
