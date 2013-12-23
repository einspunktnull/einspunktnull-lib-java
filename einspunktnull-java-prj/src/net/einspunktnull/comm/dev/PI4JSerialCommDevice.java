package net.einspunktnull.comm.dev;

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
					listener.onByteReceived(PI4JSerialCommDevice.this, (byte) bite);
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
		serial = SerialFactory.createInstance();
		int ret = serial.open(portname, (int) baudrate);
		if (ret == -1) { throw new CommDeviceException("ComPort connect failed: " + portname); }
		currThread = new ListenThread();
		currThread.start();
	}

	@Override
	public void stop()
	{
		currThread.kill();
		serial.flush();
		serial.close();
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
