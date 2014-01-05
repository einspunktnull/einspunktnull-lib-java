package net.einspunktnull.comm.dev;

public interface ICommDeviceListener
{

	public void onByteReceived(AbstractCommDevice device, byte bite);

	public void onConnect(AbstractCommDevice device);

	public void onDisconnect(AbstractCommDevice device);

	public void onReceivingStarted(AbstractCommDevice device);

	public void onReceivingFinished(AbstractCommDevice device);

}
