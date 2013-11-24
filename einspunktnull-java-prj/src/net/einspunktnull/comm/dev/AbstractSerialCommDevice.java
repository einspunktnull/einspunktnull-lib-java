package net.einspunktnull.comm.dev;

public abstract class AbstractSerialCommDevice extends AbstractCommDevice
{

	protected String portname;
	protected int baudrate;

	public AbstractSerialCommDevice(String name, ICommListener listener, int baudrate, String portname)
	{
		super(name, listener);
		this.portname = portname;
		this.baudrate = baudrate;
	}

	public abstract int available() throws CommDeviceException;

	public abstract int read() throws CommDeviceException;

}
