package net.einspunktnull.comm.dev;

public abstract class AbstractSerialCommDevice extends AbstractCommDevice
{

	protected String portname;
	protected long baudrate;

	public AbstractSerialCommDevice(String name, ICommDeviceListener listener, long baudrate, String portname)
	{
		super(name, listener);
		this.portname = portname;
		this.baudrate = baudrate;
	}

	public abstract int available() throws CommDeviceException;

	public abstract int read() throws CommDeviceException;

	@Override
	public String toString()
	{
		return "AbstractSerialCommDevice: " + name + ", " + portname + ", " + baudrate;
	}

}
