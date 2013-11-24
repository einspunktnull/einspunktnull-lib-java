package net.einspunktnull.rxtxcomm;

import java.util.EventListener;

public interface SerialCommMessageEventListener extends EventListener
{
	void incommingMessage(SerialCommMessageEvent evt);
}
