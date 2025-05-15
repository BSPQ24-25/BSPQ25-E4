package com.carrental.gateway;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PaymentGatewayTest {

    @Test
    void testPaymentGatewayImplementation() {
        PaymentGateway gateway = new CreditCardGateway();
        double amount = 50.0;
        String customerDetails = "customer@example.com";

        boolean result = gateway.processPayment(amount, customerDetails);

        assertTrue(result, "The payment gateway should process the payment and return true");
    }
}