package edu.geekhub.coursework;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.geekhub.coursework.chats.Chat;
import edu.geekhub.coursework.chats.intefaces.ChatService;
import edu.geekhub.coursework.config.SecurityConfig;
import edu.geekhub.coursework.config.WebSocketConfig;
import edu.geekhub.coursework.config.WebSocketSecurityConfig;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(ChatController.class)
@ContextConfiguration(classes = {SecurityConfig.class, WebSocketConfig.class,
    WebSocketSecurityConfig.class, MockConfig.class, TestApplication.class})
class ChatControllerTest {
    @Autowired
    protected ObjectMapper mapper;
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private ChatService chatService;
    private Chat chat;

    @BeforeEach
    void setUp() {
        chat = new Chat(1, 3, 2);
    }

    @Test
    @WithUserDetails("admin@gmail.com")
    void can_get_own_admin_chats_by_page_and_limit() throws Exception {
        doReturn(List.of(chat))
            .when(chatService).getChatsByPageAndAdminId(2, 1, 1);

        mockMvc.perform(get("/chats/{limit}/{pageNumber}/{adminId}", 1, 1, "own"))
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().json(mapper.writeValueAsString(List.of(chat))))
            .andExpect(status().isOk())
            .andDo(print());

        verify(chatService, times(1))
            .getChatsByPageAndAdminId(2, 1, 1);
    }

    @Test
    @WithUserDetails("admin@gmail.com")
    void can_get_other_chats_by_page_and_limit() throws Exception {
        doReturn(List.of(chat))
            .when(chatService).getChatsByPageAndAdminId(0, 1, 1);

        mockMvc.perform(get("/chats/{limit}/{pageNumber}/{adminId}", 1, 1, 0))
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().json(mapper.writeValueAsString(List.of(chat))))
            .andExpect(status().isOk())
            .andDo(print());

        verify(chatService, times(1))
            .getChatsByPageAndAdminId(0, 1, 1);
    }

    @Test
    @WithUserDetails("user@gmail.com")
    void can_not_get_admin_chats_by_page_and_limit_with_user() throws Exception {
        mockMvc.perform(get("/chats/{limit}/{pageNumber}/{adminId}", 1, 1, 0))
            .andExpect(status().isForbidden())
            .andDo(print());

        verify(chatService, times(0))
            .getChatsByPageAndAdminId(anyInt(), anyInt(), anyInt());
    }

    @Test
    @WithUserDetails("admin@gmail.com")
    void can_get_own_admin_chats_pages_count_by_limit() throws Exception {
        doReturn(1)
            .when(chatService).getCountOfPages(1, 2);

        mockMvc.perform(get("/chats/pages/{limit}/{adminId}", 1, "own"))
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().json(mapper.writeValueAsString(1)))
            .andExpect(status().isOk())
            .andDo(print());

        verify(chatService, times(1))
            .getCountOfPages(1, 2);
    }

    @Test
    @WithUserDetails("admin@gmail.com")
    void can_get_other_admin_chats_pages_count_by_limit() throws Exception {
        doReturn(1)
            .when(chatService).getCountOfPages(1, 0);

        mockMvc.perform(get("/chats/pages/{limit}/{adminId}", 1, 0))
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().json(mapper.writeValueAsString(1)))
            .andExpect(status().isOk())
            .andDo(print());

        verify(chatService, times(1))
            .getCountOfPages(1, 0);
    }

    @Test
    @WithUserDetails("user@gmail.com")
    void can_not_get_admin_chats_pages_count_by_limit_with_user() throws Exception {
        mockMvc.perform(get("/chats/pages/{limit}/{adminId}", 1, 0))
            .andExpect(status().isForbidden())
            .andDo(print());

        verify(chatService, times(0))
            .getCountOfPages(anyInt(), anyInt());
    }

}
