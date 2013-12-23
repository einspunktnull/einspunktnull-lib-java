package net.einspunktnull.comm.dev;


public interface ICommDeviceListener
{

	public void onByteReceived(AbstractCommDevice device, byte bite);

}
