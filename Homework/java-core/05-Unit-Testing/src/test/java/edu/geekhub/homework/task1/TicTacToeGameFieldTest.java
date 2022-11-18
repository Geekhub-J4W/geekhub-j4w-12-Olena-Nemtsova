package edu.geekhub.homework.task1;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class TicTacToeGameFieldTest {
    private TicTacToeGameField ticTacToeGameField;

    @BeforeEach
    void setUp() {
        ticTacToeGameField = new TicTacToeGameField();
    }

    @Test
    void failed_generate_null_input() {
        assertThrows(
            NullPointerException.class,
            () -> ticTacToeGameField.generateField(null)
        );
    }

    @Test
    void failed_generate_empty_input() {
        assertThrows(
            IllegalArgumentException.class,
            () -> ticTacToeGameField.generateField("")
        );
    }

    @Test
    void failed_generate_input_with_wrong_length() {
        assertThrows(
            IllegalArgumentException.class,
            () -> ticTacToeGameField.generateField("XOX")
        );
    }

    @Test
    void failed_generate_input_with_not_allowed_characters() {
        assertThrows(
            IllegalArgumentException.class,
            () -> ticTacToeGameField.generateField("XO^._.^OX")
        );
    }

    @Test
    void generate_correct_input() {
        var field = ticTacToeGameField.generateField("XOX   OXO");

        String expectedField = "+-----+" + System.lineSeparator() +
            "|X|O|X|" + System.lineSeparator() +
            "| | | |" + System.lineSeparator() +
            "|O|X|O|" + System.lineSeparator() +
            "+-----+";

        assertEquals(expectedField, field);
    }

    @Test
    void failed_save_null_input() {
        assertThrows(
            IllegalArgumentException.class,
            () -> ticTacToeGameField.saveFieldState(null)
        );
    }

    @Test
    void failed_save_empty_input() {
        assertThrows(
            IllegalArgumentException.class,
            () -> ticTacToeGameField.saveFieldState("")
        );
    }

    @Test
    void failed_save_input_with_not_allowed_characters() {
        String field = "+-----+" + System.lineSeparator() +
            "|s|o|m|" + System.lineSeparator() +
            "|e|F|i|" + System.lineSeparator() +
            "|e|l|d|" + System.lineSeparator() +
            "+-----+";

        assertThrows(
            IllegalArgumentException.class,
            () -> ticTacToeGameField.saveFieldState(field)
        );
    }

    @Test
    void failed_save_input_with_wrong_length() {
        String field = "+-----+" + System.lineSeparator() +
            "|X|O|X|" + System.lineSeparator() +
            "+-----+";

        assertThrows(
            IllegalArgumentException.class,
            () -> ticTacToeGameField.saveFieldState(field)
        );
    }

    @Test
    void failed_save_input_without_allowed_line_separator() {
        String field = """
            +-----+
            |X|O|X|
            | | | |
            |O|X|O|
            +-----+""";

        assertThrows(
            IllegalArgumentException.class,
            () -> ticTacToeGameField.saveFieldState(field)
        );
    }

    @Test
    void save_input_without_field_borders() {
        String field = "XOX   OXO";
        String fieldState = ticTacToeGameField.saveFieldState(field);

        String expectedField = "XOX   OXO";

        assertEquals(expectedField, fieldState);
    }

    @Test
    void save_correct_input() {
        String field = "+-----+" + System.lineSeparator() +
            "|X|O|X|" + System.lineSeparator() +
            "| | | |" + System.lineSeparator() +
            "|O|X|O|" + System.lineSeparator() +
            "+-----+";
        String fieldState = ticTacToeGameField.saveFieldState(field);

        String expectedField = "XOX   OXO";

        assertEquals(expectedField, fieldState);
    }
}
