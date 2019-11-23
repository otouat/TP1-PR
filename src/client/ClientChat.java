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
		setBounds(100, 100, 405, 412);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getContentPane().setLayout(null);
		
		textFieldConnect = new JTextField();
		textFieldConnect.setBounds(10, 11, 175, 29);
		getContentPane().add(textFieldConnect);
		textFieldConnect.setColumns(10);
		
		JButton btnConnect = new JButton("Connect");
		btnConnect.setBounds(195, 10, 89, 30);
		getContentPane().add(btnConnect);
		
		JButton btnDisconnect = new JButton("Disconnect");
		btnDisconnect.setBounds(294, 10, 89, 30);
		getContentPane().add(btnDisconnect);
		btnDisconnect.setEnabled(false);
		
		textAreaChat = new JTextArea();
		JScrollPane sp = new JScrollPane(textAreaChat);
		sp.setBounds(10, 51, 373, 274);
		getContentPane().add(sp);
		textAreaChat.setEnabled(false);
		
		textFieldSend = new JTextField();
		textFieldSend.setColumns(10);
		textFieldSend.setBounds(10, 336, 274, 29);
		getContentPane().add(textFieldSend);
		textFieldSend.setEnabled(false);
		
		JButton btnSend = new JButton("Send");
		btnSend.setBounds(294, 336, 89, 30);
		getContentPane().add(btnSend);
		btnSend.setEnabled(false);
		
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
				System.out.println(message);
				send(message);
				textFieldSend.setText("");
			}
		});

	}
	
	private synchronized void clientConnect(String userName) throws UnknownHostException, IOException {
		socket=new Socket("localhost",1234);
		socOut = new PrintStream(socket.getOutputStream());
		serverThread=new ServerThread(socket,userName,this,socOut);
		serverThread.start();
		socOut.println(name +" [ joined the group chat ]");
	}
	
	public void onReceiveMessage(String message) {
		textAreaChat.append(message + "\n");
	}
	
	private synchronized void clientDisconnect(String name)  {
		if (socket != null) {
			socOut.println(name +" [ leaved the group chat ]");
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
