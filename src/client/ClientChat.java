package client;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JTextArea;
import java.awt.event.ActionListener;
import java.io.IOException;
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
		btnConnect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String name = textFieldConnect.getText();
				System.out.println(name);
				try {
					clientConnect(name);
				} catch (UnknownHostException e1) {
		            e1.printStackTrace();
				} catch (IOException e1) {
		            e1.printStackTrace();
				}
			}
		});
		btnConnect.setBounds(195, 10, 89, 30);
		getContentPane().add(btnConnect);
		
		JButton btnDisconnect = new JButton("Disconnect");
		btnDisconnect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String name = textFieldConnect.getText();
				clientDisconnect(name);

			}
		});
		btnDisconnect.setBounds(294, 10, 89, 30);
		getContentPane().add(btnDisconnect);
		
		textAreaChat = new JTextArea();
		JScrollPane sp = new JScrollPane(textAreaChat);
		sp.setBounds(10, 51, 373, 274);
		getContentPane().add(sp);
		
		textFieldSend = new JTextField();
		textFieldSend.setColumns(10);
		textFieldSend.setBounds(10, 336, 274, 29);
		getContentPane().add(textFieldSend);
		
		JButton btnSend = new JButton("Send");
		btnSend.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String message = textFieldSend.getText();
				System.out.println(message);
				send(message);
				textFieldSend.setText("");
			}
		});
		btnSend.setBounds(294, 336, 89, 30);
		getContentPane().add(btnSend);

	}
	
	private synchronized void clientConnect(String userName) throws UnknownHostException, IOException {
		socket=new Socket("localhost",1234);
		serverThread=new ServerThread(socket,userName,this);
		serverThread.start();
		serverThread.addNextMessage(userName+" joined the group chat");
	}
	
	public void onReceiveMessage(String message) {
		textAreaChat.append(message + "\n");
	}
	
	private synchronized void clientDisconnect(String userName)  {
		if (socket != null) {
			try {
				serverThread.addNextMessage(userName+"leaved the group chat");
				socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public synchronized void send(String message) {
		if (socket != null) {
			serverThread.addNextMessage(message);
		}
	}
}
