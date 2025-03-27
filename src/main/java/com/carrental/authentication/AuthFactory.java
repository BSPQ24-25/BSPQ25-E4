package com.carrental.authentication;

public class AuthFactory {
	public IAPIAuthentication createGateway() {
		return new AuthGateway();
	}
}
