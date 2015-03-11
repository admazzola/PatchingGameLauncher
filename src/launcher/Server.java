package launcher;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

import com.google.common.hash.HashCode;
import com.google.common.hash.Hashing;
import com.google.common.io.Files;

import launcher.net.FileServer;
import launcher.net.FileUploader;
import launcher.records.ServerRecordKeeper;


public class Server {
	
	private FileServer fileServer;
	private ServerCommandManager commandManager;
	private static ServerRecordKeeper recordKeeper;
	

	public static void main(String[] args) {
		
		recordKeeper = new ServerRecordKeeper();
		try{
		new Server();		
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
	
	
	public Server() throws UnknownHostException{
		
		System.out.println("starting server v"+SharedData.VERSION);
			
		//new File(SharedData.PATH_TO_SERVER_JAR).mkdir();
		
		commandManager = new ServerCommandManager();
		
		//107.170.122.137
		//put my external IP here, plus the external forwarded port
		try {
			fileServer = new FileServer(SharedData.SERVERPORT);
			new Thread(fileServer).start();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		 Scanner sc = new Scanner(System.in);
		 while(sc.hasNextLine())
			 {
			 commandManager.acceptCommand(sc.nextLine());
			 }
	  
	}
	


	public static ServerRecordKeeper getRecordKeeper() {
		return recordKeeper;
	}


	public static String getCheckSum() {
		try{
		File file = new File(  SharedData.getSelfJarPath() + "sandsofosiris.jar" );		
		HashCode hc = Files.hash(file, Hashing.sha1());
		return hc.toString();
		}catch(Exception e){
			System.err.println("no file found to upload!!!");
			return "none";
		}
	}

	static void printOut(Object s) {
		printTimeStamp();
		System.out.println(s);
		
	}
	public static void printTimeStamp() {
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd h:mm a");
		System.out.print(sdf.format(date) + " ");
		
	}

}
