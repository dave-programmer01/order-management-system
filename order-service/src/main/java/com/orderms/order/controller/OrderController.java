package com.orderms.order.controller;

import com.orderms.order.dto.OrderRequest;
import com.orderms.order.dto.OrderResponse;
import com.orderms.order.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(
            @Valid @RequestBody OrderRequest request,
            // The gateway injects the authenticated user's email
            @RequestHeader("X-User-Email") String userEmail,
            // We forward the JWT so the ProductClient can authenticate downstream
            @RequestHeader("Authorization") String authorizationHeader) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(orderService.createOrder(request, userEmail, authorizationHeader));
    }

    @GetMapping
    public ResponseEntity<List<OrderResponse>> getMyOrders(
            @RequestHeader("X-User-Email") String userEmail) {
        return ResponseEntity.ok(orderService.getOrdersByUser(userEmail));
    }
}