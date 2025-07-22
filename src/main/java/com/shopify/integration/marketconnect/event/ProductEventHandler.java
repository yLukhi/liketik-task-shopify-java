package com.shopify.integration.marketconnect.event;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.*;

@Component
public class ProductEventHandler {

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Value("${shopify.api.url}")
    private String shopifyApiUrl;

    @Value("${shopify.api.token}")
    private String shopifyAccessToken;

    @org.axonframework.eventhandling.EventHandler
    public void on(ProductCreatedEvent event) {
        try {
            String existingProductId = findProductByTitle(event.getTitle());

            if (existingProductId != null) {
                // ✅ Update existing product
                updateProduct(existingProductId, event);
            } else {
                // ✅ Create new product
                createProduct(event);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String findProductByTitle(String title) throws Exception {
        String endpoint = shopifyApiUrl + "products.json?title=" + title;

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Shopify-Access-Token", shopifyAccessToken);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));

        ResponseEntity<String> response = restTemplate.exchange(
                endpoint, HttpMethod.GET, new HttpEntity<>(headers), String.class
        );

        JsonNode root = objectMapper.readTree(response.getBody());
        JsonNode products = root.get("products");

        if (products != null && products.size() > 0) {
            return products.get(0).get("id").asText();
        }
        return null;
    }

    private void updateProduct(String productId, ProductCreatedEvent event) {
        Map<String, Object> product = new HashMap<>();
        product.put("title", event.getTitle());
        product.put("body_html", event.getDescription());

        Map<String, Object> variant = new HashMap<>();
        variant.put("price", event.getPrice());
        product.put("variants", List.of(variant));

        Map<String, Object> payload = new HashMap<>();
        payload.put("product", product);

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Shopify-Access-Token", shopifyAccessToken);
        headers.setContentType(MediaType.APPLICATION_JSON);

        String url = shopifyApiUrl + "products/" + productId + ".json";

        ResponseEntity<String> response = restTemplate.exchange(
                url, HttpMethod.PUT, new HttpEntity<>(payload, headers), String.class
        );

        System.out.println("✅ Updated existing product: " + response.getStatusCode());
    }

    private void createProduct(ProductCreatedEvent event) {
        Map<String, Object> product = new HashMap<>();
        product.put("title", event.getTitle());
        product.put("body_html", event.getDescription());

        Map<String, Object> variant = new HashMap<>();
        variant.put("price", event.getPrice());
        variant.put("inventory_quantity", 10);

        product.put("variants", List.of(variant));

        Map<String, Object> payload = new HashMap<>();
        payload.put("product", product);

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Shopify-Access-Token", shopifyAccessToken);
        headers.setContentType(MediaType.APPLICATION_JSON);

        String url = shopifyApiUrl + "products.json";

        ResponseEntity<String> response = restTemplate.postForEntity(
                url, new HttpEntity<>(payload, headers), String.class
        );

        System.out.println("✅ Created new product: " + response.getStatusCode());
    }
}
