package net.einspunktnull.events;

import javax.swing.event.EventListenerList;

public class PropertyAccessNotifier
{

	private EventListenerList eventListenerList;

	public PropertyAccessNotifier(Object source)
	{
		eventListenerList = new EventListenerList();
	}

	public void addPropertyAccessEventListener(PropertyAccessEventListener pael)
	{
		eventListenerList.add(PropertyAccessEventListener.class, pael);
	}

	public void removePropertyAccessEventListener(PropertyAccessEventListener pael)
	{
		eventListenerList.remove(PropertyAccessEventListener.class, pael);
	}

	public void firePropertyAccessEvent(String propertyName, Object value)
	{
		Object[] listeners = eventListenerList.getListenerList();
		for (int i = listeners.length - 2; i >= 0; i -= 2)
		{
			if (listeners[i] == PropertyAccessEventListener.class)
			{
				PropertyAccessEvent propertyAccessEvent = new PropertyAccessEvent(this, propertyName, value);
				((PropertyAccessEventListener) listeners[i + 1]).propertyAccess(propertyAccessEvent);
			}
		}
	}

}
