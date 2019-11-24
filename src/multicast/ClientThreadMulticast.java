package multicast;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.MulticastSocket;

public class ClientThreadMulticast extends Thread {
	private MulticastSocket multicastSocket;
	private String userName;
	private ClientChatMulticast clientChat;

	public ClientThreadMulticast(MulticastSocket multicastSocket,String userName,ClientChatMulticast clientChat) {
		this.multicastSocket = multicastSocket;
		this.userName=userName;
		this.clientChat=clientChat;
	}
	
	public void run() {
		try {
			clientChat.onReceiveMessage("Bienvenue :"+userName+"\n");
			clientChat.onReceiveMessage("Serveur = " + clientChat.getGroupAddr().toString() +":"+clientChat.getGroupPort()+"\n");
			
			while(true) {
				byte[] buf = new byte[1000]; 
				DatagramPacket recv = new 
				DatagramPacket(buf, buf.length);
				multicastSocket.receive(recv);
				String received = new String(recv.getData(), 0, recv.getLength());
				clientChat.onReceiveMessage(received);
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	
}
