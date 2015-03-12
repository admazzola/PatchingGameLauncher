package launcher.net;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Date;

import launcher.Launcher;
import launcher.Server;
import launcher.SharedData;
import launcher.records.ClientConnectionRecord;

public class FileUploader implements Runnable{

	Socket socket;
	FileServer fileServer;
	public FileUploader(Socket socket, FileServer fileServer) {
	this.socket=socket;
	this.fileServer = fileServer;
	}

	@Override
	public void run() {
		
		try{
		
			 BufferedReader textin = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			 PrintWriter textout = new PrintWriter(socket.getOutputStream() , true);	    
			
			 String checksumFromClient = textin.readLine();
			 String operatingSystem = textin.readLine();
			 
			if(checksumFromClient.equals(Server.getCheckSum())){				 
				 System.out.println("client has file already");
				textout.println("up to date");
				 
				Server.printTimeStamp();
			    System.out.println("new client already has file");
			    
			   Server.getRecordKeeper().addClientConnectionData(new ClientConnectionRecord(new Date(), socket.getRemoteSocketAddress().toString(), false,false )  ); 
			 }else{				 
				 System.out.println("giving file to client running "+ operatingSystem);
				 textout.println("sending new file");
				
			 
			
		Server.printTimeStamp();
	    System.out.println("sending file to new client");
	    
	  
	    
	    textout.println(fileServer.getFileLength(operatingSystem));
	    
	    //byte[] bytes = new byte[(int) length];
	    
	    
	    FileInputStream fis = new FileInputStream(fileServer.getFile(operatingSystem));
	    BufferedInputStream bis = new BufferedInputStream(fis);
	    BufferedOutputStream out = new BufferedOutputStream(socket.getOutputStream());

	    int count;
	    
	  //  while(fileServer.isBusy()){
	   // 	Thread.sleep(100);
	   // }

	    fileServer.setBusy(true);
	    while ((count = bis.read(fileServer.getBytes(operatingSystem))) > 0) {
	        out.write(fileServer.getBytes(operatingSystem), 0, count);
	    }
	    fileServer.setBusy(false);
	    
	    	Server.printTimeStamp();
	        System.out.println("done writing");
	        
	        Server.getRecordKeeper().addClientConnectionData(new ClientConnectionRecord(new Date(), socket.getRemoteSocketAddress().toString(), true,false )  ); 
	        Thread.sleep(3000);//wait for client to finish up
	        
	    out.flush();
	    out.close();
	    fis.close();
	    bis.close();
		}

		textin.close();
		textout.close();
	    socket.close();
	    
		}catch(Exception e){
			Server.getRecordKeeper().addClientConnectionData(new ClientConnectionRecord(new Date(), socket.getRemoteSocketAddress().toString(), false,true )  ); 
			
			e.printStackTrace();
		}
		
	}

}
