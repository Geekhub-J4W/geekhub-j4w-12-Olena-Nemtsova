package edu.geekhub.coursework;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.oauth2Login;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.jose.shaded.gson.Gson;
import edu.geekhub.coursework.config.SecurityConfig;
import edu.geekhub.coursework.users.Role;
import edu.geekhub.coursework.users.User;
import edu.geekhub.coursework.users.interfaces.UserService;
import java.util.HashMap;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(UserController.class)
@ContextConfiguration(classes = {SecurityConfig.class, MockConfig.class, TestApplication.class})
class UserControllerTest {
    @Autowired
    protected ObjectMapper mapper;
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private UserService userService;
    private User user;

    @BeforeEach
    void setUp() {
        user = new User(1, "Mark", "Pearce", "Qwerty1", "some@gmail.com", Role.USER);
    }

    @Test
    @WithUserDetails("user@gmail.com")
    void can_get_current_user() throws Exception {
        doReturn(user).when(userService).getUserById(anyInt());

        mockMvc.perform(get("/users"))
            .andExpect(content().json(mapper.writeValueAsString(user)))
            .andExpect(status().isOk())
            .andDo(print());

        verify(userService, times(1))
            .getUserById(1);
    }

    @Test
    void can_not_get_current_user_by_anonymous() throws Exception {
        mockMvc.perform(get("/users"))
            .andExpect(redirectedUrl("http://localhost/login"))
            .andExpect(status().isFound())
            .andDo(print());

        verify(userService, times(0))
            .getUserById(anyInt());
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    void can_get_user_by_id() throws Exception {
        doReturn(user).when(userService).getUserById(anyInt());

        mockMvc.perform(get("/users/{id}", 1))
            .andExpect(content().json(mapper.writeValueAsString(user)))
            .andExpect(status().isOk())
            .andDo(print());

        verify(userService, times(1))
            .getUserById(1);
    }

    @Test
    @WithMockUser(authorities = "USER")
    void can_not_get_user_by_id_by_user() throws Exception {
        mockMvc.perform(get("/users/{id}", 1))
            .andExpect(status().isForbidden())
            .andDo(print());

        verify(userService, times(0))
            .getUserById(anyInt());
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    void can_get_count_of_users_pages_by_role_and_input() throws Exception {
        doReturn(5)
            .when(userService).getCountOfPages(any(), anyInt(), any());

        mockMvc.perform(get("/users/pages/{role}/{limit}/{input}", "USER", 1, "mark"))
            .andExpect(content().json(mapper.writeValueAsString(5)))
            .andExpect(status().isOk())
            .andDo(print());

        verify(userService, times(1))
            .getCountOfPages(Role.USER, 1, "mark");
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    void can_get_users_of_page_by_role_and_input() throws Exception {
        doReturn(List.of(user))
            .when(userService).getUsersOfRoleByPageAndInput(any(), anyInt(), anyInt(), any());

        mockMvc.perform(get("/users/{role}/{limit}/{pageNumber}/{input}", "USER", 1, 1, "mark"))
            .andExpect(content().json(mapper.writeValueAsString(List.of(user))))
            .andExpect(status().isOk())
            .andDo(print());

        verify(userService, times(1))
            .getUsersOfRoleByPageAndInput(Role.USER, 1, 1, "mark");
    }

    @Test
    @WithUserDetails("admin@gmail.com")
    void can_add_user() throws Exception {
        doReturn(user).when(userService).addUser(any());
        Gson gson = new Gson();
        String json = gson.toJson(user);

        mockMvc.perform(post("/users").accept(MediaType.APPLICATION_JSON)
                .content(json).contentType(MediaType.APPLICATION_JSON))
            .andExpect(content().json(mapper.writeValueAsString(user)))
            .andExpect(status().isOk())
            .andDo(print());

        verify(userService, times(1))
            .addUser(user);
    }

    @Test
    @WithUserDetails("admin@gmail.com")
    void can_not_add_user_with_same_role_to_current() throws Exception {
        user.setRole(Role.ADMIN);
        doReturn(user).when(userService).getUserById(anyInt());

        Gson gson = new Gson();
        String json = gson.toJson(user);

        mockMvc.perform(post("/users").accept(MediaType.APPLICATION_JSON)
                .content(json).contentType(MediaType.APPLICATION_JSON))
            .andExpect(content().string(""))
            .andExpect(status().isOk())
            .andDo(print());

        verify(userService, times(0))
            .addUser(user);
    }

    @Test
    void can_register_user() throws Exception {
        doReturn(user).when(userService).addUser(any());
        Gson gson = new Gson();
        String json = gson.toJson(user);

        mockMvc.perform(post("/users/register").accept(MediaType.APPLICATION_JSON)
                .content(json).contentType(MediaType.APPLICATION_JSON))
            .andExpect(content().json(mapper.writeValueAsString(user)))
            .andExpect(status().isOk())
            .andDo(print());

        verify(userService, times(1))
            .addUser(user);
    }

    @Test
    void can_register_user_with_google() throws Exception {
        doReturn(user).when(userService).addUser(any());

        OAuth2User oauth2User = new DefaultOAuth2User(
            AuthorityUtils.createAuthorityList(Role.USER.getAuthority()),
            new HashMap<>() {
                {
                    put("email", "oAuth2User@gmail.com");
                    put("family_name", "Pearce");
                    put("given_name", "Mark");
                }
            },
            "email");

        mockMvc.perform(post("/users/google")
                .with(oauth2Login().oauth2User(oauth2User)).accept(MediaType.APPLICATION_JSON))
            .andExpect(content().json(mapper.writeValueAsString(user)))
            .andExpect(status().isOk())
            .andDo(print());

        verify(userService, times(1))
            .addUser(any());
    }

    @Test
    @WithUserDetails("admin@gmail.com")
    void can_delete_user_by_id() throws Exception {
        User admin = new User(user);
        admin.setRole(Role.ADMIN);
        doReturn(user, admin).when(userService).getUserById(anyInt());
        doReturn(true).when(userService).deleteUserById(anyInt());

        mockMvc.perform(delete("/users/{id}", 1))
            .andExpect(content().string(Boolean.TRUE.toString()))
            .andExpect(status().isOk())
            .andDo(print());

        verify(userService, times(1))
            .deleteUserById(1);
    }

    @Test
    @WithUserDetails("admin@gmail.com")
    void can_not_delete_user_by_id_same_with_role_to_current() throws Exception {
        doReturn(user).when(userService).getUserById(anyInt());

        mockMvc.perform(delete("/users/{id}", 1).accept(MediaType.APPLICATION_JSON))
            .andExpect(content().string(Boolean.FALSE.toString()))
            .andExpect(status().isOk())
            .andDo(print());

        verify(userService, times(0))
            .deleteUserById(anyInt());
    }

    @Test
    @WithUserDetails("admin@gmail.com")
    void can_update_user_by_id() throws Exception {
        User admin = new User(user);
        admin.setRole(Role.ADMIN);
        doReturn(user, admin, admin).when(userService).getUserById(anyInt());
        doReturn(user).when(userService).updateUserById(any(), anyInt());

        Gson gson = new Gson();
        String json = gson.toJson(user);

        mockMvc.perform(put("/users/{id}", 1).accept(MediaType.APPLICATION_JSON)
                .content(json).contentType(MediaType.APPLICATION_JSON))
            .andExpect(content().json(mapper.writeValueAsString(user)))
            .andExpect(status().isOk())
            .andDo(print());

        verify(userService, times(1))
            .updateUserById(user, 1);
    }

    @Test
    @WithUserDetails("admin@gmail.com")
    void can_not_update_user_by_id_with_same_role_to_current() throws Exception {
        doReturn(user).when(userService).getUserById(anyInt());

        Gson gson = new Gson();
        String json = gson.toJson(user);

        mockMvc.perform(put("/users/{id}", 1).accept(MediaType.APPLICATION_JSON)
                .content(json).contentType(MediaType.APPLICATION_JSON))
            .andExpect(content().string(""))
            .andExpect(status().isOk())
            .andDo(print());

        verify(userService, times(0))
            .updateUserById(any(), anyInt());
    }

    @Test
    @WithUserDetails("user@gmail.com")
    void can_update_current_user() throws Exception {
        doReturn(user).when(userService).getUserById(anyInt());
        doReturn(user).when(userService).updateUserById(any(), anyInt());

        Gson gson = new Gson();
        String json = gson.toJson(user);

        mockMvc.perform(put("/users").accept(MediaType.APPLICATION_JSON)
                .content(json).contentType(MediaType.APPLICATION_JSON))
            .andExpect(content().json(mapper.writeValueAsString(user)))
            .andExpect(status().isOk())
            .andDo(print());

        verify(userService, times(1))
            .updateUserById(user, 1);
    }
}
