package net.einspunktnull.old.mvc;

import net.einspunktnull.events.PropertyAccessEvent;
import net.einspunktnull.events.PropertyAccessEventListener;

public class AbstractControllerExtended extends AbstractController implements PropertyAccessEventListener
{
	
	public AbstractControllerExtended()
	{
		super();
	}
	
	public void addModel(AbstractModelExtended model)
	{
		registeredModels.add(model);
		model.addPropertyChangeListener(this);
		model.addPropertyAccessEventListener(this);
	}

	public void removeModel(AbstractModelExtended model)
	{
		registeredModels.remove(model);
		model.removePropertyChangeListener(this);
		model.removePropertyAccessEventListener(this);
	}

	@Override
	public void propertyAccess(PropertyAccessEvent evt)
	{
		for (AbstractView view : registeredViews)
		{
			((AbstractViewExtended)view).modelPropertyAccess(evt);
		}
		
	}
	
}
