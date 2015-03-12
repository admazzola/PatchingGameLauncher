package launcher;

import java.util.ArrayList;

import launcher.records.ClientConnectionRecord;

public class ServerCommandManager {

	
	void acceptCommand(String command) {

		try
		{
			
	
			switch(command.toLowerCase())
			{
				case "help":printHelp();break;
				case "log":log();break;
				default: unknownCommand();break;
			}
		
		}catch(Exception e)
		{
			e.printStackTrace();
		}
	}


	private void unknownCommand() {
		Server.printOut(SharedData.UNKNOWNCOMMANDSTRING);
		
	}





	private void log() {
		if(Server.getRecordKeeper().getClientConnections().isEmpty())
			return;
		
		ArrayList<ClientConnectionRecord> clientConnectionsCopy = new ArrayList<ClientConnectionRecord>(Server.getRecordKeeper().getClientConnections());
		
		for( ClientConnectionRecord record :  clientConnectionsCopy )
		{
			Server.printOut( record );
		}
		
	}


	private void printHelp() {
		Server.printOut(SharedData.HELPSTRING);
		
	}

	
}
