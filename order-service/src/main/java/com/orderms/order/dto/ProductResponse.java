package com.orderms.order.dto;

import java.math.BigDecimal;

// Mirrors the Product Service response so we can deserialize it
public record ProductResponse(
        Long id,
        String name,
        String description,
        BigDecimal price,
        Integer stockQuantity
) {

}