package edu.geekhub.coursework.util;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PageValidatorTest {
    private PageValidator validator;

    @BeforeEach
    void setUp() {
        validator = new PageValidator();
    }

    @ParameterizedTest
    @CsvSource({
        "0, Minimum page items limit is 1 but was: 0",
        "-10, Minimum page items limit is 1 but was: -10"
    })
    void can_not_validate_page_limit_equals_or_less_than_zero(int limit, String expectedMessage) {
        IllegalArgumentException thrown = assertThrows(
            IllegalArgumentException.class,
            () -> validator.validatePageLimit(limit)
        );

        assertEquals(expectedMessage, thrown.getMessage());
    }

    @Test
    void can_validate_correct_page_limit() {
        assertDoesNotThrow(
            () -> validator.validatePageLimit(10)
        );
    }

    @ParameterizedTest
    @CsvSource({
        "0, Minimum page number is 1 but was: 0",
        "10, Maximum page number is 5 but was: 10"
    })
    void can_not_validate_wrong_page_number(int pageNumber, String expectedMessage) {
        IllegalArgumentException thrown = assertThrows(
            IllegalArgumentException.class,
            () -> validator.validatePageNumber(pageNumber, 5)
        );

        assertEquals(expectedMessage, thrown.getMessage());
    }

    @Test
    void can_validate_correct_page_number() {
        assertDoesNotThrow(
            () -> validator.validatePageNumber(2, 5)
        );
    }
}
