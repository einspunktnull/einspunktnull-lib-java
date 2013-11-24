package net.einspunktnull.rxtxcomm;

import gnu.io.SerialPortEventListener;

import java.util.EventObject;

public class SerialCommMessageEvent extends EventObject
{
	private static final long serialVersionUID = -9048023740608441493L;
	private String message;

	public SerialCommMessageEvent(SerialPortEventListener serialPortEventListener, String message)
	{
		super(serialPortEventListener);
		this.message = message;
	}

	public String getMessage()
	{
		return message;
	}

}
