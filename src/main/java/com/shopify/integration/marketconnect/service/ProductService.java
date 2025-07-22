package com.shopify.integration.marketconnect.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shopify.integration.marketconnect.command.CreateProductCommand;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.UUID;

@Service
public class ProductService {

    private final CommandGateway commandGateway;
    private final ObjectMapper objectMapper;

    @Value("classpath:products.json")
    private Resource productsFile;

    public ProductService(CommandGateway commandGateway) {
        this.commandGateway = commandGateway;
        this.objectMapper = new ObjectMapper();
    }

    public void pushProductsToShopify() throws IOException {
        JsonNode products = objectMapper.readTree(productsFile.getInputStream());

        for (JsonNode product : products) {
            String title = product.get("title").asText();
            String description = product.get("body_html").asText();
            String price = product.get("variants").get(0).get("price").asText();

            String productId = UUID.randomUUID().toString();

            CreateProductCommand command = new CreateProductCommand(
                    productId, title, description, price
            );

            commandGateway.send(command);
        }
    }
}
