package client;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.net.SocketTimeoutException;

public class RecieveMessageThread extends Thread {
	private final int BUFFER_SIZE = 130;
	private final int OFFSET = 0;
	
	private DatagramSocket socket;
	private static boolean running = true;
	private String answerFromClient;
	
	public RecieveMessageThread(DatagramSocket socket) {
		this.socket = socket;
	}

	public void run() {
		try {
			socket.setSoTimeout(500);
		} catch (SocketException e1) {
			System.out.println("Timeour konnte nicht gesetzt werden - beende");
			ChatClientMain.terminate();
		}
		
		while(running) {
			try {
				answerFromClient = "";
				
				byte[] recieveData = new byte[BUFFER_SIZE];
				DatagramPacket recievePacket = new DatagramPacket(recieveData, BUFFER_SIZE);
				
				socket.receive(recievePacket);
				answerFromClient = new String(recievePacket.getData(), OFFSET, recievePacket.getLength());
				
				ChatClientMain.addMessage(answerFromClient);
			} catch (SocketTimeoutException e) {
				//Gewollte Exception
			} catch (IOException e) {
				System.out.println("Nachricht konnte nicht empfangen werden");
			}
		}
	}
	
	public static void turnOff() {
		running = false;
	}

}
