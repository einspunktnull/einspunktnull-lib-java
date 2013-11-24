package net.einspunktnull.rxtxcomm;

import gnu.io.SerialPortEventListener;

import java.util.EventObject;

public class SerialCommEvent extends EventObject
{
	private static final long serialVersionUID = -211468453162427124L;

	public static final String MESSAGE = "Message";
	public static final String CONNECTION = "Connection";

	private String message;
	private String eventType;
	private Boolean connection;

	public SerialCommEvent(SerialPortEventListener serialPortEventListener, String eventType, String message, Boolean connection)
	{
		super(serialPortEventListener);
		this.eventType = eventType;
		this.message = message;
		this.connection = connection;
	}

	public SerialCommEvent(SerialPortEventListener serialPortEventListener, String eventType, String message)
	{
		super(serialPortEventListener);
		this.eventType = eventType;
		this.message = message;
	}

	public SerialCommEvent(SerialPortEventListener serialPortEventListener, String eventType, Boolean connection)
	{
		super(serialPortEventListener);
		this.eventType = eventType;
		this.connection = connection;
	}

	public String getMessage()
	{
		return message;
	}

	public String getEventType()
	{
		return eventType;
	}

	public Boolean getConnected()
	{
		return connection;
	}

}
