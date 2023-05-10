package edu.geekhub.coursework.chats;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;

import edu.geekhub.coursework.TestApplication;
import edu.geekhub.coursework.config.DatabaseContainer;
import edu.geekhub.coursework.config.DatabaseTestConfig;
import java.util.List;
import org.junit.ClassRule;
import org.junit.jupiter.api.Assertions;
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
class ChatRepositoryImplTest {
    @ClassRule
    @Container
    public static PostgreSQLContainer<DatabaseContainer> postgresSQLContainer =
        DatabaseContainer.getInstance();
    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;
    @Mock
    private ChatValidator validator;
    private ChatRepositoryImpl chatRepository;

    @BeforeEach
    void setUp() {
        chatRepository = new ChatRepositoryImpl(validator, jdbcTemplate);
    }

    @Test
    void can_get_chat_by_id() {
        Chat expectedChat = new Chat(1, 3, 2);

        Chat chat = chatRepository.getChatById(1);

        assertEquals(expectedChat, chat);
    }

    @Test
    void can_get_null_chat_by_wrong_id() {
        Chat chat = chatRepository.getChatById(-1);

        assertNull(chat);
    }

    @Test
    void can_get_chat_by_user_id() {
        Chat expectedChat = new Chat(1, 3, 2);

        Chat chat = chatRepository.getChatByUserId(3);

        assertEquals(expectedChat, chat);
    }

    @Test
    void can_get_null_chat_by_wrong_user_id() {
        Chat chat = chatRepository.getChatByUserId(-1);

        assertNull(chat);
    }


    @Test
    void can_add_chat() {
        Chat chat = new Chat(-1, 1, 0);

        int newChatId = chatRepository.addChat(chat);

        chat.setId(3);
        assertEquals(chat, chatRepository.getChatById(newChatId));
    }

    @Test
    void can_not_add_not_valid_chat() {
        doThrow(new IllegalArgumentException()).when(validator).validate(any());

        assertThrows(
            IllegalArgumentException.class,
            () -> chatRepository.addChat(null)
        );
    }

    @Test
    void can_update_chat_by_id() {
        Chat expectedChat = new Chat(1, 3, 1);

        chatRepository.updateChatAdminById(expectedChat, 1);

        assertEquals(expectedChat, chatRepository.getChatById(1));
    }

    @Test
    void can_not_update_chat_by_id_to_not_valid() {
        doThrow(new IllegalArgumentException()).when(validator).validate(any());

        assertThrows(
            IllegalArgumentException.class,
            () -> chatRepository.updateChatAdminById(null, 1)
        );
    }

    @Test
    void can_get_chats() {
        List<Chat> expectedChats = List.of(
            new Chat(1, 3, 2),
            new Chat(2, 2, 0)
        );

        List<Chat> chats = chatRepository.getChats();

        Assertions.assertEquals(expectedChats, chats);
    }

    @Test
    void can_get_chats_of_admin_by_page_and_admin_id() {
        List<Chat> expectedChats = List.of(
            new Chat(1, 3, 2)
        );

        List<Chat> chats = chatRepository.getChatsByPageAndAdminId(2, 1, 1);

        assertEquals(expectedChats, chats);
    }

    @Test
    void can_get_tied_chats_by_page_and_admin_id_equals_zero() {
        List<Chat> expectedChats = List.of(
            new Chat(2, 2, 0)
        );

        List<Chat> chats = chatRepository.getChatsByPageAndAdminId(0, 1, 1);

        assertEquals(expectedChats, chats);
    }

    @Test
    void can_get_all_chats_by_page_and_admin_id_equals_minus_zero() {
        List<Chat> expectedChats = List.of(
            new Chat(2, 2, 0),
            new Chat(1, 3, 2)
        );

        List<Chat> chats = chatRepository.getChatsByPageAndAdminId(-1, 2, 1);

        assertEquals(expectedChats, chats);
    }
}
