package edu.geekhub.coursework;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.jose.shaded.gson.Gson;
import edu.geekhub.coursework.allergics.UserAllergicProduct;
import edu.geekhub.coursework.allergics.interfaces.UserAllergicProductService;
import edu.geekhub.coursework.config.SecurityConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(UserAllergicProductController.class)
@ContextConfiguration(classes = {SecurityConfig.class, MockConfig.class, TestApplication.class})
class UserAllergicProductControllerTest {
    @Autowired
    protected ObjectMapper mapper;
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private UserAllergicProductService userAllergicProductService;

    @Test
    @WithUserDetails("user@gmail.com")
    void can_add_userAllergicProduct_relation() throws Exception {
        UserAllergicProduct relation = new UserAllergicProduct(1, 1);
        doReturn(relation).when(userAllergicProductService).addRelation(any());
        Gson gson = new Gson();
        String json = gson.toJson(relation);

        mockMvc.perform(post("/allergic").accept(MediaType.APPLICATION_JSON)
                .content(json).contentType(MediaType.APPLICATION_JSON)
                .with(csrf()))
            .andExpect(content().json(mapper.writeValueAsString(relation)))
            .andExpect(status().isOk())
            .andDo(print());

        verify(userAllergicProductService, times(1))
            .addRelation(relation);
    }

    @Test
    @WithUserDetails("user@gmail.com")
    void can_not_add_userAllergicProduct_relation_without_csrf() throws Exception {
        UserAllergicProduct relation = new UserAllergicProduct(1, 1);
        Gson gson = new Gson();
        String json = gson.toJson(relation);

        mockMvc.perform(post("/allergic").accept(MediaType.APPLICATION_JSON)
                .content(json).contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isForbidden())
            .andDo(print());

        verify(userAllergicProductService, times(0))
            .addRelation(any());
    }

    @Test
    void can_not_add_userAllergicProduct_relation_by_anonymous() throws Exception {
        UserAllergicProduct relation = new UserAllergicProduct(1, 1);
        Gson gson = new Gson();
        String json = gson.toJson(relation);

        mockMvc.perform(post("/allergic").accept(MediaType.APPLICATION_JSON)
                .content(json).contentType(MediaType.APPLICATION_JSON)
                .with(csrf()))
            .andExpect(redirectedUrl("http://localhost/login"))
            .andExpect(status().isFound())
            .andDo(print());

        verify(userAllergicProductService, times(0))
            .addRelation(any());
    }

    @Test
    @WithUserDetails("user@gmail.com")
    void can_delete_userAllergicProduct_relation() throws Exception {
        doReturn(true)
            .when(userAllergicProductService)
            .deleteRelationByUserAndProductId(anyInt(), anyInt());

        mockMvc.perform(delete("/allergic/{productId}", 1)
                .with(csrf()))
            .andExpect(content().string(Boolean.TRUE.toString()))
            .andExpect(status().isOk())
            .andDo(print());

        verify(userAllergicProductService, times(1))
            .deleteRelationByUserAndProductId(1, 1);
    }

    @Test
    @WithUserDetails("user@gmail.com")
    void can_not_delete_userAllergicProduct_relation_without_csrf() throws Exception {
        mockMvc.perform(delete("/allergic/{productId}", 1))
            .andExpect(status().isForbidden())
            .andDo(print());

        verify(userAllergicProductService, times(0))
            .deleteRelationByUserAndProductId(anyInt(), anyInt());
    }

    @Test
    void can_not_delete_userAllergicProduct_relation_by_anonymous() throws Exception {
        mockMvc.perform(delete("/allergic/{productId}", 1)
                .with(csrf()))
            .andExpect(redirectedUrl("http://localhost/login"))
            .andExpect(status().isFound())
            .andDo(print());

        verify(userAllergicProductService, times(0))
            .deleteRelationByUserAndProductId(anyInt(), anyInt());
    }
}
