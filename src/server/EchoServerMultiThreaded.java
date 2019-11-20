/***
 * EchoServer
 * Example of a TCP server
 * Date: 10/01/04
 * Authors:
 */

package server;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

public class EchoServerMultiThreaded  {
  
	private static final int portNumber=1234;
	private int port;
	private static List<ClientThread> clients;
	
	/**
  	* main method
  	* 
  	**/
	
    public static void main(String args[]){ 
    	EchoServerMultiThreaded server=new EchoServerMultiThreaded(portNumber);
    	ServerSocket listenSocket;
         
    	try {
    		listenSocket = new ServerSocket(server.port); //port
    		System.out.println("Server ready..."); 
        	while (true) {
    			try {
    				Socket clientSocket = listenSocket.accept();
    				System.out.println("Connexion from:" + clientSocket.getInetAddress());
    				ClientThread ct = new ClientThread(server,clientSocket);
    				ct.start();
    				clients.add(ct);
    			} catch (IOException e) {
    				System.out.println("Socket accept failed on :"+server.port);
    			}
    		}
            } catch (Exception e) {
                System.err.println("Could not listen to the port:" + server.port);
                System.exit(1);
            }
    }
    
    public EchoServerMultiThreaded(int port) {
    	this.port=port;
    	EchoServerMultiThreaded.clients=new ArrayList<ClientThread>();
    }
    
    public List<ClientThread> getClients(){
    	return clients;
    }
    
    
  }