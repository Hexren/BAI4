package client;

import java.io.*;
import java.net.*;
import java.util.*;


public class Client {

	public static int port;
	public static String host;

	private static Socket clientSocket;
	private static OutputStream outputStream;
	private static InputStream inputStream;

	private static boolean running = true;

	public static void main(String[] args) {
		Scanner scanFromUser = new Scanner(System.in);
		String inputFromUser = null;
		String inputFromServer = null;

		try {
			// Get IP Address from user
			System.out.println("Enter an IP-Address!");
			inputFromUser = scanFromUser.nextLine();
			host = inputFromUser;
			
			// Get port from user
			System.out.println("Enter a Port");
			inputFromUser = scanFromUser.nextLine();
			port = Integer.valueOf(inputFromUser);

			// Connect to socket
			clientSocket = new Socket(host, port);

			// Initialize streams
			outputStream = clientSocket.getOutputStream();
			inputStream = clientSocket.getInputStream();
			
			
			// Communication with Server
			while (running) {
				// Get input from User
				/*if (!clientSocket.isConnected()) {
					System.out.println("Your emperor is annoyed!");
					break;
				}*/

				System.out.println("Now enter greetings to our emperor");
				inputFromUser = scanFromUser.nextLine();

				// Send String to Server
				writeToServer(inputFromUser);

				// Get answer from Server
				inputFromServer = readFromServer();
				inputFromServer = inputFromServer.trim();
				
				if(inputFromServer.equals("BYE") 
						|| inputFromUser.indexOf("SHUTDOWN") == 0 && inputFromServer.equals("OK")) {
					running = false;
				}
			}
			
			if (clientSocket.isConnected()) {
				clientSocket.close();
			}

		} catch (ConnectException e) {
			System.out.println("Connection terminated");
		} catch (IOException e) {
			System.out.println("Connection failed: " + e.toString());
		}
	}
	
	private static void writeToServer(String request) throws IOException {
		byte[] byteArray = (request + "\n").getBytes("UTF-8");
		
		outputStream.write(byteArray, 0, byteArray.length);
	}
	
	private static String readFromServer() throws IOException {
		int read, i;
		String request;
		byte[] byteArray = new byte[256];					//Begrenzt die mögliche Nachrichtenlänge auf ein Array der Länge 256 
		
		if(inputStream.available() > 1) {
			
		}
		
		i = 0;
		do {
			read = inputStream.read();
			byteArray[i] = (byte) read;
			i++;
		} while(read != 10 && read != -1 && i < 256);
		
		if(read == -1 && i == 1) {
			throw new ConnectException();
		}
		
		if(i > 255) {
			throw new IllegalArgumentException();
		}
		
		request = new String(byteArray, "UTF-8");

		System.out.println("Server answer: " + request);
		return request;
	}
}
