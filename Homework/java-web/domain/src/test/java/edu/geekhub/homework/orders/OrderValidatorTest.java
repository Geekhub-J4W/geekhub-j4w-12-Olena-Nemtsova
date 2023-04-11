package edu.geekhub.homework.orders;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;

import edu.geekhub.homework.users.User;
import edu.geekhub.homework.users.interfaces.UserRepository;
import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class OrderValidatorTest {
    private OrderValidator orderValidator;
    @Mock
    private UserRepository userRepository;
    private Order order;

    @BeforeEach
    void setUp() {
        orderValidator = new OrderValidator(userRepository);
        order = spy(
            new Order(1, LocalDateTime.now(), 100.0, 1,
                OrderStatus.PENDING, "some address", "Mark Pearce")
        );
    }

    @Test
    void can_not_validate_null_order() {
        IllegalArgumentException thrown = assertThrows(
            IllegalArgumentException.class,
            () -> orderValidator.validate(null)
        );

        assertEquals("Order was null", thrown.getMessage());
    }

    @Test
    void can_not_validate_order_with_null_status() {
        doReturn(new User()).when(userRepository).getUserById(anyInt());
        doReturn(null).when(order).getStatus();
        IllegalArgumentException thrown = assertThrows(
            IllegalArgumentException.class,
            () -> orderValidator.validate(order)
        );

        assertEquals("Order status was null", thrown.getMessage());
    }

    @ParameterizedTest
    @CsvSource({
        ", Order customer name was null or empty",
        "'', Order customer name was null or empty",
        "Mark, Order customer name contained less than 2 words"
    })
    void can_not_validate_order_with_wrong_name(String customerName, String expectedMessage) {
        doReturn(new User()).when(userRepository).getUserById(anyInt());
        doReturn(customerName).when(order).getCustomerName();

        IllegalArgumentException thrown = assertThrows(
            IllegalArgumentException.class,
            () -> orderValidator.validate(order)
        );

        assertEquals(expectedMessage, thrown.getMessage());
    }

    @ParameterizedTest
    @CsvSource({
        ", Order address was null or empty",
        "'', Order address was null or empty"
    })
    void can_not_validate_order_with_wrong_address(String address, String expectedMessage) {
        doReturn(new User()).when(userRepository).getUserById(anyInt());
        doReturn(address).when(order).getAddress();

        IllegalArgumentException thrown = assertThrows(
            IllegalArgumentException.class,
            () -> orderValidator.validate(order)
        );

        assertEquals(expectedMessage, thrown.getMessage());
    }

    @Test
    void can_not_validate_order_with_not_exists_user() {
        doReturn(null).when(userRepository).getUserById(anyInt());

        IllegalArgumentException thrown = assertThrows(
            IllegalArgumentException.class,
            () -> orderValidator.validate(order)
        );

        assertEquals("Order had not exists user id", thrown.getMessage());
    }

    @ParameterizedTest
    @CsvSource({
        "-1, Order total price was less than 0",
        "1.456, Order total price had too much numbers after point"
    })
    void can_not_validate_order_with_wrong_totalPrice(Double price, String expectedMessage) {
        doReturn(price).when(order).getTotalPrice();

        IllegalArgumentException thrown = assertThrows(
            IllegalArgumentException.class,
            () -> orderValidator.validate(order)
        );

        assertEquals(expectedMessage, thrown.getMessage());
    }

    @Test
    void can_not_validate_order_with_null_dateTime() {
        doReturn(null).when(order).getDateTime();

        IllegalArgumentException thrown = assertThrows(
            IllegalArgumentException.class,
            () -> orderValidator.validate(order)
        );

        assertEquals("Order dateTime was null", thrown.getMessage());
    }

    @Test
    void can_not_validate_order_with_dateTime_more_than_now() {
        LocalDateTime future = LocalDateTime.of(2050, 1, 1, 0, 0);
        doReturn(future).when(order).getDateTime();

        IllegalArgumentException thrown = assertThrows(
            IllegalArgumentException.class,
            () -> orderValidator.validate(order)
        );

        assertEquals("Order dateTime was more than now", thrown.getMessage());
    }

    @Test
    void can_validate_correct_order() {
        doReturn(new User()).when(userRepository).getUserById(anyInt());

        assertDoesNotThrow(
            () -> orderValidator.validate(order)
        );
    }
}
