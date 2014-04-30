package launcher;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

import com.Weaver;
import com.network.NodeInfo;
import com.orb.WeaverOrb;


public class Server {
	
	private Weaver weaver;


	public static void main(String[] args) {
		try{
		new Server();		
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
	
	
	public Server() throws UnknownHostException{
		
		System.out.println("starting server v"+SharedData.VERSION);
			
		//new File(SharedData.PATH_TO_SERVER_JAR).mkdir();
		
		
		//107.170.122.137
		//put my external IP here, plus the external forwarded port
        weaver = new Weaver( new NodeInfo[]{new NodeInfo("192.168.62.187",2232)} , 2232 );
        
        WeaverOrb orb = new WeaverOrb( SharedData.getSelfJarPath() + "sandsofosiris.jar", weaver );
		orb.start();		 
		      
		//pass in addresses of the master nodes		
		weaver.registerOrb(orb);
		weaver.start();
		
		
	}

}
