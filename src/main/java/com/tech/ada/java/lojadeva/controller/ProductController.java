package com.tech.ada.java.lojadeva.controller;

import com.tech.ada.java.lojadeva.domain.Product;
import com.tech.ada.java.lojadeva.dto.ProductRequest;
import com.tech.ada.java.lojadeva.repository.ProductRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController("/product")
public class ProductController {

    private final ProductRepository productRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public ProductController(ProductRepository productRepository, ModelMapper modelMapper){
        this.productRepository = productRepository;
        this.modelMapper = modelMapper;
    }

    @PostMapping("/product")
    public ResponseEntity<Product> registerProduct(@RequestBody ProductRequest productRequest) {
        Product convertedProduct = modelMapper.map(productRequest, Product.class);
        Product newProduct = productRepository.save(convertedProduct);
        return ResponseEntity.status(HttpStatus.CREATED).body(newProduct);

    }

    @GetMapping("/product")
    public List<Product> findAllProducts(){
        return productRepository.findAll();
    }

    @GetMapping("product/{id}")
    public ResponseEntity<Optional<Product>> findProductById(@PathVariable Long id){
        Optional<Product> product = productRepository.findById(id);
        return ResponseEntity.ok().body(product);
    }
}

