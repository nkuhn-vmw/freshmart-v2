# FreshMart v2 — Modern Grocery Ecommerce Platform

## Overview
FreshMart is a modern online grocery store inspired by Kroger.com. It features a polished, professional UI with vibrant colors, high-quality product imagery, category-based navigation, a persistent shopping cart, and streamlined checkout. Built with Spring Boot + vanilla HTML/CSS/JS (no frameworks).

## Design Language (Kroger-inspired)
- **Color palette**: Primary green (#2d7d46) for brand identity, white (#ffffff) background, dark text (#1a1a2e), accent orange (#ff6b35) for CTAs and promotions, light gray (#f5f5f5) for card backgrounds
- **Typography**: System font stack (-apple-system, BlinkMacSystemFont, Segoe UI), bold product names, large prices in green
- **Product cards**: White cards with subtle shadow, product emoji as placeholder image, category badge, price in large green text, "Add to Cart" button in orange
- **Layout**: Full-width header with logo + search bar + cart icon with count badge, category filter pills below header, responsive product grid (4 columns desktop, 2 mobile, 1 small mobile)

## Features

### 1. Product Catalog (Phase 1)
**Entity: Product**
- Fields: Long id, String name, String description, BigDecimal price, Category category, int stockQuantity, String emoji (product emoji icon)
- Category enum: PRODUCE, DAIRY, BAKERY, MEAT, SEAFOOD, BEVERAGES, SNACKS, FROZEN, PANTRY, HOUSEHOLD

**REST API:**
- GET /api/products — all products
- GET /api/products/{id} — single product
- GET /api/products/category/{category} — filter by category
- GET /api/products/search?q={term} — search by name (LIKE query)
- POST /api/products — create product (admin)

**Data Seeder** — 20 products across categories:
| Name | Category | Price | Emoji |
|------|----------|-------|-------|
| Organic Bananas | PRODUCE | 1.29 | 🍌 |
| Avocados (3pk) | PRODUCE | 4.99 | 🥑 |
| Fresh Strawberries | PRODUCE | 3.99 | 🍓 |
| Whole Milk 1 Gal | DAIRY | 4.49 | 🥛 |
| Greek Yogurt | DAIRY | 1.79 | 🫙 |
| Cheddar Cheese Block | DAIRY | 5.99 | 🧀 |
| Sourdough Loaf | BAKERY | 5.49 | 🍞 |
| Chocolate Croissants (4pk) | BAKERY | 4.99 | 🥐 |
| Chicken Breast 1lb | MEAT | 8.99 | 🍗 |
| Ground Beef 1lb | MEAT | 7.49 | 🥩 |
| Atlantic Salmon Fillet | SEAFOOD | 12.99 | 🐟 |
| Orange Juice 64oz | BEVERAGES | 3.79 | 🍊 |
| Sparkling Water 12pk | BEVERAGES | 5.99 | 💧 |
| Trail Mix 16oz | SNACKS | 6.49 | 🥜 |
| Dark Chocolate Bar | SNACKS | 2.99 | 🍫 |
| Frozen Pizza Margherita | FROZEN | 6.99 | 🍕 |
| Ice Cream Vanilla 1pt | FROZEN | 4.99 | 🍦 |
| Pasta Penne 1lb | PANTRY | 1.99 | 🍝 |
| Extra Virgin Olive Oil | PANTRY | 8.99 | 🫒 |
| Paper Towels 6pk | HOUSEHOLD | 9.99 | 🧻 |

### 2. Storefront UI (Phase 1)
**src/main/resources/static/index.html** — single page app:

**Header:**
- Full-width green (#2d7d46) background
- Left: FreshMart logo (text + 🛒 emoji)
- Center: Search bar with magnifying glass icon (filters products client-side)
- Right: Cart icon with orange count badge showing number of items

**Category Filter Bar:**
- Horizontal scrollable pills below header
- "All" pill + one per category with emoji: 🍎 Produce, 🥛 Dairy, 🍞 Bakery, etc.
- Active pill highlighted in green, others in light gray
- Clicking filters the product grid via JavaScript (fetch /api/products/category/{cat})

**Product Grid:**
- CSS Grid: 4 columns on desktop (>1024px), 3 on tablet (>768px), 2 on mobile (>480px), 1 on small
- Each product card:
  - Large emoji as "image" (centered, 64px font size)
  - Product name (bold, 16px)
  - Category badge (small, gray pill)
  - Price (24px, green, bold)
  - Stock indicator ("In Stock" green or "Low Stock" orange if < 5)
  - "Add to Cart" button (orange background, white text, rounded)
  - Hover: card lifts with shadow, button darkens

**Cart Sidebar:**
- Slides in from right when cart icon clicked
- Shows cart items with name, qty, price, remove button
- Quantity +/- buttons per item
- Subtotal at bottom
- "Checkout" button (green, full width)
- Cart persists via localStorage (cartId)

**Checkout Modal:**
- Overlay modal with form: name, email, phone, address
- Order summary showing items + total
- "Place Order" button
- On success: show confirmation with order ID, clear cart

**Footer:**
- Dark gray background
- "Powered by jclaw 🤖" centered
- Links: About, Contact, Privacy (placeholder)

### 3. Shopping Cart API (Phase 2)
- POST /api/cart — create cart, returns {cartId}
- GET /api/cart/{cartId} — get cart items
- POST /api/cart/{cartId}/items — add item {productId, quantity}
- PUT /api/cart/{cartId}/items/{productId} — update quantity
- DELETE /api/cart/{cartId}/items/{productId} — remove item
- GET /api/cart/{cartId}/total — get cart total

Cart stored in-memory via ConcurrentHashMap<String, List<CartItem>>.

### 4. Checkout & Orders (Phase 2)
- POST /api/orders/checkout — create order from cart
  - Accepts: {cartId, customerName, email, phone, address}
  - Creates Order entity with line items
  - Decrements stock
  - Clears cart
  - Returns order with items
- GET /api/orders/{id} — get order with line items

Order entity: id, customerName, email, phone, address, total, status (PENDING/CONFIRMED), createdAt
OrderItem entity: id, orderId, productName, quantity, unitPrice

## Technical Requirements
- Spring Boot 3.4.2, Java 21
- H2 in-memory database with JPA
- No authentication (public storefront)
- All monetary values use BigDecimal
- Package: com.freshmart
- CSS: embedded in index.html (no external files)
- JS: embedded in index.html (no external files)
- Mobile-first responsive design
- No JavaScript frameworks (vanilla JS only)

## Non-Goals
- Payment processing
- User accounts / authentication
- Real delivery scheduling
- Image uploads (use emoji as product icons)
