package client;

import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

import data.ChatUser;
import data.Log;

import org.eclipse.swt.widgets.*;

public class main {
	private static final int MESSAGEMAX = 20;
	private static boolean running = true;
	
	private static String userName = "";
	private static String hostAddresse = "";
	private static ChatUser user;
	
	private static volatile List<ChatUser> userList = new ArrayList<ChatUser>();
	private static volatile List<String> recievedMessageList = new ArrayList<String>();
	private static volatile DatagramSocket udpSocket = null;
	
	static ServerCommunicationThread serverCommunicationThread = null;
	static RecieveMessageThread recieveMessageThread = null;
	
	private static Log log = new Log("ClientMainThread");
	
	public static void main(String[] args) {
		System.out.println("ChatClientMain");
		hostAddresse = javax.swing.JOptionPane.showInputDialog("Bitte geben sie die Hostaddresse des ChatServers an");
		userName = javax.swing.JOptionPane.showInputDialog("Bitte geben sie ihren Chat-Namen ein");
		
		user = new ChatUser(userName, hostAddresse);
		
		//DatagramSocket udpSocket = null;
		try {
			udpSocket = new DatagramSocket(50001);
		} catch (SocketException e) {
			log.newWarning("UDP-Socket konnte nicht erstellt werden - Beende");
			return;
		}
		
		serverCommunicationThread = new ServerCommunicationThread(userName, hostAddresse);
		recieveMessageThread = new RecieveMessageThread(udpSocket);
		
		serverCommunicationThread.start();
		recieveMessageThread.start();

		clientGUI.main();
		
		while(running) {
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		
	}

	public static void refreshUserList(List<ChatUser> users) {
		userList = new ArrayList<ChatUser>(users);
	}
	
	public static void addMessage(String m) {
		clientGUI.incomingMessage(m);
		recievedMessageList.add(m);
		
		log.newInfo("Got Message: " + m);
	}
	
	public static void sendMessage(String m) {
		(new SendMessageThread(user, udpSocket, m)).start();
	}
	
	public static List<ChatUser> getUserList() {
		return userList;
	}
	
	public static void terminate() {
		serverCommunicationThread.turnOff();
		recieveMessageThread.turnOff();
	}
}
