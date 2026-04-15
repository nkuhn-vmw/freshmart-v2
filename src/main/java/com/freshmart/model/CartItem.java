package com.freshmart.model;

import java.math.BigDecimal;

public class CartItem {
    private Long productId;
    private String productName;
    private String emoji;
    private BigDecimal price;
    private int quantity;

    public CartItem() {}

    public CartItem(Long productId, String productName, String emoji, BigDecimal price, int quantity) {
        this.productId = productId;
        this.productName = productName;
        this.emoji = emoji;
        this.price = price;
        this.quantity = quantity;
    }

    public Long getProductId() { return productId; }
    public void setProductId(Long productId) { this.productId = productId; }

    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }

    public String getEmoji() { return emoji; }
    public void setEmoji(String emoji) { this.emoji = emoji; }

    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
}
