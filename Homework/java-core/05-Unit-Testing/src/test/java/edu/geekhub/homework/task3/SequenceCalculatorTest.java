package edu.geekhub.homework.task3;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SequenceCalculatorTest {

    private static final String NUMBERS_INPUT = "10,2,3";
    private SequenceCalculator sequenceCalculator;

    @BeforeEach
    void setUp() {
        sequenceCalculator = new SequenceCalculator();
    }

    @Test
    void failed_calculate_null_input() {
        assertThrows(
            NullPointerException.class,
            () -> sequenceCalculator.calculate(null, ArithmeticOperation.ADD)
        );
    }

    @Test
    void failed_calculate_empty_input() {
        assertThrows(
            IllegalArgumentException.class,
            () -> sequenceCalculator.calculate("", ArithmeticOperation.ADD)
        );
    }

    @Test
    void failed_calculate_input_without_numbers_in_() {
        assertThrows(
            IllegalArgumentException.class,
            () -> sequenceCalculator.calculate("some, without any numbers, input", ArithmeticOperation.ADD)
        );
    }

    @Test
    void failed_calculate_null_operation() {
        assertThrows(
            NullPointerException.class,
            () -> sequenceCalculator.calculate(NUMBERS_INPUT, null)
        );
    }

    @Test
    void failed_calculate_operation_division_by_zero() {
        assertThrows(
            ArithmeticException.class,
            () -> sequenceCalculator.calculate("2, 0, 9", ArithmeticOperation.DIVISION)
        );
    }

    @Test
    void calculate_operation_add() {
        var result = sequenceCalculator.calculate(NUMBERS_INPUT, ArithmeticOperation.ADD);

        int expectedResult = 15;

        assertEquals(expectedResult, result);
    }

    @Test
    void calculate_operation_multiply() {
        var result = sequenceCalculator.calculate(NUMBERS_INPUT, ArithmeticOperation.MULTIPLY);

        int expectedResult = 60;

        assertEquals(expectedResult, result);
    }

    @Test
    void calculate_operation_subtract() {
        var result = sequenceCalculator.calculate(NUMBERS_INPUT, ArithmeticOperation.SUBTRACT);

        int expectedResult = 5;

        assertEquals(expectedResult, result);
    }

    @Test
    void calculate_operation_division() {
        var result = sequenceCalculator.calculate(NUMBERS_INPUT, ArithmeticOperation.DIVISION);

        int expectedResult = 1;

        assertEquals(expectedResult, result);
    }

    @Test
    void calculate_input_with_spaces_around_values() {
        String input = "1 0 , 2 , 3 ";
        var result = sequenceCalculator.calculate(input, ArithmeticOperation.ADD);

        int expectedResult = 15;

        assertEquals(expectedResult, result);
    }

    @Test
    void calculate_input_with_special_characters() {
        String input = "1!#0$,&2^%,!!3@@";
        var result = sequenceCalculator.calculate(input, ArithmeticOperation.ADD);

        int expectedResult = 15;

        assertEquals(expectedResult, result);
    }

    @Test
    void calculate_input_with_odd_number_count() {
        var result = sequenceCalculator.calculate(NUMBERS_INPUT, ArithmeticOperation.ADD);

        int expectedResult = 15;

        assertEquals(expectedResult, result);
    }

    @Test
    void calculate_input_with_even_number_count() {
        String input = "10,2,3,1";
        var result = sequenceCalculator.calculate(input, ArithmeticOperation.ADD);

        int expectedResult = 16;

        assertEquals(expectedResult, result);
    }
}
