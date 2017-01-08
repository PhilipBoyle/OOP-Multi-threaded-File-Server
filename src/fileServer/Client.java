package fileServer;

import java.io.DataInputStream;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

public class Client {
	
	private static Socket clientSock;
    private static String fileName;	
	private static PrintStream outStream;
	private static BufferedReader userInput;
	
	 public static void main(String[] args) throws IOException {
		 //manually sets up host and port, will implement XML parsing later
	        try {
	            clientSock = new Socket("127.0.0.1", 7777);
	            userInput = new BufferedReader(new InputStreamReader(System.in));
	        } catch (Exception e) {
	            System.err.println("Cannot connect to the server, try again later.");
	            System.exit(1);
	        }

	        outStream = new PrintStream(clientSock.getOutputStream());
	        
	        try {
	              switch (Integer.parseInt(chooseOption())) {
	            case 1:
	                outStream.println("1");
	                break;
	            case 2:
	                outStream.println("2");
	                break;
	            case 3:
	                outStream.println("2");
	                System.out.print("Enter file name: ");
	                fileName = userInput.readLine();
	                outStream.println(fileName);
	                downloadFile(fileName);
	                break;
	            case 4:
	                outStream.println("4");
	                break;
	        }
	        } catch (Exception e) {
	            System.err.println("not valid input");
	        }
	        
	        clientSock.close();
		 
	 }//main
	 
	 public static String chooseOption() throws IOException {
		 System.out.println("1. Connect to Server");
		 System.out.println("2. Print File Listing ");
		 System.out.println("3. Download File.");
		 System.out.println("4. Quit.");
		 System.out.print("\nType Option [1-4]>");
		 
		 return userInput.readLine();

	
	 }//chooseOption
	 
    public static void downloadFile(String fileName) {
        try {
            int bytesRead;
            InputStream clientIn = clientSock.getInputStream();

            DataInputStream clientDataInStream = new DataInputStream(clientIn);

            fileName = clientDataInStream.readUTF();
            OutputStream out = new FileOutputStream(("Received file :" + fileName));
            long fileSize = clientDataInStream.readLong();
            byte[] buffer = new byte[1024];
            while (fileSize > 0 && (bytesRead = clientDataInStream.read(buffer, 0, (int) Math.min(buffer.length, fileSize))) != -1) {
                out.write(buffer, 0, bytesRead);
                fileSize -= bytesRead;
            }
            
            out.close();
            clientIn.close();

            System.out.println("File "+fileName+" received from Server.");
        } catch (IOException x) {
        	System.out.println("ERROR encountered");
        }
    }//downloadFile

}//Client

