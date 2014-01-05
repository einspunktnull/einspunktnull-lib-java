package net.einspunktnull.comm.dev;

import java.io.IOException;
import java.nio.BufferOverflowException;
import java.nio.BufferUnderflowException;
import java.nio.channels.ClosedChannelException;

import net.einspunktnull.io.Sysout;

import org.xsocket.MaxReadSizeExceededException;
import org.xsocket.connection.IConnectHandler;
import org.xsocket.connection.IDataHandler;
import org.xsocket.connection.IDisconnectHandler;
import org.xsocket.connection.INonBlockingConnection;

public abstract class AbstractXSocketCommDevice extends AbstractCommDevice implements IDataHandler, IConnectHandler, IDisconnectHandler
{

	protected int portnumber;
	protected INonBlockingConnection nbc;

	public AbstractXSocketCommDevice(String name, ICommDeviceListener listener, int portnumber)
	{
		super(name, listener);
		this.portnumber = portnumber;
	}

	@Override
	public String toString()
	{
		return "AbstractXSocketCommDevice: " + name + ", " + portnumber;
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
		super.onByteReceived(bite);
		return false;
	}

	@Override
	public boolean onConnect(INonBlockingConnection nbc) throws IOException, BufferUnderflowException, MaxReadSizeExceededException
	{
		this.nbc = nbc;
		running = true;
		listener.onConnect(this);
		Sysout.println("!!! AbstractXSocketCommDevice.onConnect() !!!");
		return false;
	}

	@Override
	public boolean onDisconnect(INonBlockingConnection nbc) throws IOException, BufferUnderflowException, MaxReadSizeExceededException
	{
		this.nbc = null;
		running = false;
		listener.onDisconnect(this);
		Sysout.println("!!! AbstractXSocketCommDevice.onDisconnect() !!!");
		return false;
	}

}
