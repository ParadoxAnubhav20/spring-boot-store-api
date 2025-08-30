package com.anubhav.dev.store.controllers;

import java.net.URI;
import java.util.*;

import com.anubhav.dev.store.dtos.ProductDto;
import com.anubhav.dev.store.entities.Category;
import com.anubhav.dev.store.entities.Product;
import com.anubhav.dev.store.mappers.ProductMapper;
import com.anubhav.dev.store.repositories.CategoryRepository;
import com.anubhav.dev.store.repositories.ProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@AllArgsConstructor
@RequestMapping("/products")
public class ProductController {
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final CategoryRepository categoryRepository;

    @GetMapping
    public List<ProductDto> getAllProducts(@RequestParam(required = false, name = "categoryId") Byte categoryId) {
        List<Product> products;
        if (categoryId != null) {
            products = productRepository.findByCategoryId(categoryId);
        } else {
            products = productRepository.findAllWithCategory();
        }
        return products.stream().map(productMapper::toDto).toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDto> getProductById(@PathVariable long id) {
        Product product = productRepository.findById(id).orElse(null);
        if (product == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(productMapper.toDto(product));
    }

    @PostMapping
    public ResponseEntity<ProductDto> createProduct(@RequestBody ProductDto productDto, UriComponentsBuilder uriBuilder) {
        Category category = categoryRepository.findById(productDto.getCategoryId()).orElse(null);
        if (category == null) return ResponseEntity.badRequest().build();
        Product product = productMapper.toEntity(productDto);
        product.setCategory(category);
        productRepository.save(product);
        productDto.setId(product.getId());
        URI uri = uriBuilder.path("/products/{id}").buildAndExpand(product.getId()).toUri();
        return ResponseEntity.created(uri).body(productDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductDto> updateProduct(@PathVariable long id, @RequestBody ProductDto productDto) {
        Product product = productRepository.findById(id).orElse(null);
        if (product == null) return ResponseEntity.notFound().build();
        Category category = categoryRepository.findById(productDto.getCategoryId()).orElse(null);
        if (category == null) return ResponseEntity.badRequest().build();
        productMapper.updateProduct(productDto, product);
        product.setCategory(category);
        productRepository.save(product);
        productDto.setId(product.getId());
        return ResponseEntity.ok(productDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ProductDto> deleteProduct(@PathVariable long id) {
        Product product = productRepository.findById(id).orElse(null);
        if (product == null) return ResponseEntity.notFound().build();
        productRepository.delete(product);
        return ResponseEntity.noContent().build();
    }
}
