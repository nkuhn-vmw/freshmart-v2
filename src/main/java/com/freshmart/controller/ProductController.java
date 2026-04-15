package com.freshmart.controller;

import com.freshmart.model.Category;
import com.freshmart.model.Product;
import com.freshmart.repository.ProductRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductRepository productRepository;

    public ProductController(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @GetMapping
    public List<Product> listAll() {
        return productRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getById(@PathVariable Long id) {
        return productRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/category/{category}")
    public List<Product> getByCategory(@PathVariable Category category) {
        return productRepository.findByCategory(category);
    }

    @GetMapping("/search")
    public List<Product> search(@RequestParam("q") String term) {
        return productRepository.findByNameContainingIgnoreCase(term);
    }
}
