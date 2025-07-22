package com.shopify.integration.marketconnect.aggregate;

import com.shopify.integration.marketconnect.command.CreateProductCommand;
import com.shopify.integration.marketconnect.event.ProductCreatedEvent;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.spring.stereotype.Aggregate;

import static org.axonframework.modelling.command.AggregateLifecycle.apply;

@Aggregate
public class ProductAggregate {

    @AggregateIdentifier
    private String productId;
    private String title;
    private String description;
    private String price;

    public ProductAggregate() {}

    @CommandHandler
    public ProductAggregate(CreateProductCommand command) {
        apply(new ProductCreatedEvent(
                command.getProductId(),
                command.getTitle(),
                command.getDescription(),
                command.getPrice()
        ));
    }

    @EventSourcingHandler
    public void on(ProductCreatedEvent event) {
        this.productId = event.getProductId();
        this.title = event.getTitle();
        this.description = event.getDescription();
        this.price = event.getPrice();
    }
}
