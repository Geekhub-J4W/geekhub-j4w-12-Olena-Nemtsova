package edu.geekhub.coursework.messages;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;

import edu.geekhub.coursework.TestApplication;
import edu.geekhub.coursework.config.DatabaseContainer;
import edu.geekhub.coursework.config.DatabaseTestConfig;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.ClassRule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@DataJdbcTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(classes = {DatabaseTestConfig.class, TestApplication.class})
@Testcontainers
class MessageRepositoryImplTest {
    @ClassRule
    @Container
    public static PostgreSQLContainer<DatabaseContainer> postgresSQLContainer =
        DatabaseContainer.getInstance();
    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;
    @Mock
    private MessageValidator validator;
    private MessageRepositoryImpl messageRepository;

    @BeforeEach
    void setUp() {
        messageRepository = new MessageRepositoryImpl(validator, jdbcTemplate);
    }

    @Test
    void can_get_messages_by_user_id() {
        List<Message> expectedMessages = List.of(
            new Message(1, "message #1", getTestDateTime(), 1, 3),
            new Message(2, "message #2", getTestDateTime(), 1, 2)
        );

        List<Message> messages = messageRepository.getMessagesByUserId(3);
        messages.forEach(message -> message.setDateTime(getTestDateTime()));

        assertEquals(expectedMessages, messages);
    }

    @Test
    void can_get_last_message_by_user_id() {
        Message expectedMessage = new Message(2, "message #2", getTestDateTime(), 1, 2);

        Message message = messageRepository.getLastMessageByUserId(3);
        message.setDateTime(getTestDateTime());

        assertEquals(expectedMessage, message);
    }

    @Test
    void can_add_message() {
        Message message = new Message(-1, "message #3", getTestDateTime(), 1, 2);

        int newMessageId = messageRepository.addMessage(message);

        message.setId(3);
        assertEquals(3, newMessageId);
        assertEquals(message, messageRepository.getLastMessageByUserId(3));
    }

    @Test
    void can_not_add_not_valid_message() {
        doThrow(new IllegalArgumentException()).when(validator).validate(any());

        assertThrows(
            IllegalArgumentException.class,
            () -> messageRepository.addMessage(null)
        );
    }

    private LocalDateTime getTestDateTime() {
        return LocalDateTime.of(2000, 1, 1, 0, 0);
    }
}
