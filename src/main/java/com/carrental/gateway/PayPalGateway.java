package com.carrental.gateway;

public class PayPalGateway implements PaymentGateway {

    @Override
    public boolean processPayment(double amount, String customerDetails) {
        //System.out.println("Simulating PayPal payment..."); NO PRINT!
       // System.out.println("Charged $" + amount + " via PayPal account of " + customerDetails);   NO PRINT!
        return true; // Simulación siempre exitosa
    }
}
