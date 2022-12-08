package edu.geekhub.orcostat.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TrivialCollectionTest {
    private TrivialCollection trivialCollection;

    @BeforeEach
    void setUp() {
        trivialCollection = new TrivialCollection();
    }

    @Test
    void can_add_element() {
        trivialCollection.add(new Object());

        assertEquals(1, trivialCollection.count());
    }

    @Test
    void can_add_two_elements() {
        trivialCollection.add(new Object());
        trivialCollection.add(new Object());

        assertEquals(2, trivialCollection.count());
    }

    @Test
    void cannot_add_undefined_element() {
        IllegalArgumentException thrown = assertThrows(
            IllegalArgumentException.class,
            () -> trivialCollection.add(null)
        );
        assertEquals("Object cannot be null", thrown.getMessage());
    }

    @Test
    void can_get_object_array() {
        int[] expectedArray = new int[3];
        for (int i = 0; i < 3; i++) {
            expectedArray[i] = i;
            trivialCollection.add(expectedArray[i]);
        }

        var objectArray = trivialCollection.getData();
        int[] array = new int[objectArray.length];
        for (int i = 0; i < objectArray.length; i++) {
            array[i] = (int) objectArray[i];
        }

        assertArrayEquals(expectedArray, array);
    }

    @Test
    void can_get_value_by_index() {
        for (int i = 0; i < 3; i++) {
            trivialCollection.add(i);
        }
        var value = (int) trivialCollection.getDataByIndex(1);

        assertEquals(1, value);
    }

    @Test
    void cannot_get_value_by_negative_index() {
        for (int i = 0; i < 3; i++) {
            trivialCollection.add(i);
        }
        assertThrows(
            IllegalArgumentException.class,
            () -> trivialCollection.getDataByIndex(-2)
        );
    }

    @Test
    void cannot_get_value_by_index_more_array_length() {
        for (int i = 0; i < 3; i++) {
            trivialCollection.add(i);
        }
        assertThrows(
            IllegalArgumentException.class,
            () -> trivialCollection.getDataByIndex(10)
        );
    }

    @Test
    void can_change_value() {
        trivialCollection.add(5);
        trivialCollection.changeValue(0, 10);
        var value = (int) trivialCollection.getDataByIndex(0);

        assertEquals(10, value);
    }

    @Test
    void cannot_change_value_by_negative_index() {
        for (int i = 0; i < 3; i++) {
            trivialCollection.add(i);
        }
        assertThrows(
            IllegalArgumentException.class,
            () -> trivialCollection.changeValue(-5, 10)
        );
    }

    @Test
    void cannot_change_value_by_index_more_array_length() {
        for (int i = 0; i < 3; i++) {
            trivialCollection.add(i);
        }
        assertThrows(
            IllegalArgumentException.class,
            () -> trivialCollection.changeValue(20, 10)
        );
    }

    @Test
    void cannot_change_to_null_value() {
        for (int i = 0; i < 3; i++) {
            trivialCollection.add(i);
        }
        assertThrows(
            IllegalArgumentException.class,
            () -> trivialCollection.changeValue(0, null)
        );
    }

}
