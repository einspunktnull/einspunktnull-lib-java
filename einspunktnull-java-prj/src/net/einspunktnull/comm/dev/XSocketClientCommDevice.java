package net.einspunktnull.comm.dev;

import java.io.IOException;
import java.net.UnknownHostException;

import org.xsocket.connection.NonBlockingConnection;
import org.xsocket.connection.Server;

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
		if (!running)
		{

			try
			{
				nbc = new NonBlockingConnection(host, portnumber, this);
				running = true;
			}
			catch (IOException e)
			{
				throw new CommDeviceException("IOException", e);
			}
		}
		else throw new CommDeviceException("Already running", null);

	}

	@Override
	public void stop() throws CommDeviceException
	{
		if (running)
		{
			try
			{
				nbc.close();
				running = false;
			}
			catch (IOException e)
			{
				throw new CommDeviceException("IOException", e);
			}
		}
		else throw new CommDeviceException("Not running", null);

	}

	@Override
	public String toString()
	{
		return "XSocketClientCommDevice: " + name + ", " + host + ", " + portnumber;
	}

}
