package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.LinkedList;

public class ServerThread extends Thread {
    
	private Socket echoSocket;
	private String userName;
	private final LinkedList<String> messages;
	private boolean haveMessage=false;
	
	public ServerThread(Socket socket, String userName) {
		this.echoSocket=socket;
		this.userName=userName;
		messages=new LinkedList<String>();
	}
	
	public void addNextMessage(String message) {
		synchronized(messages) {
			haveMessage=true;
			messages.push(message);
		}
	}

	
	public void run() {
    	System.out.println("Bienvenue :"+userName);
    	System.out.println("Serveur = " + echoSocket.getRemoteSocketAddress());
		
		
		try {
    		PrintStream socOut = new PrintStream(echoSocket.getOutputStream());
    		InputStream socIn = echoSocket.getInputStream();
    		BufferedReader socInB = new BufferedReader(new InputStreamReader(socIn));
    		BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
			
			while(!echoSocket.isClosed()) {
				if(socIn.available()>0) {
					String input= socInB.readLine();
					if(input!=null) {
						System.out.println(input);
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

