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
import edu.geekhub.coursework.config.SecurityConfig;
import edu.geekhub.coursework.config.WebSocketConfig;
import edu.geekhub.coursework.messages.Message;
import edu.geekhub.coursework.messages.interfaces.MessageService;
import java.time.LocalDateTime;
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

@WebMvcTest(MessageController.class)
@ContextConfiguration(classes = {SecurityConfig.class, WebSocketConfig.class,
    MockConfig.class, TestApplication.class})
class MessageControllerTest {
    @Autowired
    protected ObjectMapper mapper;
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private MessageService messageService;

    private Message message;

    @BeforeEach
    void setUp() {
        message = new Message(1, "text", LocalDateTime.now(), 1, 1);
    }

    @Test
    @WithUserDetails("user@gmail.com")
    void can_get_user_chat_messages() throws Exception {
        doReturn(List.of(message))
            .when(messageService).getMessagesByUserId(anyInt());

        mockMvc.perform(get("/messages"))
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().json(mapper.writeValueAsString(List.of(message))))
            .andExpect(status().isOk())
            .andDo(print());

        verify(messageService, times(1))
            .getMessagesByUserId(1);
    }

    @Test
    @WithUserDetails("admin@gmail.com")
    void can_get_chat_messages_by_user_id() throws Exception {
        doReturn(List.of(message))
            .when(messageService).getMessagesByUserId(anyInt());

        mockMvc.perform(get("/messages/{userId}", 1))
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().json(mapper.writeValueAsString(List.of(message))))
            .andExpect(status().isOk())
            .andDo(print());

        verify(messageService, times(1))
            .getMessagesByUserId(1);
    }

    @Test
    @WithUserDetails("user@gmail.com")
    void can_not_get_chat_messages_with_user_by_user_id() throws Exception {
        mockMvc.perform(get("/messages/{userId}", 1))
            .andExpect(status().isForbidden())
            .andDo(print());

        verify(messageService, times(0))
            .getMessagesByUserId(anyInt());
    }

    @Test
    @WithUserDetails("admin@gmail.com")
    void can_get_last_message_by_user_id() throws Exception {
        doReturn(message)
            .when(messageService).getLastMessageByUserId(anyInt());

        mockMvc.perform(get("/messages/last/{userId}", 1))
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().json(mapper.writeValueAsString(message)))
            .andExpect(status().isOk())
            .andDo(print());

        verify(messageService, times(1))
            .getLastMessageByUserId(1);
    }

    @Test
    @WithUserDetails("user@gmail.com")
    void can_not_get_last_message_with_user_by_user_id() throws Exception {
        mockMvc.perform(get("/messages/last/{userId}", 1))
            .andExpect(status().isForbidden())
            .andDo(print());

        verify(messageService, times(0))
            .getLastMessageByUserId(anyInt());
    }
}
