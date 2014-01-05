package net.einspunktnull.comm.dev;

import java.io.IOException;
import java.net.UnknownHostException;

import org.xsocket.connection.Server;

import com.pi4j.io.serial.Serial;
import com.pi4j.io.serial.SerialFactory;

public class PI4JSerialCommDevice extends AbstractSerialCommDevice
{

	private Serial serial;
	private ListenThread currThread;

	public class ListenThread extends Thread
	{

		private volatile boolean isRunning = false;

		public void run()
		{
			isRunning = true;
			while (isRunning)
			{

				if (serial.availableBytes() > 0)
				{
					char bite = serial.read();
					onByteReceived((byte) bite);
//					listener.onByteReceived(PI4JSerialCommDevice.this, (byte) bite);
				}
			}
		}

		public void kill()
		{
			isRunning = false;
		}
	}
	
	public PI4JSerialCommDevice(String name, ICommDeviceListener listener, long baudrate, String portname)
	{
		super(name, listener, baudrate, portname);
	}

	@Override
	public void start() throws CommDeviceException
	{
		if (!running)
		{
			serial = SerialFactory.createInstance();
			int ret = serial.open(portname, (int) baudrate);
			if (ret == -1) { throw new CommDeviceException("ComPort connect failed: " + portname); }
			currThread = new ListenThread();
			currThread.start();
			running = true;
		}
		else throw new CommDeviceException("Already running", null);
	}

	@Override
	public void stop() throws CommDeviceException
	{
		if (running)
		{
			currThread.kill();
			serial.flush();
			serial.close();
			running = false;
		}
		else throw new CommDeviceException("Not running", null);
	}

	@Override
	public void write(String msg)
	{
		serial.write(msg);
	}

	@Override
	public void write(byte bite)
	{
		serial.write(bite);
	}

	@Override
	public void write(byte[] bytes)
	{
		serial.write(bytes);
	}

	@Override
	public int available()
	{
		return serial.availableBytes();
	}

	@Override
	public int read()
	{
		return serial.read();
	}

	@Override
	public String toString()
	{
		return "PI4JSerialCommDevice: " + name + ", " + portname + ", " + baudrate;
	}

}
