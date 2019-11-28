package client;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JTextArea;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.awt.event.ActionEvent;

public class ClientChat extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JTextField textFieldConnect;
	private JTextField textFieldSend;
	private JTextArea textAreaChat; 
	private Socket socket;
	private ServerThread serverThread;
	private PrintStream socOut;
	private String name;
	private String ipAddress;
	private String port;
	private JTextField textFieldAddress;
	private JTextField textFieldPort;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ClientChat frame = new ClientChat();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public ClientChat() {
		setTitle("Chat Client (socket-based)");
		setBounds(100, 100, 405, 454);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getContentPane().setLayout(null);
		
		textFieldAddress = new JTextField();
		textFieldAddress.setToolTipText("Ip Address");
		textFieldAddress.setBounds(10, 11, 175, 29);
		textFieldAddress.setColumns(10);
		getContentPane().add(textFieldAddress);
		
		textFieldPort = new JTextField();
		textFieldPort.setToolTipText("Port");
		textFieldPort.setBounds(195, 11, 89, 29);
		textFieldPort.setColumns(10);
		getContentPane().add(textFieldPort);
		
		JButton btnConfiguration = new JButton("Configure");
		btnConfiguration.setBounds(294, 10, 89, 30);
		getContentPane().add(btnConfiguration);
		
		textFieldConnect = new JTextField();
		textFieldConnect.setToolTipText("Username");
		textFieldConnect.setBounds(10, 51, 175, 29);
		getContentPane().add(textFieldConnect);
		textFieldConnect.setColumns(10);
		
		JButton btnConnect = new JButton("Connect");
		btnConnect.setBounds(195, 50, 89, 30);
		getContentPane().add(btnConnect);
		btnConnect.setEnabled(false);
		
		JButton btnDisconnect = new JButton("Disconnect");
		btnDisconnect.setBounds(294, 51, 89, 30);
		getContentPane().add(btnDisconnect);
		btnDisconnect.setEnabled(false);
		JScrollPane sp = new JScrollPane();
		sp.setBounds(10, 91, 373, 274);
		getContentPane().add(sp);
		
		textAreaChat = new JTextArea();
		sp.setViewportView(textAreaChat);
		textAreaChat.setEnabled(false);
		
		textFieldSend = new JTextField();
		textFieldSend.setToolTipText("Input your message here");
		textFieldSend.setBounds(10, 376, 274, 29);
		textFieldSend.setColumns(10);
		getContentPane().add(textFieldSend);
		textFieldSend.setEnabled(false);
		
		JButton btnSend = new JButton("Send");
		btnSend.setBounds(294, 376, 89, 30);
		getContentPane().add(btnSend);
		btnSend.setEnabled(false);
		
		btnConfiguration.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ipAddress = textFieldAddress.getText();
				port=textFieldPort.getText();
				textFieldConnect.setEnabled(true);
				btnConfiguration.setEnabled(false);
				textFieldAddress.setEnabled(false);
				textFieldPort.setEnabled(false);
				btnConnect.setEnabled(true);

			}
		});
		
		btnConnect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				name = textFieldConnect.getText();
				try {
					clientConnect(name);
					textFieldConnect.setEnabled(false);
					btnConnect.setEnabled(false);
					textAreaChat.setEnabled(true);
					textFieldSend.setEnabled(true);
					btnDisconnect.setEnabled(true);
					btnSend.setEnabled(true);
				} catch (UnknownHostException e1) {
		            e1.printStackTrace();
				} catch (IOException e1) {
		            e1.printStackTrace();
				}
			}
		});
		
		btnDisconnect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				clientDisconnect(name);
				textFieldConnect.setEnabled(true);
				btnConnect.setEnabled(true);
				textAreaChat.setEnabled(false);
				textFieldSend.setEnabled(false);
				btnDisconnect.setEnabled(false);
				btnSend.setEnabled(false);

			}
		});
		
		btnSend.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String message = textFieldSend.getText();
				send(message);
				textFieldSend.setText("");
			}
		});

	}
	
	private synchronized void clientConnect(String userName) throws UnknownHostException, IOException {
		socket=new Socket(ipAddress,Integer.parseInt(port));
		socOut = new PrintStream(socket.getOutputStream());
		serverThread=new ServerThread(socket,userName,this,socOut);
		serverThread.start();
		
	}
	
	public void onReceiveMessage(String message) {
		textAreaChat.append(message);
	}
	
	private synchronized void clientDisconnect(String name)  {
		if (socket != null) {
			socOut.println(name +" [ leaved the group chat ]"+ "\n");
			try {
				socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
	}
	
	public synchronized void send(String message) {
		if (socket != null) {
			if(message!=null) {
				serverThread.addNextMessage(message);
			}
			else {
				try {
					ServerThread.sleep(200);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
