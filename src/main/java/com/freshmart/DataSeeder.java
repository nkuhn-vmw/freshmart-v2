package com.freshmart;

import com.freshmart.model.Category;
import com.freshmart.model.Product;
import com.freshmart.repository.ProductRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class DataSeeder implements CommandLineRunner {

    private final ProductRepository productRepository;

    public DataSeeder(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public void run(String... args) {
        if (productRepository.count() > 0) return;

        seed("Organic Bananas", "Fresh organic bananas", "1.29", Category.PRODUCE, 150, "\uD83C\uDF4C");
        seed("Avocados (3pk)", "Ripe Hass avocados", "4.99", Category.PRODUCE, 80, "\uD83E\uDD51");
        seed("Fresh Strawberries", "Sweet seasonal strawberries", "3.99", Category.PRODUCE, 60, "\uD83C\uDF53");
        seed("Whole Milk 1 Gal", "Vitamin D whole milk", "4.49", Category.DAIRY, 100, "\uD83E\uDD5B");
        seed("Greek Yogurt", "Plain nonfat Greek yogurt", "1.79", Category.DAIRY, 120, "\uD83E\uDED9");
        seed("Cheddar Cheese Block", "Sharp cheddar cheese 8oz", "5.99", Category.DAIRY, 90, "\uD83E\uDDC0");
        seed("Sourdough Loaf", "Artisan sourdough bread", "5.49", Category.BAKERY, 40, "\uD83C\uDF5E");
        seed("Chocolate Croissants (4pk)", "Buttery chocolate croissants", "4.99", Category.BAKERY, 30, "\uD83E\uDD50");
        seed("Chicken Breast 1lb", "Boneless skinless chicken breast", "8.99", Category.MEAT, 70, "\uD83C\uDF57");
        seed("Ground Beef 1lb", "80/20 ground beef", "7.49", Category.MEAT, 55, "\uD83E\uDD69");
        seed("Atlantic Salmon Fillet", "Fresh Atlantic salmon", "12.99", Category.SEAFOOD, 35, "\uD83D\uDC1F");
        seed("Orange Juice 64oz", "100% pure orange juice", "3.79", Category.BEVERAGES, 85, "\uD83C\uDF4A");
        seed("Sparkling Water 12pk", "Unflavored sparkling water", "5.99", Category.BEVERAGES, 100, "\uD83D\uDCA7");
        seed("Trail Mix 16oz", "Mixed nuts and dried fruit", "6.49", Category.SNACKS, 65, "\uD83E\uDD5C");
        seed("Dark Chocolate Bar", "72% cacao dark chocolate", "2.99", Category.SNACKS, 110, "\uD83C\uDF6B");
        seed("Frozen Pizza Margherita", "Stone-fired margherita pizza", "6.99", Category.FROZEN, 45, "\uD83C\uDF55");
        seed("Ice Cream Vanilla 1pt", "Premium vanilla ice cream", "4.99", Category.FROZEN, 50, "\uD83C\uDF66");
        seed("Pasta Penne 1lb", "Italian penne rigate", "1.99", Category.PANTRY, 200, "\uD83C\uDF5D");
        seed("Extra Virgin Olive Oil", "Cold-pressed EVOO 500ml", "8.99", Category.PANTRY, 75, "\uD83E\uDED2");
        seed("Paper Towels 6pk", "Absorbent paper towels", "9.99", Category.HOUSEHOLD, 60, "\uD83E\uDDFB");
    }

    private void seed(String name, String description, String price, Category category, int stock, String emoji) {
        Product p = new Product();
        p.setName(name);
        p.setDescription(description);
        p.setPrice(new BigDecimal(price));
        p.setCategory(category);
        p.setStockQuantity(stock);
        p.setEmoji(emoji);
        productRepository.save(p);
    }
}
