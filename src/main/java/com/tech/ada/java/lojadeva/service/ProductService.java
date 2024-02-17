package com.tech.ada.java.lojadeva.service;

import com.tech.ada.java.lojadeva.domain.Product;
import com.tech.ada.java.lojadeva.dto.ProductRequest;
import com.tech.ada.java.lojadeva.dto.UpdateProductRequest;
import com.tech.ada.java.lojadeva.repository.ProductRepository;
import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final ModelMapper modelMapper;

    public ProductService(ProductRepository productRepository, ModelMapper modelMapper) {
        this.productRepository = productRepository;
        this.modelMapper = modelMapper;
    }

    public Product registerProduct(ProductRequest productRequest) {
        Product convertedProduct = modelMapper.map(productRequest, Product.class);
        return productRepository.save(convertedProduct);
    }

    public List<Product> findAllProducts() {
        return productRepository.findAll();
    }

    public Optional<Product> findProductById(Long id) {
        return productRepository.findById(id);
    }

    public ResponseEntity<Product> updateProduct(Long id, UpdateProductRequest updateProductRequest){

        Optional<Product> productToBeUpdate = findProductById(id);

        if (productToBeUpdate.isPresent()) {
            Product productFound = productToBeUpdate.get();

            updateProductRequest.updateProduct(productFound);

            Product productUpdated = productRepository.save(productFound);

            return ResponseEntity.ok(productUpdated);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    public ResponseEntity<String> deleteProductById(Long id) {
        Optional<Product> productToDelete = findProductById(id);

        if (productToDelete.isPresent()) {
            productRepository.deleteById(id);
            return ResponseEntity.ok("Produto excluído com sucesso.");
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
