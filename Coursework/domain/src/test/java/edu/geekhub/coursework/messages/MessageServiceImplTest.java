package edu.geekhub.coursework.messages;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;

import edu.geekhub.coursework.chats.Chat;
import edu.geekhub.coursework.chats.intefaces.ChatService;
import edu.geekhub.coursework.messages.interfaces.MessageRepository;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataAccessException;

@ExtendWith(MockitoExtension.class)
class MessageServiceImplTest {
    private MessageServiceImpl messageService;
    @Mock
    private MessageValidator validator;
    @Mock
    private MessageRepository repository;
    @Mock
    private ChatService chatService;
    private Message message;

    @BeforeEach
    void setUp() {
        messageService = new MessageServiceImpl(validator, repository, chatService);

        message = new Message(1, "text", LocalDateTime.now(), 1, 1);
    }

    @Test
    void can_get_messages_by_user_id() {
        doReturn(List.of(message)).when(repository).getMessagesByUserId(anyInt());

        List<Message> messages = messageService.getMessagesByUserId(1);

        assertEquals(List.of(message), messages);
    }

    @Test
    void can_get_last_message_by_user_id() {
        doReturn(message).when(repository).getLastMessageByUserId(anyInt());

        Message gotMessage = messageService.getLastMessageByUserId(1);

        assertEquals(message, gotMessage);
    }

    @Test
    void can_add_message() {
        doReturn(new Chat()).when(chatService).getChatById(anyInt());
        doNothing().when(validator).validate(any());
        doReturn(1).when(repository).addMessage(any());

        Message addedMessage = messageService.addMessage(message, 1);

        assertEquals(message, addedMessage);
    }

    @Test
    void can_add_message_with_creating_new_chat() {
        Chat chat = new Chat(1, 1, 1);

        doReturn(null).when(chatService).getChatById(anyInt());
        doReturn(null).when(chatService).getChatByUserId(anyInt());
        doNothing().when(validator).validate(any());
        doReturn(chat).when(chatService).addChat(any());
        doReturn(1).when(repository).addMessage(any());

        Message addedMessage = messageService.addMessage(message, 1);

        assertEquals(message, addedMessage);
    }

    @Test
    void can_not_add_message_with_failed_creating_new_chat() {
        doReturn(null).when(chatService).getChatById(anyInt());
        doReturn(null).when(chatService).getChatByUserId(anyInt());
        doReturn(null).when(chatService).addChat(any());

        Message addedMessage = messageService.addMessage(message, 1);

        assertNull(addedMessage);
    }

    @Test
    void can_not_add_not_valid_message() {
        doReturn(new Chat()).when(chatService).getChatById(anyInt());
        doThrow(new IllegalArgumentException()).when(validator).validate(any());

        Message addedMessage = messageService.addMessage(message, 1);

        assertNull(addedMessage);
    }

    @Test
    void can_not_add_message_not_retrieved_generated_key_from_repository() {
        doReturn(new Chat()).when(chatService).getChatById(anyInt());
        doNothing().when(validator).validate(any());
        doReturn(-1).when(repository).addMessage(any());

        Message addedMessage = messageService.addMessage(message, 1);

        assertNull(addedMessage);
    }

    @Test
    void can_not_add_message_not_added_at_repository() {
        doReturn(new Chat()).when(chatService).getChatById(anyInt());
        doNothing().when(validator).validate(any());
        doThrow(new DataAccessException("") {
        }).when(repository).addMessage(any());

        Message addedMessage = messageService.addMessage(message, 1);

        assertNull(addedMessage);
    }
}
