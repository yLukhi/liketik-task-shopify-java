package com.shopify.integration.marketconnect;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class ShopifyService {

    @Value("${shopify.api.url}")
    private String shopifyApiUrl;

    @Value("${shopify.api.token}")
    private String shopifyAccessToken;

    private final RestTemplate restTemplate = new RestTemplate();

    public String fetchCustomers() {
        String url = shopifyApiUrl + "customers.json";

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Shopify-Access-Token", shopifyAccessToken);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));

        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new RuntimeException("Failed to fetch customers: " + response.getStatusCode());
        }


        return response.getBody();
    }
}
