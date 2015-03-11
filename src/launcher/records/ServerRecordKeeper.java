package launcher.records;

import java.util.ArrayList;

public class ServerRecordKeeper {

	ArrayList<ClientConnectionRecord> clientConnections = new ArrayList<ClientConnectionRecord>();

	public synchronized ArrayList<ClientConnectionRecord> getClientConnections() {
		return clientConnections;
	}
	static int MAX_SIZE = 100;
	
	//hit from multiple threads
	public synchronized void addClientConnectionData(ClientConnectionRecord record  )
	{
		
		
		clientConnections.add(record);
		
		if( clientConnections.size() > MAX_SIZE )
		{
			clientConnections.remove( 0 );
		}
	}
	
	
	
}
