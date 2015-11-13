package Client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class ChatClient {

	private Socket connection;
	private DataInputStream reader;
	private DataOutputStream writer;
	private String username;
	private Scanner sc;
	
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
			new JF_main(user);
			reader = new DataInputStream(connection.getInputStream());
			writer = new DataOutputStream(connection.getOutputStream());
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
						System.out.println(reader.readUTF());
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		listenReader.start();
		
		
		//Pour l'instant j'ai simplifié cette méthode, j'ai préféré me concentrer d'abord sur l'envoi/réception de messages simples.
		String raw="";
		while(!raw.equals("leave")){
			raw = sc.nextLine();
			/*if(raw.equals("m")){
				String to = sc.nextLine();
				String message = sc.nextLine(); 
				sendMessage(message, to);
			}
			else if(raw.equals("cr")){
				String name = sc.nextLine();
				createRoom(name);
			}
			else if(raw.equals("dr")){
				String name = sc.nextLine();
				deleteRoom(name);
			}*/
			sendMessage(raw, "");
		}
		
		close();
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
	
	private void sendMessage(String message, String to){
		try{
			//writer.writeUTF("m/;"+to+"/;"+message);
			writer.writeUTF(message);
			writer.flush();
		}
		catch(IOException ioe){
			System.err.println(ioe.getStackTrace().toString());
		}
	}
	
	private void createRoom(String name){
		try{
			writer.writeUTF("cr/;"+name);
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
