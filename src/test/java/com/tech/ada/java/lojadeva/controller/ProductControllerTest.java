package com.tech.ada.java.lojadeva.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tech.ada.java.lojadeva.domain.Product;
import com.tech.ada.java.lojadeva.dto.ProductRequest;
import com.tech.ada.java.lojadeva.dto.UpdateProductDetailsRequest;
import com.tech.ada.java.lojadeva.dto.UpdateProductRequest;
import com.tech.ada.java.lojadeva.repository.ProductRepository;
import com.tech.ada.java.lojadeva.service.ProductService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class ProductControllerTest {
    @Mock
    private ProductService productService;
    @Mock
    private ProductRepository productRepository;
    private ProductRequest productRequest;
    @Mock
    private Product product;
    private UpdateProductRequest updateProductRequest;
    private UpdateProductDetailsRequest updateProductDetailsRequest;
    private List<Product> productsList;

    @InjectMocks
    private ProductController productController;
    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        productRequest = new ProductRequest("IPhone", "Smartphone", new BigDecimal("1000.99"), 10, "Eletrônicos");
        product = new Product("IPhone", "Smartphone", new BigDecimal("1000.99"), 10, "Eletrônicos");
        updateProductRequest = new UpdateProductRequest("IPhone X", "Smartphone", new BigDecimal("1100.99"), 11, "Eletrônicos");
        updateProductDetailsRequest = new UpdateProductDetailsRequest("Smartphone", new BigDecimal("1010.99"), 9);
        productsList = Arrays.asList(product, new Product("IPhone X", "Smartphone", new BigDecimal("1100.99"), 11, "Eletrônicos"));
        mockMvc = MockMvcBuilders.standaloneSetup(productController).build();
    }

    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void registerProductHttpTest() throws Exception {
        BigDecimal expectedPrice = new BigDecimal("1000.99");
        BigDecimal tolerance = new BigDecimal("0.01");
        product.setId(1L);
        when(productService.registerProduct(Mockito.any())).thenReturn(product);

        var response = mockMvc.perform(MockMvcRequestBuilders.post("/product").
                contentType(MediaType.APPLICATION_JSON).
                content(asJsonString(productRequest))).andExpect(status().isCreated());

        response.andExpect(jsonPath("$.id").value(1)).
                andExpect(jsonPath("$.name", equalTo("IPhone"))).
                andExpect(jsonPath("$.description", equalTo("Smartphone"))).
                andExpect(jsonPath("$.price",  Matchers.closeTo(expectedPrice.doubleValue(), tolerance.doubleValue()))).
                andExpect(jsonPath("$.inventoryQuantity", equalTo(10))).
                andExpect(jsonPath("$.category", equalTo("Eletrônicos")));
    }

    @Test
    public void findAllProductsHttpTest() throws Exception {
        when(productService.findAllProducts()).thenReturn(productsList);

        mockMvc.perform(MockMvcRequestBuilders.get("/product").
                contentType(MediaType.APPLICATION_JSON).
                content(asJsonString(productRequest))).andDo(MockMvcResultHandlers.print()).
                andExpect(jsonPath("$[0].name", equalTo("IPhone"))).
                andExpect(jsonPath("$[1].name", equalTo("IPhone X")));

    }

    @Test
    public void findProductByIdHttpRequest() throws Exception {
        product.setId(1L);

        when(productService.findProductById(Mockito.any())).thenReturn(Optional.of(product));

        mockMvc.perform(MockMvcRequestBuilders.get("/product/{id}", 1).
                        contentType(MediaType.APPLICATION_JSON).
                        content(asJsonString(product))).
                        andDo(MockMvcResultHandlers.print()).
                        andExpect(jsonPath("$.id").value(1)).
                        andExpect(jsonPath("$.name", equalTo("IPhone")));
    }
    @Test
    public void updateProductHttpTest() throws Exception {
        product.setId(1L);
        product.setName("IPhone X");

        when(productService.updateProduct(Mockito.any(), Mockito.any(UpdateProductRequest.class)))
                .thenReturn(ResponseEntity.of(Optional.of(product)));

        mockMvc.perform(MockMvcRequestBuilders.put("/product/{id}", 1).
                contentType(MediaType.APPLICATION_JSON).
                content(asJsonString(updateProductRequest))).
                andExpect(status().isOk()).
                andExpect(jsonPath("$.id").value(1)).
                andExpect(jsonPath("$.name", equalTo("IPhone X")));
    }
    @Test
    public void updateProductDetails() throws Exception {
        product.setId(1L);

        when(productService.updateProductDetails(Mockito.any(), Mockito.any(UpdateProductDetailsRequest.class)))
                .thenReturn(ResponseEntity.of(Optional.of(product)));

        mockMvc.perform(MockMvcRequestBuilders.patch("/product/{id}", 1).
                        contentType(MediaType.APPLICATION_JSON).
                        content(asJsonString(updateProductDetailsRequest))).andExpect(status().isOk());
    }

    @Test
    public void deleteProductByIdHttpRequest() throws Exception {
        product.setId(1L);
        when(productService.deleteProductById(Mockito.any()))
                .thenReturn(ResponseEntity.ok().body("Produto excluído com sucesso."));


        mockMvc.perform(MockMvcRequestBuilders.delete("/product/{id}", 1).
                        contentType(MediaType.APPLICATION_JSON).
                        content(asJsonString(product))).
                        andDo(MockMvcResultHandlers.print()).
                        andExpect(status().isOk()).
                        andExpect(content().string("Produto excluído com sucesso."));
    }

}