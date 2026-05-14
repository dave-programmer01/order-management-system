package com.orderms.order.dto;

import jakarta.validation.constraints.Min;

// Incoming request payload for creating an order
public record OrderRequest(
        Long productId,
        @Min(1) Integer quantity
) {}