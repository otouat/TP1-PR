/***
 * EchoClient
 * Example of a TCP client 
 * Date: 10/01/04
 * Authors:
 */
package client;

import java.io.*;
import java.net.*;



public class EchoClient {

 
  /**
  *  main method
  *  accepts a connection, receives a message from client then sends an echo to the client
  **/
	
	private static final String hostName="localhost";
	private static final int portNumber=1234;
	private String userName;
	private String host;
	private int port;
	
	
    
    public static void main(String[] args) throws IOException, InterruptedException {
    	String read;
    	BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
    	System.out.println("Please input username :");
    	read=stdIn.readLine();
    	
    	EchoClient client=new EchoClient(read,hostName,portNumber);
    	client.startClient();
    	
    }



	private EchoClient(String userName, String host, int port) {
		this.userName = userName;
		this.host = host;
		this.port = port;
	}
	
	private void startClient() throws InterruptedException {
		try {
    		BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
			Socket socket=new Socket(host,port);
			ServerThread serverThread=new ServerThread(socket,userName);
			serverThread.start();
			while(serverThread.isAlive()) {
				String input= stdIn.readLine();
				if(input!=null) {
					serverThread.addNextMessage(input);
				}
				else {
					ServerThread.sleep(200);
				}
				
			}
		}
		catch(IOException e) {
			System.err.println("Connection error!");
            e.printStackTrace();
		}
		
	}
    
    
    
    
    
}


