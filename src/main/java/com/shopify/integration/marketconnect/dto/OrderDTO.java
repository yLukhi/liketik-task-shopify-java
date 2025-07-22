package com.shopify.integration.marketconnect.dto; // ✅ define package

public class OrderDTO {

    // ✅ Basic order fields
    public String id;
    public String orderNumber;
    public String customerId;
    public String totalPrice;
    public String financialStatus;
    public String fulfillmentStatus;

    // ✅ Shipping details
    public String shippingName;
    public String address1;
    public String city;
    public String zip;
    public String country;

    // ✅ Default constructor (used by Jackson)
    public OrderDTO() {}
}
