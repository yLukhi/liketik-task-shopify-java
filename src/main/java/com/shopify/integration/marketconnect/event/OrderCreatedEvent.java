package com.shopify.integration.marketconnect.event;

public class OrderCreatedEvent {
    private final String orderId;
    private final String customerName;
    private final String total;

    public OrderCreatedEvent(String orderId, String customerName, String total) {
        this.orderId = orderId;
        this.customerName = customerName;
        this.total = total;
    }

    public String getOrderId() {
        return orderId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public String getTotal() {
        return total;
    }
}
