package com.example.ecommerce;

import com.example.ecommerce.model.Product;
import com.example.ecommerce.repository.ProductRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements CommandLineRunner {

    private final ProductRepository productRepository;

    public DataLoader(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public void run(String... args) {
        if (productRepository.count() > 0) {
            return;
        }

        Product phone = new Product(
                "Smartphone",
                "Simple smartphone suitable for everyday use.",
                299.99,
                "/images/phone.jpg",
                "Electronics",
                25
        );

        Product laptop = new Product(
                "Laptop",
                "Lightweight laptop for study and work.",
                799.00,
                "/images/laptop.jpg",
                "Computers",
                12
        );

        Product headphones = new Product(
                "Headphones",
                "Comfortable over-ear headphones.",
                59.50,
                "/images/headphones.jpg",
                "Accessories",
                40
        );

        Product camera = new Product(
                "Digital Camera",
                "Entry-level camera for beginners.",
                449.00,
                "/images/camera.jpg",
                "Cameras",
                8
        );

        productRepository.save(phone);
        productRepository.save(laptop);
        productRepository.save(headphones);
        productRepository.save(camera);
    }
}

