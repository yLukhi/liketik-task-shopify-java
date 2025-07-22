package com.shopify.integration.marketconnect;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shopify.integration.marketconnect.command.CreateOrderCommand;
import com.shopify.integration.marketconnect.webhook.WebhookEventStore;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/webhooks")
public class WebhookController {

    private final CommandGateway commandGateway;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final WebhookEventStore eventStore;

    public WebhookController(CommandGateway commandGateway, WebhookEventStore eventStore) {
        this.commandGateway = commandGateway;
        this.eventStore = eventStore;
    }

    @PostMapping("/orders")
    public ResponseEntity<String> receiveOrder(@RequestBody String payload) {
        try {
            // Save payload for debugging
            eventStore.addEvent(payload);
            System.out.println("🔔 Webhook Received: " + payload);

            JsonNode order = objectMapper.readTree(payload);
            String orderId = order.get("id").asText();
            String customerName = order.get("shipping_address").get("name").asText();
            String totalPrice = order.get("total_price").asText();

            CreateOrderCommand command = new CreateOrderCommand(orderId, customerName, totalPrice);
            commandGateway.send(command);

            return ResponseEntity.ok("Webhook received and processed.");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("Error handling webhook.");
        }
    }

    // ✅ New endpoint to view recent webhook events
    @GetMapping("/received")
    public ResponseEntity<List<String>> getRecentWebhooks() {
        return ResponseEntity.ok(eventStore.getRecentEvents());
    }
}
