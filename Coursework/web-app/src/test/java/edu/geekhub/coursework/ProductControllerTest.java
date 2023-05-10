package edu.geekhub.coursework;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
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
import edu.geekhub.coursework.products.Product;
import edu.geekhub.coursework.products.interfaces.ProductService;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(ProductController.class)
@ContextConfiguration(classes = {SecurityConfig.class, MockConfig.class, TestApplication.class})
class ProductControllerTest {
    @Autowired
    protected ObjectMapper mapper;
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private ProductService productService;
    private Product product;

    @BeforeEach
    void setUp() {
        product = new Product(1, "Rice", 130);
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    void can_add_product() throws Exception {
        doReturn(product).when(productService).addProduct(any());
        Gson gson = new Gson();
        String json = gson.toJson(product);

        mockMvc.perform(post("/products").accept(MediaType.APPLICATION_JSON)
                .content(json).contentType(MediaType.APPLICATION_JSON)
                .with(csrf()))
            .andExpect(content().json(mapper.writeValueAsString(product)))
            .andExpect(status().isOk())
            .andDo(print());

        verify(productService, times(1))
            .addProduct(product);
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    void can_not_add_product_without_csrf() throws Exception {
        Gson gson = new Gson();
        String json = gson.toJson(product);

        mockMvc.perform(post("/products").accept(MediaType.APPLICATION_JSON)
                .content(json).contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isForbidden())
            .andDo(print());

        verify(productService, times(0))
            .addProduct(any());
    }

    @Test
    @WithMockUser(authorities = "USER")
    void can_not_add_product_by_user() throws Exception {
        Gson gson = new Gson();
        String json = gson.toJson(product);

        mockMvc.perform(post("/products").accept(MediaType.APPLICATION_JSON)
                .content(json).contentType(MediaType.APPLICATION_JSON)
                .with(csrf()))
            .andExpect(status().isForbidden())
            .andDo(print());

        verify(productService, times(0))
            .addProduct(product);
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    void can_update_product() throws Exception {
        doReturn(product).when(productService).updateProductById(any(), anyInt());
        Gson gson = new Gson();
        String json = gson.toJson(product);

        mockMvc.perform(put("/products/{id}", 1).accept(MediaType.APPLICATION_JSON)
                .content(json).contentType(MediaType.APPLICATION_JSON)
                .with(csrf()))
            .andExpect(content().json(mapper.writeValueAsString(product)))
            .andExpect(status().isOk())
            .andDo(print());

        verify(productService, times(1))
            .updateProductById(product, 1);
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    void can_not_update_product_without_csrf() throws Exception {
        Gson gson = new Gson();
        String json = gson.toJson(product);

        mockMvc.perform(put("/products/{id}", 1).accept(MediaType.APPLICATION_JSON)
                .content(json).contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isForbidden())
            .andDo(print());

        verify(productService, times(0))
            .updateProductById(any(), anyInt());
    }

    @Test
    @WithMockUser(authorities = "USER")
    void can_not_update_product_by_user() throws Exception {
        Gson gson = new Gson();
        String json = gson.toJson(product);

        mockMvc.perform(put("/products/{id}", 1).accept(MediaType.APPLICATION_JSON)
                .content(json).contentType(MediaType.APPLICATION_JSON)
                .with(csrf()))
            .andExpect(status().isForbidden())
            .andDo(print());

        verify(productService, times(0))
            .updateProductById(product, 1);
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    void can_delete_product() throws Exception {
        doReturn(true).when(productService).deleteProductById(anyInt());

        mockMvc.perform(delete("/products/{id}", 1)
                .with(csrf()))
            .andExpect(content().string(Boolean.TRUE.toString()))
            .andExpect(status().isOk())
            .andDo(print());

        verify(productService, times(1))
            .deleteProductById(1);
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    void can_get_product_by_id() throws Exception {
        doReturn(product).when(productService).getProductById(anyInt());

        mockMvc.perform(get("/products/{id}", 1))
            .andExpect(content().json(mapper.writeValueAsString(product)))
            .andExpect(status().isOk())
            .andDo(print());

        verify(productService, times(1))
            .getProductById(1);
    }

    @Test
    void can_get_count_of_products_pages_by_input() throws Exception {
        doReturn(5)
            .when(productService).getCountOfPages(anyInt(), any());

        mockMvc.perform(get("/products/pages/{limit}/{input}", 1, "rice"))
            .andExpect(content().json(mapper.writeValueAsString(5)))
            .andExpect(status().isOk())
            .andDo(print());

        verify(productService, times(1))
            .getCountOfPages(1, "rice");
    }

    @Test
    void can_get_products_of_page_by_input() throws Exception {
        doReturn(List.of(product))
            .when(productService).getProductsNameSortedByPageAndInput(anyInt(), anyInt(), any());

        mockMvc.perform(get("/products/{limit}/{pageNumber}/{input}", 1, 1, "rice"))
            .andExpect(content().json(mapper.writeValueAsString(List.of(product))))
            .andExpect(status().isOk())
            .andDo(print());

        verify(productService, times(1))
            .getProductsNameSortedByPageAndInput(1, 1, "rice");
    }

    @Test
    void can_get_products_of_dish() throws Exception {
        doReturn(List.of(product))
            .when(productService).getProductsOfDish(anyInt());

        mockMvc.perform(get("/products/dish/{dishId}", 1))
            .andExpect(content().json(mapper.writeValueAsString(List.of(product))))
            .andExpect(status().isOk())
            .andDo(print());

        verify(productService, times(1))
            .getProductsOfDish(1);
    }

    @Test
    @WithUserDetails("user@gmail.com")
    void can_get_user_allergic_products() throws Exception {
        doReturn(List.of(product))
            .when(productService).getUserAllergicProducts(anyInt());

        mockMvc.perform(get("/products/allergic"))
            .andExpect(content().json(mapper.writeValueAsString(List.of(product))))
            .andExpect(status().isOk())
            .andDo(print());

        verify(productService, times(1))
            .getUserAllergicProducts(1);
    }
}
