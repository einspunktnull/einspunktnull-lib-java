package net.einspunktnull.rxtxcomm;

import gnu.io.SerialPortEventListener;

import java.util.EventObject;

public class SerialCommConnectionEvent extends EventObject
{

	private static final long serialVersionUID = -5551736074845911334L;

	public static final String CONNECTION = "Connection";

	private Boolean connection;

	public SerialCommConnectionEvent(SerialPortEventListener serialPortEventListener, Boolean connection)
	{
		super(serialPortEventListener);
		this.connection = connection;
	}

	public Boolean getConnected()
	{
		return connection;
	}

}
