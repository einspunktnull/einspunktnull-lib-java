package net.einspunktnull.comm.dev;


public interface ICommListener
{

	public void onByteReceived(AbstractCommDevice device, byte bite);

}
