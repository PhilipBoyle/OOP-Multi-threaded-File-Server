package fileServer;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;

public class Connection implements Runnable{
	
    private Socket cSocket;
    private BufferedReader userInput = null;
    
    public Connection(Socket client){
    	this.cSocket = client;
    }
    
	@Override
	public void run() {
		
		try{
			userInput = new BufferedReader(new InputStreamReader(cSocket.getInputStream()));
			//opetionChosen = which ever option the user has selected
			String optionChosen;
			while ((optionChosen = userInput.readLine()) != "4"){
				//connection is established automatically, will add user control later
				switch(optionChosen){
					case "1":
						System.out.println("Connection to server already established.");
						break;
					case "2":
						listFiles();
						break;
					case "3":
						String fileSent;
						while((fileSent = userInput.readLine()) != null){
							downloadFile(fileSent);
						}// 3
					case "4":
						System.out.println("Thanks for using this application!");
						break;
					default:
						System.out.println("Please enter a correct number. (Suggestion: 3)");
						break;
					
						
				}//switch
				userInput.close();
				break;
				
			}//while
		}
		catch (IOException x){
			System.out.println("ERROR encountered");
		}
		
	}//run
	
	private static String[] listFiles() {
	    String list[];
	    File folder = new File("ServerFiles");
	    File[] fileList = folder.listFiles();



	    list = new String[fileList.length];
	    for (int i = 0; i < fileList.length; i++) {
	        if (fileList[i].isFile()) {
	            list[i] = fileList[i].getName();
	        }
	    }
	    return list;
	}
    
	public void downloadFile(String fileName) {
        try {
            //read file
            File f = new File(fileName);
            byte[] byteArray = new byte[(int) f.length()];

            FileInputStream fileInputStream = new FileInputStream(f);
            BufferedInputStream buffInputStream = new BufferedInputStream(fileInputStream);
            
            DataInputStream dataInputStream = new DataInputStream(buffInputStream);
            dataInputStream.readFully(byteArray, 0, byteArray.length);
            
            //send file
            OutputStream outputStream = cSocket.getOutputStream();

            //Send file name + size to server
            DataOutputStream dataOutputStream = new DataOutputStream(outputStream);
            dataOutputStream.writeUTF(f.getName());
            dataOutputStream.writeLong(byteArray.length);
            dataOutputStream.write(byteArray, 0, byteArray.length);
            dataOutputStream.flush();
            System.out.println(fileName + " sent.");
        } catch (Exception e) {
            System.err.println("Please enter a valid file name.");
        }
        
	}//downloadFile

}//Connection
