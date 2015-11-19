package client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

import client.commands.Commandes;
import client.gui.JF_main;
import client.gui.JF_roomTchat;

public class ChatClient {

	private Socket connection;
	private ObjectInputStream reader;
	private ObjectOutputStream writer;
	private String username;
	private JF_main mainFrame;
	
	/**
	 * Le main se charge uniquement de démarrer le client
	 * @param args
	 */
	public static void main(String[] args) {
		new ChatClient(args[0], Integer.parseInt(args[1]), args[2]);
	}
	

	/**
	 * Constructeur : ouvre la socket, les stream de lecture/écriture, et lance le protocole d'échange
	 * @param hostname
	 * @param port
	 * @param user
	 */
	public ChatClient(String hostname, int port, String user){
		username=user;
		try{
			mainFrame=new JF_main(user,this);
			connection = new Socket(hostname, port);
			writer = new ObjectOutputStream(connection.getOutputStream());
			reader = new ObjectInputStream(connection.getInputStream());
		}catch(IOException ioe){
			System.err.println("Connection failed");
		}
		
		try {
			talkingToServer();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * Protocole d'échange avec le serveur
	 * @throws IOException
	 */
	private void talkingToServer() throws IOException{
		
		//Pour que le serveur nous identifie, on lui envoi notre pseudo
		writer.writeUTF(username);
		writer.flush();		
		
		//Création d'un Thread pour écouter les messages entrants, et les afficher
		Thread listenReader=new Thread(new Runnable() {
			
			@Override
			public void run() {
				try {
					while(true){
						Object o = reader.readObject();

						if(o instanceof String){
							String msg = (String)o;
							if(msg.equals(Commandes.RECEIVE_LIST_ROOMS)){
								mainFrame.setListRoomsNames((ArrayList<String>)reader.readObject());
								
							}else if(msg.equals(Commandes.RECEIVE_LIST_USERS)){
								mainFrame.setListUserNames((ArrayList<String>)reader.readObject());
								
							}else if(msg.startsWith(Commandes.RECEIVE_OPEN_PVP)){
								String roomName=msg.replace(Commandes.RECEIVE_OPEN_PVP, "");
								String tmp = roomName.replace(Commandes.LABEL_ROOM_PVP, "");
								String user=tmp.substring(0, tmp.indexOf("-"));
								mainFrame.addRoomTchat(new JF_roomTchat(roomName, getThis(), user));
							}else{
								//On extrait le nom de la salle
								String room = msg.substring(msg.indexOf("[")+1, msg.indexOf("]"));
								msg = msg.replace("["+room+"]", "");
								//On affiche le message dans la bonne salle
								for(JF_roomTchat tchat : mainFrame.getRoomsTchat()){
									if(tchat.getRoomName().equals(room)){
										tchat.displayNewMessage(msg);
									}
								}
							}
						}
					}
				} catch (IOException e) {
					e.printStackTrace();
				}catch (ClassNotFoundException e1) {
					e1.printStackTrace();
				}
			}
		});
		listenReader.start();
		
	}
	
	private ChatClient getThis(){
		return this;
	}
	
	private void close(){
		try{
			writer.close();
			reader.close();
			connection.close();
		}
		catch(IOException ioe){
			System.err.println(ioe.getStackTrace().toString());
		}
	}
	
	public void sendMessage(String message){
		try{
			writer.writeUTF(Commandes.SEND_MESSAGE+message);
			writer.flush();
		}
		catch(IOException ioe){
			System.err.println(ioe.getStackTrace().toString());
		}
	}
	
	//On envoi la commande de création d'une room
	public void createRoom(String name){
		try{
			writer.writeUTF(Commandes.SEND_NEW_ROOM+name);
			writer.flush();
		}
		catch(IOException ioe){
			System.err.println(ioe.getStackTrace().toString());
		}
	}
	
	//Envoi la commande de modification de la room courante
	public void setCurrentRoom(String name){
		try{
			writer.writeUTF(Commandes.SEND_SET_ROOM+name);
			writer.flush();
		}
		catch(IOException ioe){
			System.err.println(ioe.getStackTrace().toString());
		}
	}
	
}
