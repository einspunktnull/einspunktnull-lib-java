package net.einspunktnull.comm.dev;

import java.io.IOException;

import org.xsocket.connection.NonBlockingConnection;

public class XSocketClientCommDevice extends AbstractXSocketCommDevice
{

	private String host;

	public XSocketClientCommDevice(String name, ICommDeviceListener listener, String host, int portnumber)
	{
		super(name, listener, portnumber);
		this.host = host;
	}

	@Override
	public void start() throws CommDeviceException
	{
		try
		{
			nbc = new NonBlockingConnection(host, portnumber, this);
		}
		catch (IOException e)
		{
			throw new CommDeviceException("IOException", e);
		}

	}

	@Override
	public void stop() throws CommDeviceException
	{
		try
		{
			nbc.close();
		}
		catch (IOException e)
		{
			throw new CommDeviceException("IOException", e);
		}

	}

	@Override
	public String toString()
	{
		return "XSocketClientCommDevice: " + name + ", " + host + ", " + portnumber;
	}

}
