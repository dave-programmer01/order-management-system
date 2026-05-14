package com.orderms.product.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

// Incoming request shape for creating or updating a product
public record ProductRequest(
        @NotBlank String name,
        String description,
        @Positive BigDecimal price,
        @Min(0) Integer stockQuantity
) {}