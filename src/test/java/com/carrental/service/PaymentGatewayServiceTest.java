package com.carrental.service;

import com.carrental.models.Booking;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PaymentGatewayServiceTest {

    private PaymentGatewayService paymentGatewayService;

    @BeforeEach
    void setUp() {
        paymentGatewayService = new PaymentGatewayService();
    }

    @Test
    void processPayment_creditCard_returnsSimulatedResponse() {
        Booking booking = new Booking();
        booking.setPaymentMethod("credit card");

        String result = paymentGatewayService.processPayment(booking);

        assertEquals("Processed via Credit Card (simulated)", result);
    }

    @Test
    void processPayment_paypal_returnsSimulatedResponse() {
        Booking booking = new Booking();
        booking.setPaymentMethod("paypal");

        String result = paymentGatewayService.processPayment(booking);

        assertEquals("Processed via PayPal (simulated)", result);
    }

    @Test
    void processPayment_cash_returnsNoGatewayNeeded() {
        Booking booking = new Booking();
        booking.setPaymentMethod("cash");

        String result = paymentGatewayService.processPayment(booking);

        assertEquals("No gateway needed", result);
    }

    @Test
    void processPayment_unknownMethod_returnsNoGatewayNeeded() {
        Booking booking = new Booking();
        booking.setPaymentMethod("bitcoin");

        String result = paymentGatewayService.processPayment(booking);

        assertEquals("No gateway needed", result);
    }
}