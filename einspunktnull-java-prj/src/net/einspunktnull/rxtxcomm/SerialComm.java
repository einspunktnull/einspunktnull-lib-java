package net.einspunktnull.rxtxcomm;

import gnu.io.CommPort;
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
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.EventListener;
import java.util.HashMap;
import java.util.TooManyListenersException;

import javax.swing.event.EventListenerList;

/**
 * Description
 * 
 * @author Albrecht Nitsche
 */
public class SerialComm implements SerialPortEventListener
{
	public static final String PROPERTY_BAUDRATE = "BaudRate";
	public static final String PROPERTY_PORTNAME = "PortName";
	public static final String PROPERTY_CONNECTED = "Connected";
	public static final String PROPERTY_INPUTMESSAGE = "InputMessage";

	// Baudrate statics
	public static final Integer BAUDRATE_300 = 300;
	public static final Integer BAUDRATE_1200 = 1200;
	public static final Integer BAUDRATE_2400 = 2400;
	public static final Integer BAUDRATE_4800 = 4800;
	public static final Integer BAUDRATE_9600 = 9600;
	public static final Integer BAUDRATE_14400 = 14400;
	public static final Integer BAUDRATE_19200 = 19200;
	public static final Integer BAUDRATE_28800 = 28800;
	public static final Integer BAUDRATE_38400 = 38400;
	public static final Integer BAUDRATE_57600 = 57600;
	public static final Integer BAUDRATE_115200 = 115200;

	// CommObjects
	private CommPortIdentifier commPortIdentifier;
	private SerialPort serialPort;
	private InputStream inputStream;
	private OutputStream outputStream;
	private String portName;
	private Integer baudRate;
	private byte[] buffer = new byte[1024];

	// Misc
	public boolean debug = false;
	private boolean connected = false;
	private EventListenerList eventListenerList = new EventListenerList();
	private ArrayList<EventListener> eventListenersArrayList = new ArrayList<EventListener>();

	/**********************************************/
	/*                                            */
	/* INITIALISATION */
	/*                                            */
	/**********************************************/
	public SerialComm()
	{
		baudRate = BAUDRATE_9600;
		setDefaultPortName();
	}

	private void setDefaultPortName()
	{
		String osname = System.getProperty("os.name", "").toLowerCase();
		if (osname.startsWith("windows"))
		{
			portName = "COM3";
		}
		else if (osname.startsWith("linux"))
		{
			portName = "/dev/ttyUSB0";
		}
		else if (osname.startsWith("mac"))
		{
			portName = "/dev/cu.usbserial*";
		}
		else
		{
			System.out.println("Sorry, your operating system is not supported");
			return;
		}
	}

