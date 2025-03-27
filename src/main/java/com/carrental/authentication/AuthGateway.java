package com.carrental.authentication;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class AuthGateway implements IAPIAuthentication {
	
	private final static int SERVER_PORT = 8080; //TODO Check the real port
	private String serverIP = "localhost";
	private static String DELIMITER = "#";
	
	@Override
	public boolean login(String email, String password) {
		String message = "login"+DELIMITER+email+DELIMITER+password;
		String auth = null;
		
		try (Socket socket = new Socket(serverIP, SERVER_PORT);
				DataInputStream in = new DataInputStream(socket.getInputStream());
				DataOutputStream out = new DataOutputStream(socket.getOutputStream())) {
			
			out.writeUTF(message);
			auth = in.readUTF();
		} catch (UnknownHostException e) {
			System.err.println("Auth. SocketClient: Socket error: " + e.getMessage());
			e.printStackTrace();
		} catch (EOFException e) {
			System.err.println("Auth. SocketClient: EOF error: " + e.getMessage());
			e.printStackTrace();
		} catch (IOException e) {
			System.err.println("Auth. SocketClient: IO error: " + e.getMessage());
			e.printStackTrace();	
		}
		
		if(auth.equals("true")) {
			return true;
		} else {
			return false;
		}
	}
	@Override
	public void register(String email, String password) {
		String message = "register"+DELIMITER+email+DELIMITER+password;
		
		try (Socket socket = new Socket(serverIP, SERVER_PORT);
				DataOutputStream out = new DataOutputStream(socket.getOutputStream())) {
			out.writeUTF(message);
			
		} catch (UnknownHostException e) {
			System.err.println("Auth. SocketClient: Socket error: " + e.getMessage());
			e.printStackTrace();
		} catch (EOFException e) {
			System.err.println("Auth. SocketClient: EOF error: " + e.getMessage());
			e.printStackTrace();
		} catch (IOException e) {
			System.err.println("Auth. SocketClient: IO error: " + e.getMessage());
			e.printStackTrace();	
		}
		
	}
	

}
