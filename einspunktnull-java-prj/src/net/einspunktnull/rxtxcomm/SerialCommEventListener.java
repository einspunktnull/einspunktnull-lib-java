package net.einspunktnull.rxtxcomm;

import java.util.EventListener;

public interface SerialCommEventListener extends EventListener
{
	void incommingMessage(SerialCommEvent evt);

	void connectionState(SerialCommEvent evt);
}
