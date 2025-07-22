package com.shopify.integration.marketconnect.event;

public class ProductCreatedEvent {

    private final String productId;
    private final String title;
    private final String description;
    private final String price;

    public ProductCreatedEvent(String productId, String title, String description, String price) {
        this.productId = productId;
        this.title = title;
        this.description = description;
        this.price = price;
    }

    public String getProductId() {
        return productId;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getPrice() {
        return price;
    }
}
