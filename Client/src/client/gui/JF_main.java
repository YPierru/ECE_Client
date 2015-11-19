package client.gui;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import client.ChatClient;
import client.commands.Commandes;

public class JF_main extends JFrame{
	
	private String username;
	private JButton btnNewRoom;
	private JButton btnJoinRoom;
	private JButton btnPVP;
	private ArrayList<String> listRoomsName;
	private ArrayList<String> listUsersName;
	private ArrayList<JF_roomTchat> listRoomsTchat;
	private ChatClient chatClient;

	public JF_main(String user, ChatClient cc){
		super("Client - "+user);
		username=user;
		chatClient=cc;
		listRoomsTchat= new ArrayList<>();
		
		btnNewRoom = new JButton("Nouvelle salle");
		btnJoinRoom = new JButton("Rejoindre salle");
		btnPVP = new JButton("Message privé avec un user");
		
		
		setLayout(new FlowLayout());
		setSize(500, 70);
		add(btnNewRoom);
		add(btnJoinRoom);
		add(btnPVP);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		btnNewRoom.addActionListener(new NewRoomAL(this));
		btnJoinRoom.addActionListener(new JoinRoomAL(this));
		btnPVP.addActionListener(new PvpAL(this));
		btnPVP.setEnabled(false);
		btnJoinRoom.setEnabled(false);
		
		setVisible(true);
	}
	
	public void setListRoomsNames(ArrayList<String> l){
		if(listRoomsName==null){
			listRoomsName = new ArrayList<>();
		}
		listRoomsName.clear();
		for(String rn : l){
			if(!rn.startsWith(Commandes.LABEL_ROOM_PVP)){
				listRoomsName.add(rn);
			}
		}
		System.out.println(listRoomsName);
		if(!btnJoinRoom.isEnabled() && listRoomsName.size()>0)
			btnJoinRoom.setEnabled(true);
	}
	
	public ArrayList<JF_roomTchat> getRoomsTchat(){
		return listRoomsTchat;
	}
	
	public void addRoomTchat(JF_roomTchat rt){
		listRoomsTchat.add(rt);
	}
	
	public void setListUserNames(ArrayList<String> l){
		if(listUsersName==null){
			listUsersName = new ArrayList<>();
		}
		listUsersName.clear();
		for(String un : l){
			if(!un.equals(username)){
				listUsersName.add(un);
			}
		}
		System.out.println(listUsersName);
		if(!btnPVP.isEnabled() && listUsersName.size()>0)
			btnPVP.setEnabled(true);
	}
	
	
		
	private class NewRoomAL implements ActionListener{
		
		private JF_main jfmain;

		public NewRoomAL(JF_main jfm) {
			jfmain=jfm;
		}
		
		@Override
		public void actionPerformed(ActionEvent e) {
			String roomName = JOptionPane.showInputDialog(jfmain, "Tapez le nom de la salle", "Création d'une salle", JOptionPane.QUESTION_MESSAGE);
			
			if(roomName!=null && !roomName.equals("") && !listRoomsName.contains(roomName)){
				listRoomsTchat.add(new JF_roomTchat(roomName, chatClient, username));
				chatClient.createRoom(roomName);
			}else{
				if(roomName!=null && !roomName.equals(""))
					JOptionPane.showMessageDialog(jfmain, "Mauvaise saisie de la room","Erreur", JOptionPane.ERROR_MESSAGE);
			}
		}
		
	}
	
	
	private class JoinRoomAL implements ActionListener{
		
		private JF_main jfmain;

		public JoinRoomAL(JF_main jfm) {
			jfmain=jfm;
		}
		
		@Override
		public void actionPerformed(ActionEvent e) {
			//System.out.println(listRoomsName);
			String[] arrayRoomNames = listRoomsName.toArray(new String[listRoomsName.size()]);
			String roomName = (String) JOptionPane.showInputDialog(jfmain, 
			        "Sélectionnez la salle à rejoindre",
			        "Rejoindre une salle",
			        JOptionPane.QUESTION_MESSAGE, 
			        null, 
			        arrayRoomNames, 
			        arrayRoomNames[0]);
					
			if(roomName!=null && !roomName.equals("") && !roomName.startsWith(Commandes.LABEL_ROOM_PVP)){
				listRoomsTchat.add(new JF_roomTchat(roomName, chatClient, username));
				chatClient.setCurrentRoom(roomName);
			}
		}
		
	}
	
	
		
	private class PvpAL implements ActionListener{
		
		private JF_main jfmain;

		public PvpAL(JF_main jfm) {
			jfmain=jfm;
		}
		
		@Override
		public void actionPerformed(ActionEvent e) {
			String[] arrayUserNames = listUsersName.toArray(new String[listUsersName.size()]);
			String userFromJOP = (String) JOptionPane.showInputDialog(jfmain, 
			        "Sélectionnez l'utilisateur à contacter",
			        "Liste des utilisateurs",
			        JOptionPane.QUESTION_MESSAGE, 
			        null, 
			        arrayUserNames, 
			        arrayUserNames[0]);
			
			if(userFromJOP!=null && !userFromJOP.equals("")){
				String room="MP "+userFromJOP+"-"+username;
				listRoomsTchat.add(new JF_roomTchat(room,chatClient,username));
				chatClient.createRoom(room);
			}
		}
		
	}
	
}
