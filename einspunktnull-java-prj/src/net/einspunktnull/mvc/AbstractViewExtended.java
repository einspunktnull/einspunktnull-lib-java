package net.einspunktnull.mvc;

import net.einspunktnull.events.PropertyAccessEvent;

public abstract class AbstractViewExtended extends AbstractView
{

	private static final long serialVersionUID = -6883709138838346691L;


	protected boolean isReady = false;
	
	public abstract void modelPropertyAccess(PropertyAccessEvent evt);
	
	public abstract void init();

	public abstract void reset();

	public boolean isReady()
	{
		return isReady ;
	}

}
