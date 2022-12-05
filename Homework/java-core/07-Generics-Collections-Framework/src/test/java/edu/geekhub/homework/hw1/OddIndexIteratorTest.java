package edu.geekhub.homework.hw1;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class OddIndexIteratorTest {
    private OddIndexIterator<Integer> oddIndexIterator;

    @BeforeEach
    void setUp() {
        List<Integer> list = new LinkedList<>();
        list.add(1);
        list.add(2);

        oddIndexIterator = new OddIndexIterator<>(list);
    }

    @Test
    void can_get_false_position_more_length() {
        oddIndexIterator.position = 5;

        assertFalse(oddIndexIterator.hasNext());
    }

    @Test
    void can_get_false_position_less_0() {
        oddIndexIterator.position = -1;

        assertFalse(oddIndexIterator.hasNext());
    }

    @Test
    void can_get_false_even_position() {
        oddIndexIterator.position = 0;

        assertFalse(oddIndexIterator.hasNext());
    }

    @Test
    void can_get_true_correct_position() {
        oddIndexIterator.position = 1;

        assertTrue(oddIndexIterator.hasNext());
    }

    @Test
    void can_not_get_value_position_more_length() {
        oddIndexIterator.position = 5;

        assertThrows(
            IllegalArgumentException.class,
            () -> oddIndexIterator.next()
        );
    }

    @Test
    void can_not_get_value_position_less_0() {
        oddIndexIterator.position = -1;

        assertThrows(
            IllegalArgumentException.class,
            () -> oddIndexIterator.next()
        );
    }

    @Test
    void can_not_get_value_even_position() {
        oddIndexIterator.position = 0;

        assertThrows(
            IllegalArgumentException.class,
            () -> oddIndexIterator.next()
        );
    }

    @Test
    void can_get_value_correct_position() {
        assertEquals(2, oddIndexIterator.next());
    }
}
