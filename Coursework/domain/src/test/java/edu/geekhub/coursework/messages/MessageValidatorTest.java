package edu.geekhub.coursework.messages;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;

import edu.geekhub.coursework.chats.Chat;
import edu.geekhub.coursework.chats.intefaces.ChatRepository;
import edu.geekhub.coursework.users.User;
import edu.geekhub.coursework.users.interfaces.UserRepository;
import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MessageValidatorTest {
    private MessageValidator validator;
    private Message message;
    @Mock
    private ChatRepository chatRepository;
    @Mock
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        validator = new MessageValidator(chatRepository, userRepository);

        message = spy(
            new Message(1, "text", LocalDateTime.now(), 1, 1)
        );
    }

    @Test
    void can_not_validate_null_message() {
        IllegalArgumentException thrown = assertThrows(
            IllegalArgumentException.class,
            () -> validator.validate(null)
        );

        assertEquals("Message was null", thrown.getMessage());
    }

    @ParameterizedTest
    @CsvSource({
        ", Message text was null",
        "'', Message text was empty",
        "m, Message text had wrong length"
    })
    void can_not_validate_message_with_wrong_text(String text, String expectedMessage) {
        if (text != null && text.equals("m")) {
            text = text.repeat(70000);
        }
        doReturn(text).when(message).getText();

        IllegalArgumentException thrown = assertThrows(
            IllegalArgumentException.class,
            () -> validator.validate(message)
        );

        assertEquals(expectedMessage, thrown.getMessage());
    }

    @Test
    void can_not_validate_message_with_null_dateTime() {
        doReturn(null).when(message).getDateTime();

        IllegalArgumentException thrown = assertThrows(
            IllegalArgumentException.class,
            () -> validator.validate(message)
        );

        assertEquals("Message dateTime was null", thrown.getMessage());
    }

    @Test
    void can_not_validate_message_with_dateTime_more_than_now() {
        LocalDateTime future = LocalDateTime.of(2050, 1, 1, 0, 0);
        doReturn(future).when(message).getDateTime();

        IllegalArgumentException thrown = assertThrows(
            IllegalArgumentException.class,
            () -> validator.validate(message)
        );

        assertEquals("Message dateTime was more than now", thrown.getMessage());
    }

    @Test
    void can_not_validate_message_with_wrong_chatId() {
        doReturn(null).when(chatRepository).getChatById(anyInt());

        IllegalArgumentException thrown = assertThrows(
            IllegalArgumentException.class,
            () -> validator.validate(message)
        );

        assertEquals("Message had not exists chat id", thrown.getMessage());
    }

    @Test
    void can_not_validate_message_with_wrong_senderId() {
        doReturn(new Chat()).when(chatRepository).getChatById(anyInt());
        doReturn(null).when(userRepository).getUserById(anyInt());

        IllegalArgumentException thrown = assertThrows(
            IllegalArgumentException.class,
            () -> validator.validate(message)
        );

        assertEquals("Message had not exists sender id", thrown.getMessage());
    }

    @Test
    void can_validate_correct_message() {
        doReturn(new Chat()).when(chatRepository).getChatById(anyInt());
        doReturn(new User()).when(userRepository).getUserById(anyInt());

        assertDoesNotThrow(
            () -> validator.validate(message)
        );
    }
}
