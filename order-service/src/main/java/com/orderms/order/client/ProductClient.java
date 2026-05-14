package com.orderms.order.client;

import com.orderms.order.dto.ProductResponse;
import com.orderms.order.exception.ProductServiceUnavailableException;
import com.orderms.order.exception.ResourceNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;

@Component
public class ProductClient {

    private final RestClient restClient;

    public ProductClient(RestClient.Builder restClientBuilder) {
        // Point at the API Gateway so JWT validation happens on the call
        this.restClient = restClientBuilder
                .baseUrl("http://localhost:8080")
                .build();
    }

    @CircuitBreaker(name = "productService", fallbackMethod = "productServiceFallback")
    @Retry(name = "productService")
    public ProductResponse getProductById(Long productId, String authorizationHeader) {
        return restClient.get()
                .uri("/api/products/{id}", productId)
                .header("Authorization", authorizationHeader)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, (request, response) -> {
                    if (response.getStatusCode().value() == 404) {
                        throw new ResourceNotFoundException("Product not found with id: " + productId);
                    }
                })
                .onStatus(HttpStatusCode::is5xxServerError, (request, response) -> {
                    throw new ProductServiceUnavailableException("Product service is temporarily unavailable");
                })
                .body(ProductResponse.class);
    }

    // Fallback method invoked when circuit breaker is open or all retries are exhausted
    private ProductResponse productServiceFallback(Long productId, String authorizationHeader, Exception e) {
        throw new ProductServiceUnavailableException(
                "Product service is temporarily unavailable, please try again later. Reason: " + e.getMessage()
        );
    }
}