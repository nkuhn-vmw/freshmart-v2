package com.freshmart.controller;

import com.freshmart.model.CartItem;
import com.freshmart.model.Product;
import com.freshmart.repository.ProductRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    private static final ConcurrentHashMap<String, List<CartItem>> carts = new ConcurrentHashMap<>();

    private final ProductRepository productRepository;

    public CartController(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @PostMapping
    public Map<String, String> createCart() {
        String cartId = UUID.randomUUID().toString();
        carts.put(cartId, Collections.synchronizedList(new ArrayList<>()));
        return Map.of("cartId", cartId);
    }

    @GetMapping("/{cartId}")
    public ResponseEntity<List<CartItem>> getCart(@PathVariable String cartId) {
        List<CartItem> items = carts.get(cartId);
        if (items == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(items);
    }

    @PostMapping("/{cartId}/items")
    public ResponseEntity<CartItem> addItem(@PathVariable String cartId, @RequestBody Map<String, Object> body) {
        List<CartItem> items = carts.get(cartId);
        if (items == null) return ResponseEntity.notFound().build();

        Long productId = Long.valueOf(body.get("productId").toString());
        int quantity = body.containsKey("quantity") ? Integer.parseInt(body.get("quantity").toString()) : 1;

        Product product = productRepository.findById(productId).orElse(null);
        if (product == null) return ResponseEntity.badRequest().build();

        // Check if item already in cart
        Optional<CartItem> existing = items.stream()
                .filter(i -> i.getProductId().equals(productId))
                .findFirst();

        if (existing.isPresent()) {
            existing.get().setQuantity(existing.get().getQuantity() + quantity);
            return ResponseEntity.ok(existing.get());
        }

        CartItem item = new CartItem(productId, product.getName(), product.getEmoji(), product.getPrice(), quantity);
        items.add(item);
        return ResponseEntity.ok(item);
    }

    @PutMapping("/{cartId}/items/{productId}")
    public ResponseEntity<CartItem> updateItemQuantity(@PathVariable String cartId, @PathVariable Long productId,
                                                        @RequestBody Map<String, Object> body) {
        List<CartItem> items = carts.get(cartId);
        if (items == null) return ResponseEntity.notFound().build();

        int quantity = Integer.parseInt(body.get("quantity").toString());

        Optional<CartItem> existing = items.stream()
                .filter(i -> i.getProductId().equals(productId))
                .findFirst();

        if (existing.isEmpty()) return ResponseEntity.notFound().build();

        if (quantity <= 0) {
            items.remove(existing.get());
            return ResponseEntity.ok(existing.get());
        }

        existing.get().setQuantity(quantity);
        return ResponseEntity.ok(existing.get());
    }

    @DeleteMapping("/{cartId}/items/{productId}")
    public ResponseEntity<Void> removeItem(@PathVariable String cartId, @PathVariable Long productId) {
        List<CartItem> items = carts.get(cartId);
        if (items == null) return ResponseEntity.notFound().build();
        items.removeIf(i -> i.getProductId().equals(productId));
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{cartId}/total")
    public ResponseEntity<Map<String, BigDecimal>> getTotal(@PathVariable String cartId) {
        List<CartItem> items = carts.get(cartId);
        if (items == null) return ResponseEntity.notFound().build();
        BigDecimal total = items.stream()
                .map(i -> i.getPrice().multiply(BigDecimal.valueOf(i.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        return ResponseEntity.ok(Map.of("total", total));
    }

    // Internal: get and clear cart (used by OrderController)
    public static List<CartItem> getAndClearCart(String cartId) {
        return carts.remove(cartId);
    }

    public static List<CartItem> peekCart(String cartId) {
        return carts.get(cartId);
    }
}
