package com.freshmart.controller;

import com.freshmart.model.CartItem;
import com.freshmart.model.Order;
import com.freshmart.model.OrderItem;
import com.freshmart.model.Product;
import com.freshmart.repository.ProductRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @PersistenceContext
    private EntityManager em;

    private final ProductRepository productRepository;

    public OrderController(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @PostMapping("/checkout")
    @Transactional
    public ResponseEntity<?> checkout(@RequestBody Map<String, String> body) {
        String cartId = body.get("cartId");
        String customerName = body.get("customerName");
        String email = body.get("email");
        String phone = body.get("phone");
        String address = body.get("address");

        if (cartId == null || customerName == null || email == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "cartId, customerName, and email are required"));
        }

        List<CartItem> cartItems = CartController.peekCart(cartId);
        if (cartItems == null || cartItems.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Cart is empty or not found"));
        }

        // Calculate total
        BigDecimal total = cartItems.stream()
                .map(i -> i.getPrice().multiply(BigDecimal.valueOf(i.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // Create order
        Order order = new Order();
        order.setCustomerName(customerName);
        order.setEmail(email);
        order.setPhone(phone);
        order.setAddress(address);
        order.setTotal(total);
        order.setStatus("CONFIRMED");
        em.persist(order);
        em.flush();

        // Create order items and decrement stock
        for (CartItem ci : cartItems) {
            OrderItem oi = new OrderItem();
            oi.setOrderId(order.getId());
            oi.setProductName(ci.getProductName());
            oi.setQuantity(ci.getQuantity());
            oi.setUnitPrice(ci.getPrice());
            em.persist(oi);

            // Decrement stock
            Product product = productRepository.findById(ci.getProductId()).orElse(null);
            if (product != null) {
                product.setStockQuantity(Math.max(0, product.getStockQuantity() - ci.getQuantity()));
                productRepository.save(product);
            }
        }

        // Clear the cart
        CartController.getAndClearCart(cartId);

        return ResponseEntity.ok(order);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Order> getOrder(@PathVariable Long id) {
        Order order = em.find(Order.class, id);
        if (order == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(order);
    }
}
