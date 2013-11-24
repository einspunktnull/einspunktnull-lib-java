package net.einspunktnull.comm.dev;

import java.io.IOException;
import java.net.UnknownHostException;
import java.nio.BufferOverflowException;
import java.nio.BufferUnderflowException;
import java.nio.channels.ClosedChannelException;

import org.xsocket.MaxReadSizeExceededException;
import org.xsocket.connection.IConnectHandler;
import org.xsocket.connection.IDataHandler;
import org.xsocket.connection.INonBlockingConnection;
import org.xsocket.connection.Server;

public class XSocketCommDevice extends AbstractCommDevice implements IDataHandler, IConnectHandler
{

	private int portnumber;

	private Server server;

	private INonBlockingConnection nbc;

	public XSocketCommDevice(String name, ICommListener listener, int portnumber)
	{
		super(name, listener);
		this.portnumber = portnumber;
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
	public void write(String msg) throws CommDeviceException
	{
		if (nbc == null) { throw new CommDeviceException("No Connection"); }

		try
		{
			nbc.write(msg);
		}
		catch (BufferOverflowException e)
		{
			throw new CommDeviceException("BufferOverflowException", e);
		}
		catch (IOException e)
		{
			throw new CommDeviceException("IOException", e);
		}

	}

	@Override
	public void write(byte bite) throws CommDeviceException
	{
		if (nbc == null) { throw new CommDeviceException("No Connection"); }

		try
		{
			nbc.write(bite);
		}
		catch (BufferOverflowException e)
		{
			throw new CommDeviceException("BufferOverflowException", e);
		}
		catch (IOException e)
		{
			throw new CommDeviceException("IOException", e);
		}

	}

	@Override
	public void write(byte[] bytes) throws CommDeviceException
	{
		if (nbc == null) { throw new CommDeviceException("No Connection"); }

		try
		{
			nbc.write(bytes);
		}
		catch (BufferOverflowException e)
		{
			throw new CommDeviceException("BufferOverflowException", e);
		}
		catch (IOException e)
		{
			throw new CommDeviceException("IOException", e);
		}

	}

	@Override
	public boolean onData(INonBlockingConnection nbc) throws IOException, BufferUnderflowException, ClosedChannelException, MaxReadSizeExceededException
	{
		byte bite = nbc.readByte();
		listener.onByteReceived(this, bite);
		return false;
	}

	@Override
	public boolean onConnect(INonBlockingConnection nbc) throws IOException, BufferUnderflowException, MaxReadSizeExceededException
	{
		this.nbc = nbc;
		return false;
	}

}
