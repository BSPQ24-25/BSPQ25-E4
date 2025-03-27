package com.carrental.authentication;

public interface IAPIAuthentication {
	
	boolean login(String email, String password);
	void register(String email, String password);
}
