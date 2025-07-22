package com.shopify.integration.marketconnect.command;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

public class CreateProductCommand {

    @TargetAggregateIdentifier
    private final String productId;
    private final String title;
    private final String description;
    private final String price;

    public CreateProductCommand(String productId, String title, String description, String price) {
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
