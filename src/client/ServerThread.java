package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.LinkedList;

import server.SaveMessage;

public class ServerThread extends Thread {
    
	private Socket echoSocket;
	private String userName;
	private final LinkedList<String> messages;
	private boolean haveMessage=false;
	ClientChat clientChat;
	PrintStream socOut;
	
	public ServerThread(Socket socket, String userName,ClientChat clientChat, PrintStream socOut) {
		this.echoSocket=socket;
		this.userName=userName;
		this.messages=new LinkedList<String>();
		this.clientChat=clientChat;
		this.socOut=socOut;
	}
	
	public void addNextMessage(String message) {
		synchronized(messages) {
			haveMessage=true;
			messages.push(message);
		}	
		
	}

	
	public void run() {
		clientChat.onReceiveMessage("Bienvenue :"+userName+"\n");
		clientChat.onReceiveMessage("Serveur = " + echoSocket.getRemoteSocketAddress()+"\n");
		try {
			clientChat.onReceiveMessage(SaveMessage.readAllMessages());
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		socOut.println(userName +" [ joined the group chat ]");
		
		try {
    		PrintStream socOut = new PrintStream(echoSocket.getOutputStream());
    		InputStream socIn = echoSocket.getInputStream();
    		BufferedReader socInB = new BufferedReader(new InputStreamReader(socIn));
			
			while(!echoSocket.isClosed()) {
				if(socIn.available()>0) {
					String input= socInB.readLine();
					if(input!=null) {
						clientChat.onReceiveMessage(input+ "\n");
					}
				}
				if(haveMessage) {
					String nextSend="";
					synchronized(messages) {
						nextSend=messages.pop();
						haveMessage=!messages.isEmpty();
					}
					socOut.println(userName+" > "+nextSend);	
					socOut.flush();

				}
			}
		} catch (IOException e) {
			System.err.println("Couldn't get I/O for "
                    + "the connection to:");
			e.printStackTrace();
			System.exit(1);
		}
		
		
    	
    }

}

