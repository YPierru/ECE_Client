package Client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

import com.sun.xml.internal.ws.resources.SenderMessages;

public class ChatClient {

	private Socket connection;
	private DataInputStream reader;
	private DataOutputStream writer;
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ChatClient chat = new ChatClient(args[0], Integer.parseInt(args[1]));
		@SuppressWarnings("resource")
		Scanner sc = new Scanner(System.in);
		@SuppressWarnings("unused")
		String raw = "";
		chat.open(args[2]);
		while(true){
			raw = sc.nextLine();
			if(raw.equals("m")){
				String to = sc.nextLine();
				String message = sc.nextLine(); 
				chat.sendMessage(message, to);
			}
			else if(raw.equals("cr")){
				String name = sc.nextLine();
				chat.createRoom(name);
			}
			else if(raw.equals("dr")){
				String name = sc.nextLine();
				chat.deleteRoom(name);
			}
		}
	}
	
	@SuppressWarnings("unused")
	public ChatClient(String hostname, int port){
		connection = null;
		try{
			connection = new Socket(hostname, port);		
			DataInputStream reader = new DataInputStream(connection.getInputStream());
			DataOutputStream writer = new DataOutputStream(connection.getOutputStream());
		}catch(IOException ioe){
			System.err.println("Connection failed");
			return;
		}
	}
	
	public void open(String userName){
		try{
			writer.writeUTF(userName);
			String reply = reader.readUTF();
			System.out.println("Server reply : "+reply);
			writer.flush();
		}
		catch(IOException ioe){
			System.err.println(ioe.getStackTrace().toString());
		}
	}
	
	public void close(){
		try{
			writer.close();
			reader.close();
			connection.close();
		}
		catch(IOException ioe){
			System.err.println(ioe.getStackTrace().toString());
		}
	}
	
	public void sendMessage(String message, String to){
		try{
			writer.writeUTF("m/;"+to+"/;"+message);
		}
		catch(IOException ioe){
			System.err.println(ioe.getStackTrace().toString());
		}
	}
	
	public void createRoom(String name){
		try{
			writer.writeUTF("cr/;"+name);
		}
		catch(IOException ioe){
			System.err.println(ioe.getStackTrace().toString());
		}
	}
	
	public void deleteRoom(String name){
		try{
			writer.writeUTF("dr/;"+name);
		}
		catch(IOException ioe){
			System.err.println(ioe.getStackTrace().toString());
		}
	}

}
