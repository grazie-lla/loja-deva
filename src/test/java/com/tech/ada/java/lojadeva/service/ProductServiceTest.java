package com.tech.ada.java.lojadeva.service;

import com.tech.ada.java.lojadeva.domain.Product;
import com.tech.ada.java.lojadeva.dto.ProductRequest;
import com.tech.ada.java.lojadeva.dto.UpdateProductDetailsRequest;
import com.tech.ada.java.lojadeva.dto.UpdateProductRequest;
import com.tech.ada.java.lojadeva.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProductServiceTest {
    @Mock
    private ProductRepository productRepository;
    private ProductRequest productRequest;
    private Product product;
    private List<Product> productList;
    private UpdateProductRequest updateProductRequest;
    private UpdateProductDetailsRequest updateDetailsRequest;
    @Mock
    private ModelMapper modelMapper;


    @InjectMocks
    private ProductService productService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        productRequest = new ProductRequest("IPhone", "Smartphone", new BigDecimal("1000.99"), 10, "Eletrônicos");
        product = new Product("IPhone", "Smartphone", new BigDecimal("1000.99"), 10, "Eletrônicos");
        updateProductRequest = new UpdateProductRequest("IPhone X", "Smartphone", new BigDecimal("1100.99"), 11, "Eletrônicos");
        updateDetailsRequest = new UpdateProductDetailsRequest("Iphone novo", new BigDecimal("1230.99"), 12);
        productList = Arrays.asList(product, new Product("IPhone X", "Smartphone", new BigDecimal("1100.99"), 11, "Eletrônicos"));
    }
    @Test
    public void registerProduct() {
        when(modelMapper.map(productRequest, Product.class)).thenReturn(product);
        when(productRepository.save(any(Product.class))).thenReturn(product);


        assertEquals(product.getId(), productService.registerProduct(productRequest).getId());
        assertEquals(product.getName(), productService.registerProduct(productRequest).getName());
        assertEquals(product.getPrice(), productService.registerProduct(productRequest).getPrice());
    }

    @Test
    public void findAllProducts() {
        when(productRepository.findAll()).thenReturn(productList);

        List<Product> retrievedProducts = productService.findAllProducts();

        assertEquals(productList.size(), retrievedProducts.size());
        for (int i = 0; i < productList.size(); i++) {
            Product expectedProduct = productList.get(i);
            Product actualProduct = retrievedProducts.get(i);
            assertEquals(expectedProduct.getName(), actualProduct.getName());
            assertEquals(expectedProduct.getDescription(), actualProduct.getDescription());
            assertEquals(expectedProduct.getPrice(), actualProduct.getPrice());
            assertEquals(expectedProduct.getInventoryQuantity(), actualProduct.getInventoryQuantity());
            assertEquals(expectedProduct.getCategory(), actualProduct.getCategory());
        }
    }

    @Test
    public void findProductById() {
        when(productRepository.findById(Mockito.any())).thenReturn(Optional.of(product));
        product.setId(1L);
        Product expectedProduct = productService.findProductById(1L).get();

        assertEquals(product.getId(), expectedProduct.getId());
        assertEquals(product.getName(), expectedProduct.getName());
        assertEquals(product.getPrice(), expectedProduct.getPrice());
    }

    @Test
    public void updateProduct_ProductFound() {

        Product updatedProduct = new Product(
                updateProductRequest.name(),
                updateProductRequest.description(),
                updateProductRequest.price(),
                updateProductRequest.inventoryQuantity(),
                updateProductRequest.category()
        );
        updatedProduct.setId(1L);
        product.setId(1L);


        when(productRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(product));
        when(productRepository.save(product)).thenReturn(updatedProduct);

        ResponseEntity<Product> result = productService.updateProduct(1L, updateProductRequest);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(updatedProduct, result.getBody());
    }

    @Test
    public void updateProduct_ProductNotFound() {
        when(productRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());

        ResponseEntity<Product> result = productService.updateProduct(1L, updateProductRequest);

        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());

        assertNull(result.getBody());

        verify(productRepository, never()).save(Mockito.any());
    }

    @Test
    public void updateProductDetails_ProductFound() {
        when(productRepository.findById(Mockito.any())).thenReturn(Optional.of(product));

        ResponseEntity<Product> result = productService.updateProductDetails(1L, updateDetailsRequest);

        assertEquals(HttpStatus.OK, result.getStatusCode());
    }

    @Test
    public void updateProductDetails_ProductNotFound() {
        when(productRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());

        ResponseEntity<Product> result = productService.updateProductDetails(1L, updateDetailsRequest);

        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());

        assertNull(result.getBody());

        verify(productRepository, never()).save(Mockito.any());
    }

    @Test
    public void deleteProductById_ProductFound() {
        when(productRepository.findById(Mockito.any())).thenReturn(Optional.of(product));

        ResponseEntity<String> result = productService.deleteProductById(1L);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals("Produto excluído com sucesso.", result.getBody());
    }
    @Test
    public void deleteProductById_ProductNotFound() {
        when(productRepository.findById(Mockito.any())).thenReturn(Optional.empty());

        ResponseEntity<String> result = productService.deleteProductById(1L);

        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
        assertNull(result.getBody());

        verify(productRepository, never()).deleteById(Mockito.any());
    }
}