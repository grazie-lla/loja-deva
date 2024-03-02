package com.tech.ada.java.lojadeva.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tech.ada.java.lojadeva.domain.Product;
import com.tech.ada.java.lojadeva.dto.ProductRequest;
import com.tech.ada.java.lojadeva.dto.UpdateProductDetailsRequest;
import com.tech.ada.java.lojadeva.dto.UpdateProductRequest;
import com.tech.ada.java.lojadeva.service.ProductService;
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
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class ProductControllerTest {
    @Mock
    private ProductService productService;
    private ProductRequest productRequest;
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
        updateProductDetailsRequest = new UpdateProductDetailsRequest("Smartphone", new BigDecimal("1000.99"), 10);
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
        when(productService.registerProduct(Mockito.any())).thenReturn(product);

        mockMvc.perform(MockMvcRequestBuilders.post("/product").
                contentType(MediaType.APPLICATION_JSON).
                content(asJsonString(productRequest))).andExpect(status().isCreated());

        verify(productService, times(1)).registerProduct(Mockito.any());
    }

    @Test
    public void findAllProductsHttpTest() throws Exception {
        when(productService.findAllProducts()).thenReturn(productsList);

        mockMvc.perform(MockMvcRequestBuilders.get("/product").
                contentType(MediaType.APPLICATION_JSON).
                content(asJsonString(productRequest))).andDo(MockMvcResultHandlers.print());

        verify(productService, times(1)).findAllProducts();
    }

    @Test
    public void findProductByIdHttpRequest() throws Exception {
        when(productService.findProductById(Mockito.any())).thenReturn(Optional.of(product));

        mockMvc.perform(MockMvcRequestBuilders.get("/product/1").
                        contentType(MediaType.APPLICATION_JSON).
                        content(asJsonString(product))).
                andDo(MockMvcResultHandlers.print());

        verify(productService, times(1)).findProductById(Mockito.any());
    }
    @Test
    public void updateProductHttpTest() throws Exception {
        when(productService.updateProduct(Mockito.any(), Mockito.any(UpdateProductRequest.class)))
                .thenReturn(ResponseEntity.of(Optional.of(product)));

        mockMvc.perform(MockMvcRequestBuilders.put("/product/1").
                contentType(MediaType.APPLICATION_JSON).
                content(asJsonString(updateProductRequest)))
                .andDo(MockMvcResultHandlers.print());

        verify(productService, times(1)).updateProduct(Mockito.any(), Mockito.any(UpdateProductRequest.class));
    }
    @Test
    public void updateProductDetails() throws Exception {
        when(productService.updateProductDetails(Mockito.any(), Mockito.any(UpdateProductDetailsRequest.class)))
                .thenReturn(ResponseEntity.of(Optional.of(product)));

        mockMvc.perform(MockMvcRequestBuilders.patch("/product/1").
                        contentType(MediaType.APPLICATION_JSON).
                        content(asJsonString(updateProductDetailsRequest)))
                        .andDo(MockMvcResultHandlers.print());

        verify(productService, times(1)).updateProductDetails(Mockito.any(), Mockito.any(UpdateProductDetailsRequest.class));
    }


}