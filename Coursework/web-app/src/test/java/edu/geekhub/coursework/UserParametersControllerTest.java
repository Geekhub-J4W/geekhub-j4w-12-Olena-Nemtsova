package edu.geekhub.coursework;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
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
import edu.geekhub.coursework.usersparameters.ActivityLevel;
import edu.geekhub.coursework.usersparameters.Aim;
import edu.geekhub.coursework.usersparameters.BodyType;
import edu.geekhub.coursework.usersparameters.Gender;
import edu.geekhub.coursework.usersparameters.UserParameters;
import edu.geekhub.coursework.usersparameters.interfaces.UserParametersService;
import edu.geekhub.coursework.util.TypeOfMeal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(UserParametersController.class)
@ContextConfiguration(classes = {SecurityConfig.class, MockConfig.class, TestApplication.class})
class UserParametersControllerTest {
    @Autowired
    protected ObjectMapper mapper;
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private UserParametersService userParametersService;
    private UserParameters userParameters;

    @BeforeEach
    void setUp() {
        userParameters = new UserParameters(1, 24, 70, 189,
            Gender.MALE, ActivityLevel.LOW, BodyType.ASTHENIC, Aim.NONE);
    }

    @Test
    @WithUserDetails("user@gmail.com")
    void can_add_user_parameters() throws Exception {
        doReturn(userParameters).when(userParametersService).addUserParameters(any());
        Gson gson = new Gson();
        String json = gson.toJson(userParameters);

        mockMvc.perform(post("/parameters").accept(MediaType.APPLICATION_JSON)
                .content(json).contentType(MediaType.APPLICATION_JSON)
                .with(csrf()))
            .andExpect(content().json(mapper.writeValueAsString(userParameters)))
            .andExpect(status().isOk())
            .andDo(print());

        verify(userParametersService, times(1))
            .addUserParameters(userParameters);
    }

    @Test
    @WithUserDetails("user@gmail.com")
    void can_not_add_user_parameters_without_csrf() throws Exception {
        Gson gson = new Gson();
        String json = gson.toJson(userParameters);

        mockMvc.perform(post("/parameters").accept(MediaType.APPLICATION_JSON)
                .content(json).contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isForbidden())
            .andDo(print());

        verify(userParametersService, times(0))
            .addUserParameters(any());
    }

    @Test
    void can_not_add_user_parameters_by_anonymous() throws Exception {
        Gson gson = new Gson();
        String json = gson.toJson(userParameters);

        mockMvc.perform(post("/parameters").accept(MediaType.APPLICATION_JSON)
                .content(json).contentType(MediaType.APPLICATION_JSON)
                .with(csrf()))
            .andExpect(redirectedUrl("http://localhost/login"))
            .andExpect(status().isFound())
            .andDo(print());

        verify(userParametersService, times(0))
            .addUserParameters(any());
    }

    @Test
    @WithUserDetails("user@gmail.com")
    void can_update_user_parameters() throws Exception {
        doReturn(userParameters).when(userParametersService).updateUserParameters(any());
        Gson gson = new Gson();
        String json = gson.toJson(userParameters);

        mockMvc.perform(put("/parameters").accept(MediaType.APPLICATION_JSON)
                .content(json).contentType(MediaType.APPLICATION_JSON)
                .with(csrf()))
            .andExpect(content().json(mapper.writeValueAsString(userParameters)))
            .andExpect(status().isOk())
            .andDo(print());

        verify(userParametersService, times(1))
            .updateUserParameters(userParameters);
    }

    @Test
    @WithUserDetails("user@gmail.com")
    void can_not_update_user_parameters_without_csrf() throws Exception {
        Gson gson = new Gson();
        String json = gson.toJson(userParameters);

        mockMvc.perform(put("/parameters").accept(MediaType.APPLICATION_JSON)
                .content(json).contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isForbidden())
            .andDo(print());

        verify(userParametersService, times(0))
            .updateUserParameters(any());
    }

    @Test
    void can_not_update_user_parameters_by_anonymous() throws Exception {
        Gson gson = new Gson();
        String json = gson.toJson(userParameters);

        mockMvc.perform(put("/parameters").accept(MediaType.APPLICATION_JSON)
                .content(json).contentType(MediaType.APPLICATION_JSON)
                .with(csrf()))
            .andExpect(redirectedUrl("http://localhost/login"))
            .andExpect(status().isFound())
            .andDo(print());

        verify(userParametersService, times(0))
            .updateUserParameters(any());
    }

    @Test
    @WithUserDetails("user@gmail.com")
    void can_get_user_parameters() throws Exception {
        doReturn(userParameters).when(userParametersService).getUserParametersByUserId(anyInt());

        mockMvc.perform(get("/parameters"))
            .andExpect(content().json(mapper.writeValueAsString(userParameters)))
            .andExpect(status().isOk())
            .andDo(print());

        verify(userParametersService, times(1))
            .getUserParametersByUserId(1);
    }

    @Test
    @WithUserDetails("user@gmail.com")
    void can_get_user_calories_for_day() throws Exception {
        doReturn(2000).when(userParametersService).getUserCaloriesForDay(anyInt());

        mockMvc.perform(get("/parameters/calories"))
            .andExpect(content().json(mapper.writeValueAsString(2000)))
            .andExpect(status().isOk())
            .andDo(print());

        verify(userParametersService, times(1))
            .getUserCaloriesForDay(1);
    }

    @Test
    @WithUserDetails("user@gmail.com")
    void can_get_user_calories_by_typeOfMeal() throws Exception {
        doReturn(500)
            .when(userParametersService).getUserCaloriesByTypeOfMeal(anyInt(), any());

        mockMvc.perform(get("/parameters/calories/{typeOfMeal}", "DINNER"))
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().json(mapper.writeValueAsString(500)))
            .andExpect(status().isOk())
            .andDo(print());

        verify(userParametersService, times(1))
            .getUserCaloriesByTypeOfMeal(1, TypeOfMeal.DINNER);
    }
}
