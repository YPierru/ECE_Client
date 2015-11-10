package Client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ChatClient {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String hostname = "localhost"; //args[0];
		int port = 5555; //Integer.parseInt(args[1]);
		Socket connection = null;
		try{
			connection = new Socket(hostname, port);
		}catch(IOException ioe){
			System.err.println("Connection failed");
			return;
		}
		try{
			DataInputStream reader = new DataInputStream(connection.getInputStream());
			DataOutputStream writer = new DataOutputStream(connection.getOutputStream());
			writer.writeUTF("user");
			String reply = reader.readUTF();
			System.out.println("Server reply : "+reply);
			writer.flush();
			reader.close();
			writer.close();
			connection.close();
		}catch(IOException ios){
			
		}
	}

}
