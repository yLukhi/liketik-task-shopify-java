package com.shopify.integration.marketconnect;

import com.shopify.integration.marketconnect.dto.OrderDTO;
import com.shopify.integration.marketconnect.service.ShopifyOrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class ShopifyOrderController {

    private final ShopifyOrderService orderService;

    public ShopifyOrderController(ShopifyOrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    public ResponseEntity<List<OrderDTO>> getOrders() {
        try {
            List<OrderDTO> orders = orderService.fetchOrders();
            return ResponseEntity.ok(orders);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
