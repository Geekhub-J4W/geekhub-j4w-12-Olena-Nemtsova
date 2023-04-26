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
import edu.geekhub.coursework.productsdishes.ProductDish;
import edu.geekhub.coursework.productsdishes.interfaces.ProductDishService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(ProductDishController.class)
@ContextConfiguration(classes = {SecurityConfig.class, MockConfig.class, TestApplication.class})
class ProductDishControllerTest {
    @Autowired
    protected ObjectMapper mapper;
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private ProductDishService productDishService;
    private ProductDish productDish;

    @BeforeEach
    void setUp() {
        productDish = new ProductDish(1, 1, 80);
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    void can_add_productDish_relation() throws Exception {
        doReturn(productDish).when(productDishService).addRelation(any());
        Gson gson = new Gson();
        String json = gson.toJson(productDish);

        mockMvc.perform(post("/productsDishes").accept(MediaType.APPLICATION_JSON)
                .content(json).contentType(MediaType.APPLICATION_JSON))
            .andExpect(content().json(mapper.writeValueAsString(productDish)))
            .andExpect(status().isOk())
            .andDo(print());

        verify(productDishService, times(1))
            .addRelation(productDish);
    }

    @Test
    @WithMockUser(authorities = "USER")
    void can_not_add_productDish_relation_by_user() throws Exception {
        Gson gson = new Gson();
        String json = gson.toJson(productDish);

        mockMvc.perform(post("/productsDishes").accept(MediaType.APPLICATION_JSON)
                .content(json).contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isForbidden())
            .andDo(print());

        verify(productDishService, times(0))
            .addRelation(any());
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    void can_update_productDish_relation() throws Exception {
        doReturn(productDish).when(productDishService).updateRelation(any());
        Gson gson = new Gson();
        String json = gson.toJson(productDish);

        mockMvc.perform(put("/productsDishes").accept(MediaType.APPLICATION_JSON)
                .content(json).contentType(MediaType.APPLICATION_JSON))
            .andExpect(content().json(mapper.writeValueAsString(productDish)))
            .andExpect(status().isOk())
            .andDo(print());

        verify(productDishService, times(1))
            .updateRelation(productDish);
    }

    @Test
    @WithMockUser(authorities = "USER")
    void can_not_update_productDish_relation_by_user() throws Exception {
        Gson gson = new Gson();
        String json = gson.toJson(productDish);

        mockMvc.perform(put("/productsDishes").accept(MediaType.APPLICATION_JSON)
                .content(json).contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isForbidden())
            .andDo(print());

        verify(productDishService, times(0))
            .updateRelation(any());
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    void can_delete_productDish_relation() throws Exception {
        doReturn(true)
            .when(productDishService).deleteRelationByProductAndDishId(anyInt(), anyInt());

        mockMvc.perform(delete("/productsDishes/{productId}/{dishId}", 1, 1))
            .andExpect(content().string(Boolean.TRUE.toString()))
            .andExpect(status().isOk())
            .andDo(print());

        verify(productDishService, times(1))
            .deleteRelationByProductAndDishId(1, 1);
    }

    @Test
    void can_get_productDish_relation() throws Exception {
        doReturn(productDish)
            .when(productDishService).getRelationByProductAndDishId(anyInt(), anyInt());

        mockMvc.perform(get("/productsDishes/{productId}/{dishId}", 1, 1))
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().json(mapper.writeValueAsString(productDish)))
            .andExpect(status().isOk())
            .andDo(print());

        verify(productDishService, times(1))
            .getRelationByProductAndDishId(1, 1);
    }
}
