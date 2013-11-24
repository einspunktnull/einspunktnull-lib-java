package net.einspunktnull.rxtxcomm;

import java.util.EventListener;

public interface SerialCommConnectionEventListener extends EventListener
{
	void connectionState(SerialCommConnectionEvent evt);
}
