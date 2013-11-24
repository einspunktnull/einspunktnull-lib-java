package net.einspunktnull.comm.dev;

@SuppressWarnings("serial")
public class CommDeviceException extends Exception
{

	private Exception origExc;

	public CommDeviceException(String reason)
	{
		this(reason, null);
	}

	public Exception getOrigExc()
	{
		return origExc;
	}

	public CommDeviceException(String reason, Exception origExc)
	{
		super(reason);
		this.origExc = origExc != null ? origExc : this;
	}
}
