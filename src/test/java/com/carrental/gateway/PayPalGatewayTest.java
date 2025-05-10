package com.carrental.gateway;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PayPalGatewayTest {

    @Test
    void testPayPalPayment() {
        PaymentGateway gateway = new PayPalGateway();
        double amount = 100.0;
        String customerDetails = "customer@example.com";

        boolean result = gateway.processPayment(amount, customerDetails);

        assertTrue(result, "The PayPal payment gateway should process the payment and return true");
    }
}