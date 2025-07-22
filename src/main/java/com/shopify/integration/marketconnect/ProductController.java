package com.shopify.integration.marketconnect;

import com.shopify.integration.marketconnect.service.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping("/push")
    public ResponseEntity<String> pushProducts() {
        try {
            productService.pushProductsToShopify();
            return ResponseEntity.ok("Products pushed successfully.");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("Failed to push products.");
        }
    }
}
