package net.einspunktnull.comm.dev;

import gnu.io.CommPortIdentifier;
import gnu.io.NoSuchPortException;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;
import gnu.io.UnsupportedCommOperationException;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.TooManyListenersException;

public class RxTxSerialCommDevice extends AbstractSerialCommDevice implements SerialPortEventListener
{

	private CommPortIdentifier commPortIdentifier;
	protected SerialPort serialPort;
	protected InputStream inputStream;
	protected OutputStream outputStream;
	private int timeout;

	public RxTxSerialCommDevice(String name, ICommListener listener, int baudrate, String portname, int timeout)
	{
		super(name, listener, baudrate, portname);
		this.timeout = timeout;
	}

	@Override
	public void start() throws CommDeviceException
	{
		if (!isListening)
		{

			try
			{
				commPortIdentifier = CommPortIdentifier.getPortIdentifier(portname);
				serialPort = (SerialPort) commPortIdentifier.open(this.getClass().getName(), timeout);
				inputStream = serialPort.getInputStream();
				outputStream = serialPort.getOutputStream();
				serialPort.notifyOnDataAvailable(true);
				serialPort.setSerialPortParams(baudrate, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
				serialPort.addEventListener(this);
				isListening = true;
			}
			catch (NoSuchPortException e)
			{
				throw new CommDeviceException("NoSuchPortException", e);
			}
			catch (PortInUseException e)
			{
				throw new CommDeviceException("PortInUseException", e);
			}
			catch (IOException e)
			{
				throw new CommDeviceException("IOException", e);
			}
			catch (UnsupportedCommOperationException e)
			{
				throw new CommDeviceException("UnsupportedCommOperationException", e);
			}
			catch (TooManyListenersException e)
			{
				throw new CommDeviceException("TooManyListenersException", e);
			}
		}
	}

	@Override
	public void stop() throws CommDeviceException
	{
		try
		{
			outputStream.flush();
			outputStream.close();
			inputStream.close();
		}
		catch (IOException e)
		{
			throw new CommDeviceException("IOException", e);
		}
		serialPort.close();
	}

	@Override
	public void serialEvent(SerialPortEvent serialPortEvent)
	{
		try
		{
			while (inputStream.available() > 0)
			{
				int bite = inputStream.read();
				onByteReceived((byte) bite);
			}
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}

	}

	@Override
	public void write(String msg) throws CommDeviceException
	{
		try
		{
			outputStream.write(msg.getBytes());
		}
		catch (IOException e)
		{
			throw new CommDeviceException("IOException", e);
		}

	}
	

	@Override
	public void write(byte bite) throws CommDeviceException
	{
		try
		{
			outputStream.write((int) bite);
		}
		catch (IOException e)
		{
			throw new CommDeviceException("IOException", e);
		}
	}

	@Override
	public void write(byte[] bytes) throws CommDeviceException
	{
		try
		{
			outputStream.write(bytes);
		}
		catch (IOException e)
		{
			throw new CommDeviceException("IOException", e);
		}
	}

	@Override
	public int available() throws CommDeviceException
	{
		try
		{
			return inputStream.available();
		}
		catch (IOException e)
		{
			throw new CommDeviceException("IOException", e);
		}
	}

	@Override
	public int read() throws CommDeviceException
	{
		try
		{
			return inputStream.read();
		}
		catch (IOException e)
		{
			throw new CommDeviceException("IOException", e);
		}
	}

}
