package launcher.records;

import java.util.Date;

public class ClientConnectionRecord {
	
	boolean updated;
	boolean failed;
	String address;
	Date time;
	
	ClientConnectionRecord()
	{
	}
	
	public ClientConnectionRecord(Date time, String address, boolean updated, boolean failed)
	{
		this.address=address;
		this.time=time;
		this.updated = updated;
		this.failed = failed;
	}
	
	@Override
	public String toString()
	{
		return address + " @ " + time + " updated:"+ updated + " failed:" + failed;
		
	}
}
