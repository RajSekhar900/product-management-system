package com.raj.productmanagement.controller;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;
import org.springframework.security.core.Authentication;

import com.raj.productmanagement.model.Product;
import com.raj.productmanagement.service.ProductService;

@Controller
public class ProductController {

    @Autowired
    private ProductService service;

    @GetMapping("/")
    public String home() {
        return "forward:/index.html";
    }

    // Get all products
    @GetMapping("/api/products")
    public ResponseEntity<List<Product>> getAllProducts() {

        return ResponseEntity.ok(service.getAllProducts());
    }

    // Add product with image
    @PostMapping("/api/product")
    public ResponseEntity<Product> addProduct(
            @RequestPart Product product,
            @RequestPart MultipartFile imageFile) throws IOException {

        return ResponseEntity.ok(service.addProduct(product, imageFile));
    }

    // Get product by id
    @GetMapping("/api/product/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable int id) {

        return ResponseEntity.ok(service.getProductById(id));
    }

    // Update product
    @PutMapping("/api/product/{id}")
    public ResponseEntity<Product> updateProduct(
            @PathVariable int id,
            @RequestBody Product product) {

        return ResponseEntity.ok(service.updateProduct(id, product));
    }

    // Search product
    @GetMapping("/api/product/search/{keyword}")
    public ResponseEntity<List<Product>> searchProducts(
            @PathVariable String keyword) {

        return ResponseEntity.ok(service.searchProducts(keyword));
    }

    // Delete product
    @DeleteMapping("/api/product/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable int id) {

        service.deleteProduct(id);

        return ResponseEntity.ok("Product Deleted");
    }

    // Get image by product id
    @GetMapping("/api/product/{id}/image")
    public ResponseEntity<byte[]> getImageByProductId(@PathVariable int id) {

        Product product = service.getProductById(id);

        byte[] imageFile = service.getImageByProductId(id);

        return ResponseEntity.ok()
                .contentType(MediaType.valueOf(product.getImageType()))
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + product.getImageName() + "\"")
                .body(imageFile);
    }

    // Get logged in user role
    @GetMapping("/api/user")
    public ResponseEntity<Map<String, String>> getLoggedInUser(Authentication authentication) {

        Map<String, String> userData = new HashMap<>();

        if(authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {

            userData.put("role", "ADMIN");

        } else {

            userData.put("role", "USER");
        }

        return ResponseEntity.ok(userData);
    }
}