package Client;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

public class JF_main extends JFrame{
	
	private String username;
	private JButton btnNewRoom;
	private JButton btnJoinRoom;
	private JButton btnDelRoom;
	private JButton btnPVP;
	private JLabel labelTest;
	private ArrayList<String> listRoomsName;
	private ChatClient chatClient;

	public JF_main(String user, ChatClient cc){
		super("Client - "+user);
		username=user;
		chatClient=cc;
		
		listRoomsName = new ArrayList<>();
		btnNewRoom = new JButton("Nouvelle salle");
		btnJoinRoom = new JButton("Rejoindre salle");
		btnDelRoom = new JButton("Supprimer salle");
		btnPVP = new JButton("Message privé avec un user");
		
		labelTest=new JLabel("");
		
		setLayout(new FlowLayout());
		setSize(800, 70);
		add(btnNewRoom);
		add(btnJoinRoom);
		add(btnDelRoom);
		add(btnPVP);
		add(labelTest);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		btnNewRoom.addActionListener(new NewRoomAL(this));
		btnJoinRoom.addActionListener(new JoinRoomAL(this));
		//btnDelRoom.addActionListener(mActionListener);
		//btnPVP.addActionListener(mActionListener);
		
		setVisible(true);
	}
	
	
	private class NewRoomAL implements ActionListener{
		
		private JF_main jfmain;

		public NewRoomAL(JF_main jfm) {
			jfmain=jfm;
		}
		
		@Override
		public void actionPerformed(ActionEvent e) {
			String roomName = JOptionPane.showInputDialog(jfmain,"Tapez le nom de la salle", null);
			listRoomsName.add(roomName);
			labelTest.setText(roomName);
			chatClient.createRoom(roomName);
			new JF_roomTchat(roomName, chatClient);
		}
		
	}
	
	
	private class JoinRoomAL implements ActionListener{
		
		private JF_main jfmain;

		public JoinRoomAL(JF_main jfm) {
			jfmain=jfm;
		}
		
		@Override
		public void actionPerformed(ActionEvent e) {
			String roomName = JOptionPane.showInputDialog(jfmain,"Salle à rejoindre", null);
			
			
			if(listRoomsName.contains(roomName)){
				System.out.println("ici");
				chatClient.setCurrentRoom(roomName);
				new JF_roomTchat(roomName, chatClient);
			}
		}
		
	}
	
}
