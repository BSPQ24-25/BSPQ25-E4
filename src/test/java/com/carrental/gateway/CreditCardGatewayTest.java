package com.carrental.gateway;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CreditCardGatewayTest {

    @Test
    void testProcessPaymentReturnsTrue() {
        CreditCardGateway gateway = new CreditCardGateway();
        double amount = 100.0;
        String customerDetails = "test@example.com";

        boolean result = gateway.processPayment(amount, customerDetails);

        assertTrue(result, "CreditCardGateway should return true for any payment simulation");
    }
}