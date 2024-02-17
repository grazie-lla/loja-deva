package com.tech.ada.java.lojadeva.controller;

import com.tech.ada.java.lojadeva.domain.Product;
import com.tech.ada.java.lojadeva.dto.ProductRequest;
import com.tech.ada.java.lojadeva.dto.UpdateProductDetailsRequest;
import com.tech.ada.java.lojadeva.dto.UpdateProductRequest;
import com.tech.ada.java.lojadeva.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController()
@RequestMapping("/product")
public class ProductController {
    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    public ResponseEntity<Product> registerProduct(@RequestBody ProductRequest productRequest) {
        Product newProduct = productService.registerProduct(productRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(newProduct);
    }

    @GetMapping
    public List<Product> findAllProducts() {
        return productService.findAllProducts();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Optional<Product>> findProductById(@PathVariable Long id) {
        Optional<Product> product = productService.findProductById(id);
        return ResponseEntity.ok().body(product);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable Long id, @RequestBody UpdateProductRequest updateProductRequest){
        ResponseEntity<Product> productUpdated = productService.updateProduct(id, updateProductRequest);
        return ResponseEntity.status(productUpdated.getStatusCode()).body(productUpdated.getBody());
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Product> updateProductDetails(@PathVariable Long id, @RequestBody UpdateProductDetailsRequest updateDetailsRequest){
        ResponseEntity<Product> productDetailsUpdated = productService.updateProductDetails(id, updateDetailsRequest);
        return ResponseEntity.status(productDetailsUpdated.getStatusCode()).body(productDetailsUpdated.getBody());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProductById(@PathVariable Long id) {
        ResponseEntity<String> deletedProduct = productService.deleteProductById(id);
        return ResponseEntity.status(deletedProduct.getStatusCode()).body(deletedProduct.getBody());
    }

}

