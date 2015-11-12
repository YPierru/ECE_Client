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
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ChatClient chat = new ChatClient(args[0], Integer.parseInt(args[1]));
		String raw = "";
		chat.open(args[2]);
		Scanner sc = new Scanner(System.in);
		while(!raw.equals("leave")){
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
		sc.close();
		chat.close();
	}
	

	public ChatClient(String hostname, int port){
		try{
			connection = new Socket(hostname, port);		
			reader = new DataInputStream(connection.getInputStream());
			writer = new DataOutputStream(connection.getOutputStream());
		}catch(IOException ioe){
			System.err.println("Connection failed");
		}
	}
	
	public void open(String userName){
		try{
			this.writer.writeUTF(userName);
			String reply = reader.readUTF();
			System.out.println("Server reply : "+reply);
			this.writer.flush();
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
			System.out.println("sended : "+"m/;"+to+"/;"+message);
			writer.writeUTF("m/;"+to+"/;"+message);
			writer.flush();
		}
		catch(IOException ioe){
			System.err.println(ioe.getStackTrace().toString());
		}
	}
	
	public void createRoom(String name){
		try{
			writer.writeUTF("cr/;"+name);
			writer.flush();
		}
		catch(IOException ioe){
			System.err.println(ioe.getStackTrace().toString());
		}
	}
	
	public void deleteRoom(String name){
		try{
			writer.writeUTF("dr/;"+name);
			writer.flush();
		}
		catch(IOException ioe){
			System.err.println(ioe.getStackTrace().toString());
		}
	}

}
