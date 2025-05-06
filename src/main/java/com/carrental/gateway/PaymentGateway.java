package com.carrental.gateway;

public interface PaymentGateway {
	boolean processPayment(double amount, String customerDetails);
}
