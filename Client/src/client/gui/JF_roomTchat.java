package client.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import client.ChatClient;

public class JF_roomTchat extends JFrame{
	
	private static final long serialVersionUID = 1L;
	private JTextArea messagesArea;
	private JButton sendButton;
	private JTextField message;
	
	private String roomName;
	private ChatClient chatClient;
	private String username;
	
	public JF_roomTchat(String name, ChatClient cc,String user)
	{

		super(user+" Room : "+name);
		chatClient=cc;
		roomName=name;
		username=user;
		
		addWindowFocusListener(new MyWindowFocusListener(name));

		JPanel panelFields = new JPanel();
		panelFields.setLayout(new BoxLayout(panelFields, BoxLayout.X_AXIS));

		JPanel panelFields2 = new JPanel();
		panelFields2.setLayout(new BoxLayout(panelFields2, BoxLayout.X_AXIS));

		// here we will have the text messages screen
		messagesArea = new JTextArea();
		messagesArea.setColumns(30);
		messagesArea.setRows(10);
		messagesArea.setEditable(false);
		
		sendButton = new JButton("Envoyer");
		sendButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				sendMessage();
			}
		});

		// the box where the user enters the text (EditText is called in
		// Android)
		message = new JTextField();
		message.setSize(200, 20);
		message.setScrollOffset(1);
		message.addKeyListener(new MyKeyListener());

		// add the buttons and the text fields to the panel
		JScrollPane sp = new JScrollPane(messagesArea);
		panelFields.add(sp);

		panelFields2.add(message);
		panelFields2.add(sendButton);

		getContentPane().add(panelFields);
		getContentPane().add(panelFields2);

		getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

		setSize(300, 170);
		setVisible(true);

		setLocationRelativeTo(null); // *** center the app *** 
		pack();
	}
	
	private void sendMessage(){
		
		String msg =message.getText();
		
		if(msg.toString().trim().equals(""))
		{
			message.setText("");
			message.requestFocus();
			return;
		}
		// add message to the message area
		messagesArea.append("\n"+"<"+username+">"+msg);
		// send the message to the client
		// clear text
		chatClient.sendMessage(msg);
		message.setText("");
	}
	
	public String getRoomName(){
		return roomName;
	}
	
	public void displayNewMessage(String msg){
		messagesArea.append("\n"+msg);
	}
	
	
	private class MyWindowFocusListener implements WindowFocusListener{
		
		private String roomName;
		
		public MyWindowFocusListener(String rn){
			roomName=rn;
		}

		@Override
		public void windowGainedFocus(WindowEvent e) {
			chatClient.setCurrentRoom(roomName);
		}

		@Override
		public void windowLostFocus(WindowEvent e) {
			// TODO Auto-generated method stub
			
		}
	}
	
	private class MyKeyListener implements KeyListener{

		@Override
		public void keyTyped(KeyEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void keyPressed(KeyEvent e) {
			if(e.getKeyCode() == KeyEvent.VK_ENTER){
			      sendMessage();
			}
		}

		@Override
		public void keyReleased(KeyEvent e) {
			// TODO Auto-generated method stub
			
		}
		
	}

}
