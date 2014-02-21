package net.einspunktnull.comm.dev;

import jssc.SerialPort;
import jssc.SerialPortEvent;
import jssc.SerialPortEventListener;
import jssc.SerialPortException;
import net.einspunktnull.io.Sysout;

public class JSSCSerialCommDevice extends AbstractSerialCommDevice implements SerialPortEventListener
{

	private SerialPort serialPort;

	public JSSCSerialCommDevice(String name, ICommDeviceListener listener, long baudrate, String portname)
	{
		super(name, listener, baudrate, portname);
	}

	@Override
	public void start() throws CommDeviceException
	{
		if (!running)
		{
			serialPort = new SerialPort(portname);
			try
			{
				serialPort.openPort();
				serialPort.setParams((int) baudrate, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
				serialPort.setEventsMask(SerialPort.MASK_RXCHAR | SerialPort.MASK_ERR);
				serialPort.addEventListener(this);
				running = true;

			}
			catch (SerialPortException e)
			{
				throw new CommDeviceException("SerialPortException", e);
			}
		}
		else throw new CommDeviceException("Already running", null);

	}

	@Override
	public void serialEvent(SerialPortEvent evt)
	{

		try
		{
			if (evt.isRXCHAR())
			{
				int numBytes = evt.getEventValue();

				byte[] bytes = serialPort.readBytes(numBytes);
				for (int i = 0; i < bytes.length; i++)
				{
					byte bite = bytes[i];
					super.onByteReceived(bite);
				}
			}

		}
		catch (SerialPortException e)
		{
			e.printStackTrace();
		}
	}

	@Override
	public void stop() throws CommDeviceException
	{
		if (running)
		{
			try
			{
				serialPort.removeEventListener();
				serialPort.closePort();
				running = false;
			}
			catch (SerialPortException e)
			{
				throw new CommDeviceException("SerialPortException", e);
			}
		}
		else throw new CommDeviceException("Not running", null);

	}

	@Override
	public int available() throws CommDeviceException
	{
		throw new CommDeviceException("There is no available()-support for JSSCSerialCommDevice");
	}

	@Override
	public int read() throws CommDeviceException
	{
		try
		{
			return serialPort.readIntArray(1)[0];
		}
		catch (SerialPortException e)
		{
			throw new CommDeviceException("SerialPortException", e);
		}
	}

	@Override
	public void write(String msg) throws CommDeviceException
	{
		try
		{
			serialPort.writeString(msg);
		}
		catch (SerialPortException e)
		{
			throw new CommDeviceException("SerialPortException", e);
		}

	}

	@Override
	public void write(byte bite) throws CommDeviceException
	{
		try
		{
			serialPort.writeByte(bite);
		}
		catch (SerialPortException e)
		{
			throw new CommDeviceException("SerialPortException", e);
		}

	}

	@Override
	public void write(byte[] bytes) throws CommDeviceException
	{
		try
		{
			serialPort.writeBytes(bytes);
		}
		catch (SerialPortException e)
		{
			throw new CommDeviceException("SerialPortException", e);
		}

	}

	@Override
	public String toString()
	{
		return "JSSCSerialCommDevice: " + name + ", " + portname + ", " + baudrate;
	}
}
