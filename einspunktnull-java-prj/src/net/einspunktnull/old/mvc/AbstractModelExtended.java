package net.einspunktnull.old.mvc;

import net.einspunktnull.events.PropertyAccessEventListener;
import net.einspunktnull.events.PropertyAccessNotifier;

public class AbstractModelExtended extends AbstractModel
{
	protected PropertyAccessNotifier _propertyAccessNotifier;
	
	public AbstractModelExtended()
	{
		super();
		_propertyAccessNotifier = new PropertyAccessNotifier(this);
	}

	public void addPropertyAccessEventListener(PropertyAccessEventListener pael)
	{
		_propertyAccessNotifier.addPropertyAccessEventListener(pael);
	}

	public void removePropertyAccessEventListener(PropertyAccessEventListener pael)
	{
		_propertyAccessNotifier.removePropertyAccessEventListener(pael);
	}

	protected void firePropertyAccess(String propertyName, Object value)
	{
		_propertyAccessNotifier.firePropertyAccessEvent(propertyName, value);
	}

}
