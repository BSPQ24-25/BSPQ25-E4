package com.carrental.service;
import com.carrental.models.Booking;
import org.springframework.stereotype.Service;
@Service
public class PaymentGatewayService {

    public String processPayment(Booking booking) {
        switch (booking.getPaymentMethod().toLowerCase()) {
            case "credit card":
                return simulateCreditCard(booking);
            case "paypal":
                return simulatePayPal(booking);
            default:
                return "No gateway needed";
        }
    }

    private String simulateCreditCard(Booking booking) {
        // Simulación ficticia
        return "Processed via Credit Card (simulated)";
    }

    private String simulatePayPal(Booking booking) {
        // Simulación ficticia
        return "Processed via PayPal (simulated)";
    }
}