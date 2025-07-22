package com.shopify.integration.marketconnect.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shopify.integration.marketconnect.dto.OrderDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
public class ShopifyOrderService {

    @Value("${shopify.api.url}")
    private String shopifyApiUrl;

    @Value("${shopify.api.token}")
    private String shopifyAccessToken;

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public List<OrderDTO> fetchOrders() throws Exception {
        String url = shopifyApiUrl + "orders.json?limit=50";

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Shopify-Access-Token", shopifyAccessToken);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));

        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

        List<OrderDTO> ordersList = new ArrayList<>();
        JsonNode root = objectMapper.readTree(response.getBody()).get("orders");

        for (JsonNode order : root) {
            OrderDTO dto = new OrderDTO();
            dto.id = order.get("id").asText();
            dto.orderNumber = order.get("order_number").asText();
            dto.customerId = order.hasNonNull("customer") ? order.get("customer").get("id").asText() : "N/A";
            dto.totalPrice = order.get("total_price").asText();
            dto.financialStatus = order.get("financial_status").asText();
            dto.fulfillmentStatus = order.has("fulfillment_status") && !order.get("fulfillment_status").isNull()
                    ? order.get("fulfillment_status").asText() : "unfulfilled";

            if (order.hasNonNull("shipping_address")) {
                JsonNode ship = order.get("shipping_address");
                dto.shippingName = ship.get("name").asText();
                dto.address1 = ship.get("address1").asText();
                dto.city = ship.get("city").asText();
                dto.zip = ship.get("zip").asText();
                dto.country = ship.get("country").asText();
            }

            ordersList.add(dto);
        }

        return ordersList;
    }
}
