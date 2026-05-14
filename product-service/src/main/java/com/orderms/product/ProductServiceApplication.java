package com.orderms.product;

import com.orderms.product.entity.Product;
import com.orderms.product.repository.ProductRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.math.BigDecimal;

@SpringBootApplication
public class ProductServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProductServiceApplication.class, args);
    }

    // Seed sample products on startup so you have data to test with
    @Bean
    CommandLineRunner initData(ProductRepository productRepository) {
        return args -> {
            productRepository.save(Product.builder()
                    .name("Wireless Keyboard")
                    .description("Ergonomic wireless keyboard with backlight")
                    .price(new BigDecimal("49.99"))
                    .stockQuantity(100)
                    .build());

            productRepository.save(Product.builder()
                    .name("USB-C Hub")
                    .description("7-in-1 USB-C hub with HDMI and ethernet")
                    .price(new BigDecimal("35.99"))
                    .stockQuantity(50)
                    .build());

            productRepository.save(Product.builder()
                    .name("Mechanical Mouse")
                    .description("High-precision mechanical mouse for developers")
                    .price(new BigDecimal("29.99"))
                    .stockQuantity(75)
                    .build());
        };
    }
}