package edu.geekhub.homework.hw1;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class OddIndexIterableTest {
    private OddIndexIterable<Integer> integers;

    @BeforeEach
    void setUp() {
        integers = new OddIndexIterable<>();
    }

    @Test
    void can_add_value() {
        integers.add(1);
        integers.add(2);

        int number = -1;
        for (var i : integers) {
            number = i;
        }
        assertEquals(2, number);
    }

    @Test
    void can_get_by_only_odd_indexes() {
        for (int i = 1; i < 5; i++) {
            integers.add(i);
        }
        List<Integer> list = new LinkedList<>();
        for (int i : integers) {
            list.add(i);
        }

        List<Integer> expectedList = new LinkedList<>();
        expectedList.add(2);
        expectedList.add(4);

        assertEquals(expectedList, list);
    }
}
