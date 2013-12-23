package net.einspunktnull.comm.dev;

import java.io.IOException;
import java.net.UnknownHostException;

import org.xsocket.connection.Server;

public class XSocketServerCommDevice extends AbstractXSocketCommDevice
{
	private Server server;

	public XSocketServerCommDevice(String name, ICommDeviceListener listener, int portnumber)
	{
		super(name, listener, portnumber);
	}

	@Override
	public void start() throws CommDeviceException
	{
		try
		{
			server = new Server(portnumber, this);
			server.start();
		}
		catch (UnknownHostException e)
		{
			throw new CommDeviceException("UnknownHostException", e);
		}
		catch (IOException e)
		{
			throw new CommDeviceException("IOException", e);
		}
	}

	@Override
	public void stop() throws CommDeviceException
	{
		server.close();
	}

	@Override
	public String toString()
	{
		return "XSocketServerCommDevice: " + name + ", " + portnumber;
	}

}
