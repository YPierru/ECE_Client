package Client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

public class ChatClient {

	private Socket connection;
	//private DataInputStream reader;
	//private DataOutputStream writer;
	private ObjectInputStream reader;
	private ObjectOutputStream writer;
	private String username;
	private Scanner sc;
	private JF_main mainFrame;
	
	/**
	 * Le main se charge uniquement de démarrer le client
	 * @param args
	 */
	public static void main(String[] args) {
		//new JF_main("yan kcdq");
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
		sc = new Scanner(System.in);
		try{
			connection = new Socket(hostname, port);
			mainFrame=new JF_main(user,this);
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
	 * Il y avait une fonction open, elle n'est plus nécessaire. L'envoi de l'username est dans talkingToServer()
	 */
	
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
						if(o instanceof ArrayList<?>){
							mainFrame.setListRoomsNames((ArrayList<String>)o);
						}else{
							String msg = (String)o;
							String room = msg.substring(msg.indexOf("[")+1, msg.indexOf("]"));
							msg = msg.replace("["+room+"]", "");
							for(JF_roomTchat tchat : mainFrame.getRoomsTchat()){
								if(tchat.getRoomName().equals(room)){
									tchat.displayNewMessage(msg);
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
	
	public void sendMessage(String message, String roomName){
		try{
			writer.writeUTF(message);
			writer.flush();
		}
		catch(IOException ioe){
			System.err.println(ioe.getStackTrace().toString());
		}
	}
	
	public void createRoom(String name){
		try{
			writer.writeUTF("[NewRoom]"+name);
			writer.flush();
		}
		catch(IOException ioe){
			System.err.println(ioe.getStackTrace().toString());
		}
	}
	
	public void setCurrentRoom(String name){
		try{
			writer.writeUTF("[SetCurrentRoom]"+name);
			writer.flush();
		}
		catch(IOException ioe){
			System.err.println(ioe.getStackTrace().toString());
		}
	}
	
	private void deleteRoom(String name){
		try{
			writer.writeUTF("dr/;"+name);
			writer.flush();
		}
		catch(IOException ioe){
			System.err.println(ioe.getStackTrace().toString());
		}
	}
	
}
