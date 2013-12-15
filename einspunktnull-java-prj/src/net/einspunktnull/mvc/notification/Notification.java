package net.einspunktnull.mvc.notification;

public class Notification
{

	private String type;
	private Object source;
	private Object data;

	public Notification(String type, Object source, Object data)
	{
		this.setType(type);
		this.setSource(source);
		this.setData(data);
	}

	public Notification(String type)
	{
		this.setType(type);
	}

	public Notification()
	{
	}

	public Object getData()
	{
		return data;
	}

	public Object getSource()
	{
		return source;
	}

	public String getType()
	{
		return type;
	}

	public void setType(String type)
	{
		this.type = type;
	}

	public void setSource(Object source)
	{
		this.source = source;
	}

	public void setData(Object data)
	{
		this.data = data;
	}

}
