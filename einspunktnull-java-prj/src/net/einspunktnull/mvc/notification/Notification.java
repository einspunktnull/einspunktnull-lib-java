package net.einspunktnull.mvc.notification;

public class Notification
{

	public static final class Communication
	{

		public static final class Java
		{

			public static final String AVAILABLE_PORTS = "AVAILABLE_PORTS";
		};
		public static final String CONNECTION_STATE_CHANGED = "CONNECTION_STATE_CHANGED";
		public static final String MESSAGE_FROM_STORAGE = "MESSAGE_FROM_STORAGE";
	}

	public static final class Settings
	{

		public static final String SETTINGS_CHANGED = "SETTINGS_CHANGED";
	}

	public static final class Modules
	{

		public static final String MODULES_LOADED = "MODULES_LOADED";
		public static final String MODULE_ENABLED = "MODULE_ENABLED";
		public static final String MODULE_DISABLED = "MODULE_DISABLED";

	}
	public static final class Application
	{

		public static final String INITIALIZE_VIEW = "INITIALIZE_VIEW";

	}

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
