package Client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ChatClient {

	private Socket connection;
	private DataInputStream reader;
	private DataOutputStream writer;
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ChatClient chat = new ChatClient(args[0], Integer.parseInt(args[1]));
		chat.open(args[2]);
		chat.close();

	}
	
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
			writer.writeUTF("cr/;"+name);
		}
		catch(IOException ioe){
			System.err.println(ioe.getStackTrace().toString());
		}
	}

}
