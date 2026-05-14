package com.orderms.order.service;

import com.orderms.order.client.ProductClient;
import com.orderms.order.dto.OrderRequest;
import com.orderms.order.dto.OrderResponse;
import com.orderms.order.dto.ProductResponse;
import com.orderms.order.entity.Order;
import com.orderms.order.entity.OrderStatus;
import com.orderms.order.exception.InsufficientStockException;
import com.orderms.order.exception.ProductServiceUnavailableException;
import com.orderms.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductClient productClient;

    public OrderResponse createOrder(OrderRequest request, String userEmail, String authorizationHeader) {
        try {
            ProductResponse product = productClient.getProductById(request.productId(), authorizationHeader);

            if (product.stockQuantity() < request.quantity()) {
                throw new InsufficientStockException(
                        "Insufficient stock for product: " + product.name() +
                                ". Available: " + product.stockQuantity() +
                                ", Requested: " + request.quantity()
                );
            }

            BigDecimal totalPrice = product.price().multiply(BigDecimal.valueOf(request.quantity()));

            Order order = Order.builder()
                    .userEmail(userEmail)
                    .productId(request.productId())
                    .quantity(request.quantity())
                    .totalPrice(totalPrice)
                    .status(OrderStatus.CONFIRMED)
                    .orderDate(LocalDateTime.now())
                    .build();

            Order savedOrder = orderRepository.save(order);
            return mapToResponse(savedOrder);

        } catch (ProductServiceUnavailableException e) {
            // Save the order as FAILED when the Product Service is unavailable
            Order failedOrder = Order.builder()
                    .userEmail(userEmail)
                    .productId(request.productId())
                    .quantity(request.quantity())
                    .totalPrice(BigDecimal.ZERO)
                    .status(OrderStatus.FAILED)
                    .orderDate(LocalDateTime.now())
                    .build();

            orderRepository.save(failedOrder);
            throw e;
        }
    }

    public List<OrderResponse> getOrdersByUser(String userEmail) {
        return orderRepository.findByUserEmail(userEmail).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    private OrderResponse mapToResponse(Order order) {
        return OrderResponse.builder()
                .id(order.getId())
                .userEmail(order.getUserEmail())
                .productId(order.getProductId())
                .quantity(order.getQuantity())
                .totalPrice(order.getTotalPrice())
                .status(order.getStatus().name())
                .orderDate(order.getOrderDate())
                .build();
    }
}