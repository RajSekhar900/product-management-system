package com.raj.productmanagement.service;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.raj.productmanagement.model.Product;
import com.raj.productmanagement.repository.ProductRepository;

@Service
public class ProductService {

    @Autowired
    private ProductRepository repo;

    // Get all products
    public List<Product> getAllProducts() {

        return repo.findAll();
    }

    // Add product with image
    public Product addProduct(Product product,
                              MultipartFile imageFile) throws IOException {

        product.setImageName(imageFile.getOriginalFilename());

        product.setImageType(imageFile.getContentType());

        product.setImageData(imageFile.getBytes());

        return repo.save(product);
    }

    // Get product by id
    public Product getProductById(int id) {

        return repo.findById(id).orElse(null);
    }

    // Update product
    public Product updateProduct(int id, Product product) {

        product.setId(id);

        return repo.save(product);
    }

    // Search product
    public List<Product> searchProducts(String keyword) {

        return repo.findByNameContainingIgnoreCase(keyword);
    }

    // Delete product
    public void deleteProduct(int id) {

        repo.deleteById(id);
    }

    // Get image by product id
    public byte[] getImageByProductId(int id) {

        Product product = repo.findById(id).orElse(null);

        return product.getImageData();
    }
}