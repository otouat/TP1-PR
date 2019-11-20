/***
 * ClientThread
 * Example of a TCP server
 * Date: 14/12/08
 * Authors:
 */

package server;

import java.io.*;
import java.net.*;

public class ClientThread
	extends Thread {
	
	private Socket clientSocket;
	private EchoServerMultiThreaded echoServerMultiThreaded;
	private BufferedReader socIn;
	private PrintStream socOut; 
	
	ClientThread(EchoServerMultiThreaded echoServerMultiThreaded, Socket s) {
		this.clientSocket = s;
		this.echoServerMultiThreaded=echoServerMultiThreaded;
	}

	
	public void run() {
		try {

			socIn = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			socOut = new PrintStream(clientSocket.getOutputStream());

			while (!clientSocket.isClosed()) {
				String line = socIn.readLine();
				System.out.println(line);
				if (line != null) {
					for (ClientThread client : echoServerMultiThreaded.getClients()) {
						PrintStream clientOut = client.getSocOut();
						if (clientOut != null) {
							clientOut.println(line);
						}

					}
				}

			}
		} catch (Exception e) {
			System.err.println("Error in EchoServer:" + e);
		}
	}

	public PrintStream getSocOut() {
		return socOut;
	}
  
  }

  