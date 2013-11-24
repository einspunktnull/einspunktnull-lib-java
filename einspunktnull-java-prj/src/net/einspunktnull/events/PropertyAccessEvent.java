package net.einspunktnull.events;

import java.util.EventObject;

public class PropertyAccessEvent extends EventObject
{

	private static final long serialVersionUID = 8283640078933153498L;
	private Object value;
	private String propertyName;

	public PropertyAccessEvent(Object source, String propertyName, Object value)
	{
		super(source);
		this.propertyName = propertyName;
		this.value = value;
	}

	public Object getValue()
	{
		return value;
	}
	
	public Object getPropertyName()
	{
		return propertyName;
	}

}
