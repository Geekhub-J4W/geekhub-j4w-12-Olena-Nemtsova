package edu.geekhub.coursework.chats;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;

import edu.geekhub.coursework.users.Role;
import edu.geekhub.coursework.users.User;
import edu.geekhub.coursework.users.interfaces.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ChatValidatorTest {
    private ChatValidator validator;
    private Chat chat;
    @Mock
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        validator = new ChatValidator(userRepository);

        chat = spy(
            new Chat(1, 1, 1)
        );
    }

    @Test
    void can_not_validate_null_chat() {
        IllegalArgumentException thrown = assertThrows(
            IllegalArgumentException.class,
            () -> validator.validate(null)
        );

        assertEquals("Chat was null", thrown.getMessage());
    }

    @Test
    void can_not_validate_chat_with_wrong_userId() {
        doReturn(null).when(userRepository).getUserById(anyInt());

        IllegalArgumentException thrown = assertThrows(
            IllegalArgumentException.class,
            () -> validator.validate(chat)
        );

        assertEquals("Chat had not exists user id", thrown.getMessage());
    }

    @Test
    void can_not_validate_chat_with_not_existing_adminId() {
        doReturn(new User(), (Object) null).when(userRepository).getUserById(anyInt());

        IllegalArgumentException thrown = assertThrows(
            IllegalArgumentException.class,
            () -> validator.validate(chat)
        );

        assertEquals("Chat had not exists admin id", thrown.getMessage());
    }

    @Test
    void can_not_validate_chat_with_wrong_adminId() {
        User user = new User();
        user.setRole(Role.USER);
        doReturn(new User(), user).when(userRepository).getUserById(anyInt());

        IllegalArgumentException thrown = assertThrows(
            IllegalArgumentException.class,
            () -> validator.validate(chat)
        );

        assertEquals("Chat had wrong admin id", thrown.getMessage());
    }

    @Test
    void can_validate_correct_chat() {
        User admin = new User();
        admin.setRole(Role.ADMIN);
        doReturn(new User(), admin).when(userRepository).getUserById(anyInt());

        assertDoesNotThrow(
            () -> validator.validate(chat)
        );
    }
}
