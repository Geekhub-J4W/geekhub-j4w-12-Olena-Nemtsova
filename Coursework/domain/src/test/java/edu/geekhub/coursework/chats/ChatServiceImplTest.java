package edu.geekhub.coursework.chats;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;

import edu.geekhub.coursework.chats.intefaces.ChatRepository;
import edu.geekhub.coursework.util.PageValidator;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataAccessException;

@ExtendWith(MockitoExtension.class)
class ChatServiceImplTest {
    private ChatServiceImpl chatService;
    @Mock
    private ChatValidator validator;
    @Mock
    private ChatRepository repository;
    @Mock
    private PageValidator pageValidator;
    private Chat chat;

    @BeforeEach
    void setUp() {
        chatService = new ChatServiceImpl(validator, pageValidator, repository);

        chat = new Chat(1, 2, 1);
    }

    @Test
    void can_get_chat_by_id() {
        doReturn(chat).when(repository).getChatById(anyInt());

        Chat gotChat = chatService.getChatById(1);

        assertEquals(chat, gotChat);
    }

    @Test
    void can_get_chat_by_user_id() {
        doReturn(chat).when(repository).getChatByUserId(anyInt());

        Chat gotChat = chatService.getChatByUserId(1);

        assertEquals(chat, gotChat);
    }

    @Test
    void can_add_chat() {
        doNothing().when(validator).validate(any());
        doReturn(1).when(repository).addChat(any());

        Chat addedChat = chatService.addChat(chat);

        assertEquals(chat, addedChat);
    }

    @Test
    void can_not_add_not_valid_chat() {
        doThrow(new IllegalArgumentException()).when(validator).validate(any());

        Chat addedNotValidChat = chatService.addChat(chat);
        Chat addedNullChat = chatService.addChat(null);

        assertNull(addedNotValidChat);
        assertNull(addedNullChat);
    }

    @Test
    void can_not_add_chat_not_retrieved_generated_key_from_repository() {
        doNothing().when(validator).validate(any());
        doReturn(-1).when(repository).addChat(any());

        Chat addedChat = chatService.addChat(chat);

        assertNull(addedChat);
    }

    @Test
    void can_not_add_chat_not_added_at_repository() {
        doNothing().when(validator).validate(any());
        doThrow(new DataAccessException("") {
        }).when(repository).addChat(any());

        Chat addedChat = chatService.addChat(chat);

        assertNull(addedChat);
    }

    @Test
    void can_update_chat_by_id() {
        doNothing().when(validator).validate(any());
        doReturn(chat).when(repository).getChatById(anyInt());
        doNothing().when(repository).updateChatAdminById(any(), anyInt());

        Chat updatedChat = chatService.updateChat(chat, 1);

        assertEquals(chat, updatedChat);
    }

    @Test
    void can_not_update_chat_to_not_valid_product() {
        doReturn(chat).when(repository).getChatById(anyInt());
        doThrow(new IllegalArgumentException()).when(validator).validate(any());

        Chat updatedChat = chatService.updateChat(chat, 1);

        assertNull(updatedChat);
    }

    @Test
    void can_not_update_chat_by_not_existing_id() {
        doReturn(null).when(repository).getChatById(anyInt());

        Chat updatedChat = chatService.updateChat(chat, 1);

        assertNull(updatedChat);
    }

    @Test
    void can_not_update_chat_by_id_not_updated_at_repository() {
        doNothing().when(validator).validate(any());
        doReturn(chat).when(repository).getChatById(anyInt());
        doThrow(new DataAccessException("") {
        })
            .when(repository).updateChatAdminById(any(), anyInt());

        Chat updatedChat = chatService.updateChat(chat, 1);

        assertNull(updatedChat);
    }

    @Test
    void can_get_count_of_chats_pages_by_limit_and_admin_id() {
        doNothing().when(pageValidator).validatePageLimit(anyInt());
        doReturn(List.of(chat, chat)).when(repository).getChats();

        int countOfPages = chatService.getCountOfPages(1, 1);

        assertEquals(2, countOfPages);
    }

    @Test
    void can_get_count_of_all_chats_pages_by_admin_id_equals_minus_one() {
        doNothing().when(pageValidator).validatePageLimit(anyInt());
        doReturn(List.of(chat, chat)).when(repository).getChats();

        int countOfPages = chatService.getCountOfPages(1, -1);

        assertEquals(2, countOfPages);
    }

    @Test
    void can_get_one_chats_page_by_wrong_limit() {
        doThrow(new IllegalArgumentException()).when(pageValidator).validatePageLimit(anyInt());

        int countOfPages = chatService.getCountOfPages(0, 1);

        assertEquals(1, countOfPages);
    }

    @Test
    void can_get_one_chats_page_by_wrong_admin_id() {
        doNothing().when(pageValidator).validatePageLimit(anyInt());
        doReturn(List.of(chat, chat)).when(repository).getChats();

        int countOfPages = chatService.getCountOfPages(1, -10);

        assertEquals(1, countOfPages);
    }

    @Test
    void can_get_chats_by_page_and_admin_id() {
        doNothing().when(pageValidator).validatePageLimit(anyInt());
        doNothing().when(pageValidator).validatePageNumber(anyInt(), anyInt());
        doReturn(List.of(chat))
            .when(repository).getChatsByPageAndAdminId(anyInt(), anyInt(), anyInt());

        List<Chat> chats = chatService.getChatsByPageAndAdminId(1, 1, 1);

        assertEquals(List.of(chat), chats);
    }

    @Test
    void can_get_empty_chats_by_page_and_wrong_limit() {
        doThrow(new IllegalArgumentException()).when(pageValidator).validatePageLimit(anyInt());

        List<Chat> chats = chatService.getChatsByPageAndAdminId(1, -1, 1);

        assertEquals(new ArrayList<>(), chats);
    }

    @Test
    void can_get_empty_chats_by_limit_and_wrong_page() {
        doNothing().when(pageValidator).validatePageLimit(anyInt());
        doThrow(new IllegalArgumentException())
            .when(pageValidator).validatePageNumber(anyInt(), anyInt());

        List<Chat> chats = chatService.getChatsByPageAndAdminId(1, 1, -1);

        assertEquals(new ArrayList<>(), chats);
    }
}