	/**********************************************/
	/*                                            */
	/* CONNECT AND LISTEN */
	/*                                            */
	/**********************************************/
	public void connect()
	{
		Boolean connectionEstablished = true;
		try
		{
			commPortIdentifier = CommPortIdentifier.getPortIdentifier(getPortName());
			if (commPortIdentifier.getPortType() == CommPortIdentifier.PORT_SERIAL)
			{
				try
				{
					serialPort = (SerialPort) commPortIdentifier.open(this.getClass().getName(), 500);
				}
				catch (PortInUseException e)
				{
					connectionEstablished = false;
					System.out.println("Port in use.");
				}

				try
				{
					inputStream = serialPort.getInputStream();
					outputStream = serialPort.getOutputStream();
				}
				catch (IOException e)
				{
					connectionEstablished = false;
				}

				try
				{
					serialPort.notifyOnDataAvailable(true);
					// serialPort.notifyOnOutputEmpty(true);
				}
				catch (Exception e)
				{
					connectionEstablished = false;
					System.out.println("Error setting event notification");
					System.out.println(e.toString());
					System.exit(-1);
				}

				try
				{
					serialPort.setSerialPortParams(getBaudrate(), SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
				}
				catch (UnsupportedCommOperationException e)
				{
					connectionEstablished = false;
				}

			}
		}
		catch (NoSuchPortException e1)
		{
			System.out.println("There is no such Port '" + portName + "'");
		}

		if (connectionEstablished)
		{
			beginListening();
			this.connected = true;
			fireSerialCommConnectionEvent(true);
		}
	}

	private void beginListening()
	{
		try
		{
			serialPort.addEventListener(this);
		}
		catch (TooManyListenersException e)
		{
			e.printStackTrace();
		}
	}

	@Override
	public void serialEvent(SerialPortEvent ev)
	{
		int data;
		try
		{
			int len = 0;
			boolean cr = false;
			while ((data = inputStream.read()) > -1)
			{
				if (data == '\r') cr = true;
				if (data == '\n' || (data == '\n' && cr))
				{
					break;
				}
				buffer[len++] = (byte) data;
			}
			if (ev.getEventType() == SerialPortEvent.DATA_AVAILABLE)
			{
				String message = new String(buffer, 0, len - 1);
				if (debug) System.out.println("SerialComm.read: " + message);
				fireSerialCommMessageEvent(message);
				fireSerialCommEvent(message);
			}
		}
		catch (IOException e)
		{
			e.printStackTrace();
			System.exit(-1);
		}

	}

	/**********************************************/
	/*                                            */
	/* DISCONNECT AND STOP LISTENING */
	/*                                            */
	/**********************************************/
	public void disconnect()
	{
		if (getConnected())
		{
			endListening();
			serialPort.close();
		}
		this.connected = false;
		// fireSerialCommConnectionEvent(false);
		// fireSerialCommEvent(false);
	}

	private void endListening()
	{
		serialPort.notifyOnDataAvailable(false);
		serialPort.removeEventListener();
	}

	/**********************************************/
	/*                                            */
	/* WRITE AND DELETE MESSAGES */
	/*                                            */
	/**********************************************/
	public void write(String message)
	{
		if (connected)
		{
			try
			{
				outputStream.write(message.getBytes());
				if (debug) System.out.println("SerialComm.write: " + message);
			}
			catch (IOException e)
			{
			}

			try
			{
				Thread.sleep(50); // Be sure data is xferred before closing
			}
			catch (Exception e)
			{
			}
		}
		else
		{
			System.out.println("wasnt able to write - not connected");
		}
	}

	public void flush()
	{
		try
		{
			outputStream.flush();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}

	}

	/**********************************************/
	/*                                            */
	/* PUBLIC GETTER AND SETTER */
	/*                                            */
	/**********************************************/

	public void setBaudrate(Integer baudRate)
	{
		this.baudRate = baudRate;
	}

	public int getBaudrate()
	{
		return baudRate;
	}

	public void setPortName(String portName)
	{
		this.portName = portName;
	}

	public String getPortName()
	{
		return portName;
	}

	public boolean getConnected()
	{
		return connected;
	}

	/**********************************************/
	/*                                            */
	/* PUBLIC STATIC PORTCHECK */
	/*                                            */
	/**********************************************/

	public static HashMap<String, CommPortIdentifier> getAvailablePorts()
	{
		@SuppressWarnings("unchecked")
		Enumeration<CommPortIdentifier> portList = CommPortIdentifier.getPortIdentifiers();

		HashMap<String, CommPortIdentifier> portsMap = new HashMap<String, CommPortIdentifier>();

		while (portList.hasMoreElements())
		{
			CommPortIdentifier cpi = portList.nextElement();
			if (cpi.getPortType() == CommPortIdentifier.PORT_SERIAL)
			{
				portsMap.put(cpi.getName(), cpi);
			}
		}
		return portsMap;
	}

	public static HashMap<String, CommPortIdentifier> getOpenAvailablePorts()
	{
		@SuppressWarnings("unchecked")
		Enumeration<CommPortIdentifier> portList = CommPortIdentifier.getPortIdentifiers();

		HashMap<String, CommPortIdentifier> portsMap = new HashMap<String, CommPortIdentifier>();

		while (portList.hasMoreElements())
		{
			CommPortIdentifier cpi = portList.nextElement();
			if (cpi.getPortType() == CommPortIdentifier.PORT_SERIAL)
			{
				try
				{
					CommPort thePort = cpi.open("PortCheck", 50);
					thePort.close();
					portsMap.put(cpi.getName(), cpi);
				}
				catch (PortInUseException e)
				{
					System.out.println("Port, " + cpi.getName() + ", is in use.");
				}
				catch (Exception e)
				{
					System.err.println("Failed to open port " + cpi.getName());
					e.printStackTrace();
				}

				
			}
		}
		return portsMap;
	}

	/**********************************************/
	/*                                            */
	/* EVENT-FUNCTIONS */
	/*                                            */
	/**********************************************/

	public void addSerialCommMessageEventListener(SerialCommMessageEventListener scmel)
	{
		eventListenersArrayList.add(scmel);
		eventListenerList.add(SerialCommMessageEventListener.class, scmel);
	}

	public void removeSerialCommMessageEventListener(SerialCommMessageEventListener scmel)
	{
		eventListenersArrayList.remove(scmel);
		eventListenerList.remove(SerialCommMessageEventListener.class, scmel);
	}

	private void fireSerialCommMessageEvent(String message)
	{
		Object[] listeners = eventListenerList.getListenerList();
		for (int i = listeners.length - 2; i >= 0; i -= 2)
		{
			if (listeners[i] == SerialCommMessageEventListener.class)
			{
				SerialCommMessageEvent serialCommMessageEvent = new SerialCommMessageEvent(this, message);
				((SerialCommMessageEventListener) listeners[i + 1]).incommingMessage(serialCommMessageEvent);
			}
		}
	}

	public void addSerialCommConnectionEventListener(SerialCommConnectionEventListener sccel)
	{
		eventListenersArrayList.add(sccel);
		eventListenerList.add(SerialCommConnectionEventListener.class, sccel);
	}

	public void removeSerialCommConnectionEventListener(SerialCommConnectionEventListener sccel)
	{
		eventListenersArrayList.remove(sccel);
		eventListenerList.remove(SerialCommConnectionEventListener.class, sccel);
	}

	private void fireSerialCommConnectionEvent(Boolean connected)
	{
		Object[] listeners = eventListenerList.getListenerList();
		for (int i = listeners.length - 2; i >= 0; i -= 2)
		{
			if (listeners[i] == SerialCommConnectionEventListener.class)
			{
				SerialCommConnectionEvent serialCommConnectionEvent = new SerialCommConnectionEvent(this, connected);
				((SerialCommConnectionEventListener) listeners[i + 1]).connectionState(serialCommConnectionEvent);
			}
		}
	}

	public void addSerialCommEventListener(SerialCommEventListener scel)
	{
		eventListenersArrayList.add(scel);
		eventListenerList.add(SerialCommEventListener.class, scel);
	}

	public void removeSerialCommEventListener(SerialCommEventListener scel)
	{
		eventListenersArrayList.remove(scel);
		eventListenerList.remove(SerialCommEventListener.class, scel);
	}

	private void fireSerialCommEvent(String message)
	{
		Object[] listeners = eventListenerList.getListenerList();
		for (int i = listeners.length - 2; i >= 0; i -= 2)
		{
			if (listeners[i] == SerialCommEventListener.class)
			{
				SerialCommEvent serialCommEvent = new SerialCommEvent(this, SerialCommEvent.MESSAGE, message);
				((SerialCommEventListener) listeners[i + 1]).connectionState(serialCommEvent);
			}
		}
	}

	private void fireSerialCommEvent(Boolean connected)
	{
		Object[] listeners = eventListenerList.getListenerList();
		for (int i = listeners.length - 2; i >= 0; i -= 2)
		{
			if (listeners[i] == SerialCommEventListener.class)
			{
				SerialCommEvent serialCommEvent = new SerialCommEvent(this, SerialCommEvent.CONNECTION, connected);
				((SerialCommEventListener) listeners[i + 1]).connectionState(serialCommEvent);
			}
		}
	}

	public boolean hasEventListener(EventListener eventListener)
	{
		return eventListenersArrayList.contains(eventListener);
	}

}
