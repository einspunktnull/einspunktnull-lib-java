package net.einspunktnull.events;

import java.util.EventListener;

public interface PropertyAccessEventListener extends EventListener
{
	void propertyAccess(PropertyAccessEvent evt);
}
