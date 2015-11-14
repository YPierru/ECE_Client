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
	private ArrayList<String> listRoomsName;
	private ArrayList<JF_roomTchat> listRoomsTchat;
	private ChatClient chatClient;

	public JF_main(String user, ChatClient cc){
		super("Client - "+user);
		username=user;
		chatClient=cc;
		listRoomsTchat= new ArrayList<>();
		
		//listRoomsName = new ArrayList<>();
		btnNewRoom = new JButton("Nouvelle salle");
		btnJoinRoom = new JButton("Rejoindre salle");
		btnDelRoom = new JButton("Supprimer salle");
		btnPVP = new JButton("Message privé avec un user");
		
		
		setLayout(new FlowLayout());
		setSize(800, 70);
		add(btnNewRoom);
		add(btnJoinRoom);
		add(btnDelRoom);
		add(btnPVP);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		btnNewRoom.addActionListener(new NewRoomAL(this));
		btnJoinRoom.addActionListener(new JoinRoomAL(this));
		//btnDelRoom.addActionListener(mActionListener);
		//btnPVP.addActionListener(mActionListener);
		
		setVisible(true);
	}
	
	public void setListRoomsNames(ArrayList<String> l){
		listRoomsName=l;
		System.out.println(listRoomsName);
	}
	
	public ArrayList<JF_roomTchat> getRoomsTchat(){
		return listRoomsTchat;
	}
	
	
	
	private class NewRoomAL implements ActionListener{
		
		private JF_main jfmain;

		public NewRoomAL(JF_main jfm) {
			jfmain=jfm;
		}
		
		@Override
		public void actionPerformed(ActionEvent e) {
			String roomName = JOptionPane.showInputDialog(jfmain,"Tapez le nom de la salle", null);
			chatClient.createRoom(roomName);
			listRoomsTchat.add(new JF_roomTchat(roomName, chatClient, username));
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
				chatClient.setCurrentRoom(roomName);
				listRoomsTchat.add(new JF_roomTchat(roomName, chatClient, username));
			}
		}
		
	}
	
}
