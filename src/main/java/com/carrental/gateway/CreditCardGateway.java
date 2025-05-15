package com.carrental.gateway;

public class CreditCardGateway implements PaymentGateway {

    @Override
    public boolean processPayment(double amount, String customerDetails) {
        //System.out.println("Simulating credit card payment...");  NO PRINT!
        //System.out.println("Charged $" + amount + " to credit card of " + customerDetails);  NO PRINT!
        return true; // Simulación siempre exitosa
    }
}
