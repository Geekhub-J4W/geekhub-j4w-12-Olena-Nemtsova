package edu.geekhub.coursework;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.jose.shaded.gson.Gson;
import edu.geekhub.coursework.config.SecurityConfig;
import edu.geekhub.coursework.dishes.Dish;
import edu.geekhub.coursework.dishes.interfaces.DishService;
import edu.geekhub.coursework.util.TypeOfMeal;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMultipartHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@WebMvcTest(DishController.class)
@ContextConfiguration(classes = {SecurityConfig.class, MockConfig.class, TestApplication.class})
class DishControllerTest {
    @Autowired
    protected ObjectMapper mapper;
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private DishService dishService;
    private Dish dish;

    @BeforeEach
    void setUp() {
        dish = new Dish(1, "Borsch", null);
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    void can_add_dish() throws Exception {
        doReturn(dish).when(dishService).addDish(any());
        Gson gson = new Gson();
        String json = gson.toJson(dish);

        mockMvc.perform(post("/dishes").accept(MediaType.APPLICATION_JSON)
                .content(json).contentType(MediaType.APPLICATION_JSON))
            .andExpect(content().json(mapper.writeValueAsString(dish)))
            .andExpect(status().isOk())
            .andDo(print());

        verify(dishService, times(1))
            .addDish(dish);
    }

    @Test
    @WithMockUser(authorities = "USER")
    void can_not_add_dish_by_user() throws Exception {
        Gson gson = new Gson();
        String json = gson.toJson(dish);

        mockMvc.perform(post("/dishes").accept(MediaType.APPLICATION_JSON)
                .content(json).contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isForbidden())
            .andDo(print());

        verify(dishService, times(0))
            .addDish(dish);
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    void can_update_dish() throws Exception {
        doReturn(dish).when(dishService).updateDishById(any(), anyInt());
        Gson gson = new Gson();
        String json = gson.toJson(dish);

        mockMvc.perform(put("/dishes/{id}", 1).accept(MediaType.APPLICATION_JSON)
                .content(json).contentType(MediaType.APPLICATION_JSON))
            .andExpect(content().json(mapper.writeValueAsString(dish)))
            .andExpect(status().isOk())
            .andDo(print());

        verify(dishService, times(1))
            .updateDishById(dish, 1);
    }

    @Test
    @WithMockUser(authorities = "USER")
    void can_not_update_dish_by_user() throws Exception {
        Gson gson = new Gson();
        String json = gson.toJson(dish);

        mockMvc.perform(put("/dishes/{id}", 1).accept(MediaType.APPLICATION_JSON)
                .content(json).contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isForbidden())
            .andDo(print());

        verify(dishService, times(0))
            .updateDishById(dish, 1);
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    void can_update_dish_image() throws Exception {
        doReturn(dish).when(dishService).getDishById(anyInt());
        doReturn(dish).when(dishService).updateDishById(any(), anyInt());
        MockMultipartFile someImage =
            new MockMultipartFile("file", "image.png", "text/plain", "some xml".getBytes());

        MockMultipartHttpServletRequestBuilder builder =
            MockMvcRequestBuilders.multipart("/dishes/image/{id}", 1);
        builder.with(request -> {
            request.setMethod("PUT");
            return request;
        });

        mockMvc.perform(builder
                .file(someImage)
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(content().json(mapper.writeValueAsString(dish)))
            .andExpect(status().isOk())
            .andDo(print());

        verify(dishService, times(1))
            .updateDishById(dish, 1);
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    void can_delete_dish() throws Exception {
        doReturn(true).when(dishService).deleteDishById(anyInt());

        mockMvc.perform(delete("/dishes/{id}", 1))
            .andExpect(content().string(Boolean.TRUE.toString()))
            .andExpect(status().isOk())
            .andDo(print());

        verify(dishService, times(1))
            .deleteDishById(1);
    }

    @Test
    void can_get_dish_calories() throws Exception {
        doReturn(500).when(dishService).getDishCalories(anyInt());

        mockMvc.perform(get("/dishes/calories/{id}", 1))
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().json(mapper.writeValueAsString(500)))
            .andExpect(status().isOk())
            .andDo(print());

        verify(dishService, times(1))
            .getDishCalories(1);
    }

    @Test
    void can_get_dish_weight() throws Exception {
        doReturn(500).when(dishService).getDishWeight(anyInt());

        mockMvc.perform(get("/dishes/weight/{id}", 1))
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().json(mapper.writeValueAsString(500)))
            .andExpect(status().isOk())
            .andDo(print());

        verify(dishService, times(1))
            .getDishWeight(1);
    }

    @Test
    @WithUserDetails("user@gmail.com")
    void can_get_user_dishes_by_type_of_meal() throws Exception {
        doReturn(List.of(dish))
            .when(dishService).getDishesByUserIdAndTypeOfMeal(anyInt(), any());

        mockMvc.perform(get("/dishes/typeOfMeal/{typeOfMeal}", "DINNER"))
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().json(mapper.writeValueAsString(List.of(dish))))
            .andExpect(status().isOk())
            .andDo(print());

        verify(dishService, times(1))
            .getDishesByUserIdAndTypeOfMeal(1, TypeOfMeal.DINNER);
    }

    @Test
    void can_get_dish_by_id() throws Exception {
        doReturn(dish).when(dishService).getDishById(anyInt());

        mockMvc.perform(get("/dishes/{id}", 1))
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().json(mapper.writeValueAsString(dish)))
            .andExpect(status().isOk())
            .andDo(print());

        verify(dishService, times(1))
            .getDishById(1);
    }

    @Test
    void can_get_count_of_dishes_pages_by_input() throws Exception {
        doReturn(5)
            .when(dishService).getCountOfPages(anyInt(), any());

        mockMvc.perform(get("/dishes/pages/{limit}/{input}", 1, "borsch"))
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().json(mapper.writeValueAsString(5)))
            .andExpect(status().isOk())
            .andDo(print());

        verify(dishService, times(1))
            .getCountOfPages(1, "borsch");
    }

    @Test
    void can_get_dishes_of_page_by_input() throws Exception {
        doReturn(List.of(dish))
            .when(dishService).getDishesNameSortedByPageAndInput(anyInt(), anyInt(), any());

        mockMvc.perform(get("/dishes/{limit}/{pageNumber}/{input}", 1, 1, "borsch"))
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().json(mapper.writeValueAsString(List.of(dish))))
            .andExpect(status().isOk())
            .andDo(print());

        verify(dishService, times(1))
            .getDishesNameSortedByPageAndInput(1, 1, "borsch");
    }

    @Test
    void can_get_dishes_of_page_by_null_input_as_empty() throws Exception {
        doReturn(List.of(dish))
            .when(dishService).getDishesNameSortedByPageAndInput(anyInt(), anyInt(), any());

        mockMvc.perform(get("/dishes/{limit}/{pageNumber}/{input}", 1, 1, "null"))
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().json(mapper.writeValueAsString(List.of(dish))))
            .andExpect(status().isOk())
            .andDo(print());

        verify(dishService, times(1))
            .getDishesNameSortedByPageAndInput(1, 1, "");
    }
}
