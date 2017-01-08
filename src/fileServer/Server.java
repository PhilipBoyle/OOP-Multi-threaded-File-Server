package fileServer;

import java.io.IOException;

import java.net.ServerSocket;
import java.net.Socket; 
public class Server {
	
	private static ServerSocket sSocket;
	private static Socket cSocket = null; //cSocket = Client Socket
	
	public static void main(String[] args) throws IOException{
		
		try {
			//will get port number from serverinfo.xml later
			sSocket = new ServerSocket(7777);
			System.out.println("Server Started");
		}
	     catch (IOException e) {
	         System.err.println("Problem encountered");
	         System.exit(1);
	       }
		
		while(true){
			try {
			       cSocket = sSocket.accept();
			       System.out.println("Connection successful");
			       Thread t = new Thread(new Connection(cSocket));
	               t.start();
			}
			 catch (IOException e) {
			     System.err.println("Unable to connect");
			     System.exit(1);
			   }
		}//while

	}//main

}//Server
