package launcher.net;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

import launcher.SharedData;

public class FileServer implements Runnable{

/**
 * @param args the command line arguments
 */
	
	int serverport;
	File file;
	long length;
	//byte[] bytes;
	
	
public FileServer( int serverport)  throws IOException {
	this.serverport=serverport;
	
	file = new File(SharedData.getSelfJarPath() + "sandsofosiris.jar");
	length = file.length();
	
	//bytes = new byte[(int) length];
	
	System.out.println("File at " +SharedData.getSelfJarPath() + "sandsofosiris.jar"+ " length: "+length);
    
	  try {
        serverSocket = new ServerSocket(serverport);
    } catch (IOException ex) {
        System.out.println("Can't setup server on this port number. ");
    }
	
}

 ServerSocket serverSocket = null;
@Override
public void run() {
   
    
    
    System.out.println("fileserver listening on "+serverport);
  
 
    while(true){ //keep accepting connections from tons of launchers
    try {
    	Socket socket = null;
        socket = serverSocket.accept();
        
        System.out.println("accepted new client connection - starting another upload thread");
        
        Runnable uploader = new FileUploader(socket,this);
        new Thread(uploader).start();
        
        
    } catch (IOException ex) {
        System.out.println("Can't accept client connection. ");
    }
    
    
    }
	
  
    
	
}


//send separate files for each OS later on ?
public long getFileLength(String operatingSystem) {
	
	return length;
}


public File getFile(String operatingSystem) {

	return file;
}




boolean busy = false;
public boolean isBusy() {	
	return busy;
}
public void setBusy(boolean busy){
	this.busy = busy;
}




}