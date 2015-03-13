package launcher.net;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Date;

import org.apache.commons.io.IOUtils;

import launcher.Launcher;
import launcher.Server;
import launcher.SharedData;
import launcher.records.ClientConnectionRecord;

public class FileUploader implements Runnable, ProgressListener{

	Socket socket;
	FileServer fileServer;
	public FileUploader(Socket socket, FileServer fileServer) {
	this.socket=socket;
	this.fileServer = fileServer;
	}

	@Override
	public void run() {
		
		try{
		 
			// BufferedReader textin = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			// PrintWriter textout = new PrintWriter(socket.getOutputStream() , true);	
			 
			 DataOutputStream  out = new DataOutputStream(socket.getOutputStream());
			 DataInputStream  textin = new DataInputStream(socket.getInputStream());
			 
			 String checksumFromClient = textin.readUTF();
			 String operatingSystem = textin.readUTF();
			 
			if(checksumFromClient.equals(Server.getCheckSum())){				 
				 System.out.println("client has file already");
				//textout.println("up to date");
				out.writeUTF("up to date");
				 
				Server.printTimeStamp();
			    System.out.println("new client already has file");
			    
			   Server.getRecordKeeper().addClientConnectionData(new ClientConnectionRecord(new Date(), socket.getRemoteSocketAddress().toString(), false,false )  ); 
			 }else{				 
				 System.out.println("giving file to client running "+ operatingSystem);
				// textout.println("sending new file");
				 out.writeUTF("sending new file");
				 
			
		Server.printTimeStamp();
	    System.out.println("sending file to new client");
	    
	  
	    
	    //textout.println(fileServer.getFileLength(operatingSystem));
	    out.writeUTF(""+fileServer.getFileLength(operatingSystem));
	    //byte[] bytes = new byte[(int) length];
	   
	    
	    FileInputStream in = new FileInputStream(fileServer.getFile(operatingSystem));
	    
	  
	    fileServer.setBusy(true);


	    
	    CustomIOUtils.copy(in,  out , this);
	    
	    fileServer.setBusy(false);
	    
	    Thread.sleep(3000);//wait for client to finish up before cutting out
	    
	    
        in.close();
        out.close();
	    
	    	Server.printTimeStamp();
	        System.out.println("done writing");
	        

	        Server.getRecordKeeper().addClientConnectionData(new ClientConnectionRecord(new Date(), socket.getRemoteSocketAddress().toString(), true,false )  ); 
	        Thread.sleep(3000);//wait for client to finish up

	        
	    //out.flush();
	    //out.close();
	   // fis.close();
	    //bis.close();
		}

		textin.close();
		
	    socket.close();
	    
		}catch(Exception e){
			Server.getRecordKeeper().addClientConnectionData(new ClientConnectionRecord(new Date(), socket.getRemoteSocketAddress().toString(), false,true )  ); 
			
			e.printStackTrace();
		}
		
	}

	@Override
	public void getProgress(long count) {
		//System.out.println("Sent " + count);
		
	}

}
