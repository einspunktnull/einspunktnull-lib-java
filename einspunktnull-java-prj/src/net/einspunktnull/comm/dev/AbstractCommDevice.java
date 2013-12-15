package net.einspunktnull.comm.dev;

public abstract class AbstractCommDevice
{

	protected String name;
	protected ICommListener listener;
	protected boolean isListening;

	public AbstractCommDevice(String name, ICommListener listener)
	{
		this.name = name;
		this.listener = listener;
		isListening = false;
	}

	protected void onByteReceived(byte bite)
	{
		listener.onByteReceived(this, bite);
	}
	
	@Override
	public String toString()
	{
		return "AbstractCommDevice: "+ name;
	}

	public abstract void start() throws CommDeviceException;

	public abstract void stop() throws CommDeviceException;

	public abstract void write(String msg) throws CommDeviceException;

	public abstract void write(byte bite) throws CommDeviceException;

	public abstract void write(byte[] bytes) throws CommDeviceException;
}
