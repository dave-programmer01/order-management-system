package com.orderms.order.repository;

import com.orderms.order.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

// Spring Data JPA repository for Order entities
public interface OrderRepository extends JpaRepository<Order, Long> {
    // Custom query method to find all orders by a specific user
    List<Order> findByUserEmail(String userEmail);
}