package launcher;

import java.io.File;
import java.net.URI;

public class SharedData {
	
	public static final int SERVERPORT = 2232;

	//public static final String SERVERIP = "107.170.122.137";
	public static final String SERVERIP = "localhost";  //FORTESTING

	public static final String URL_BLOG = "localhost";
	//public static final String URL_BLOG = "http://www.starflask.com/launcher/updates";
	public static final String PATH_TO_CLIENT_JAR = getDefaultDirectory() +"\\" +"Sands of Osiris" +"\\";
	//public static final String PATH_TO_SERVER_JAR = getDefaultDirectory() +"\\" +"SandsLauncher" +"\\" ;
	
	public static final String FILENAME = "sandsofosiris.jar";
	public static final int GameMegabyteSizeInit = 512;
	public static final int GameMegabyteSizeMax = 2048; // do not use or it will break 32bit
	
	public static final String HELPSTRING = "AVAILABLE COMMANDS:" + "\r\n"
								 + " 'log' - Displays a list of recent client connections ";
	public static final String UNKNOWNCOMMANDSTRING = "Invalid command. Type 'help' for assistance.";

	public static String VERSION = "0.17";
	
	public static String getSelfJarPath(){
		String fullpath = Server.class.getProtectionDomain().getCodeSource().getLocation().getPath();
		
		return fullpath.substring(0,fullpath.lastIndexOf("/")+1) ;
		
	}
	

	
	public static String getDefaultDirectory()
	{

		String OS = System.getProperty("os.name").toUpperCase();

		String answer = System.getProperty("user.home");

		if (OS.contains("WIN")) {
			answer = System.getenv("APPDATA");
		}
		else if (OS.contains("MAC")) {
			answer = System.getProperty("user.home") + "/Library/Application "
					+ "Support";
		}
		else if (OS.contains("NUX")) {
			answer = System.getProperty("user.home");
		}

		//answer += "\\" + AppName + "\\";

		return answer;
	}
	
	
	public static OS getOS()
	{
		String osname = System.getProperty("os.name").toUpperCase();

		if (osname.contains("WIN")) {
			return OS.WINDOWS;
		}
		else if (osname.contains("MAC")) {
			return OS.MAC;
		}
		else if (osname.contains("NUX")) {
			return OS.LINUX;
		}
		
		return OS.LINUX;
	}
	
	public enum OS{
		WINDOWS("win"),MAC("mac"),LINUX("nux");

		
		private String name;

		OS(String name){
			this.name=name;
		}
		
		public String getName() {
			// TODO Auto-generated method stub
			return name;
		}
	}
	
	
}
