package net.einspunktnull.preferences;

import java.io.IOException;
import java.io.Serializable;
import java.util.prefs.Preferences;

import net.einspunktnull.io.Sysout;
import net.einspunktnull.util.Serializer;

public class FilePreferenzes
{

	private Preferences preferences;

	public FilePreferenzes(String filename)
	{
		System.setProperty("java.util.prefs.PreferencesFactory", FilePreferencesFactory.class.getName());
		System.setProperty(FilePreferencesFactory.SYSTEM_PROPERTY_FILE, filename);
		preferences = Preferences.userNodeForPackage(this.getClass());
	}

	/*
	 * SETTER
	 */
	public void setString(String key, String value)
	{
		Sysout.println("SET >>>", key, value);
		preferences.put(key, value);
	}

	public void setBoolean(String key, boolean value)
	{
		Sysout.println("SET >>>", key, value);
		preferences.putBoolean(key, value);
	}

	public void setByteArray(String key, byte[] value)
	{
		Sysout.println("SET >>>", key, value);
		preferences.putByteArray(key, value);
	}

	public void setDouble(String key, double value)
	{
		Sysout.println("SET >>>", key, value);
		preferences.putDouble(key, value);
	}

	public void setFloat(String key, float value)
	{
		Sysout.println("SET >>>", key, value);
		preferences.putFloat(key, value);
	}

	public void setInt(String key, int value)
	{
		Sysout.println("SET >>>", key, value);
		preferences.putInt(key, value);
	}

	public void setLong(String key, long value)
	{
		Sysout.println("SET >>>", key, value);
		preferences.putLong(key, value);
	}

	public void setSerializableObject(String key, Serializable object) throws IOException, ClassNotFoundException
	{
		String value = Serializer.serializeToString(object);
		setString(key, value);
	}

	/*
	 * GETTER
	 */

	public String getString(String key, String def)
	{
		String value = preferences.get(key, def);
		// Sysout.println("GET <<<",key,value);
		return value;
	}

	public boolean getBoolean(String key, boolean def)
	{
		boolean value = preferences.getBoolean(key, def);
		// Sysout.println("GET <<<",key,value);
		return value;
	}

	public byte[] getByteArray(String key, byte[] def)
	{
		byte[] value = preferences.getByteArray(key, def);
		// Sysout.println("GET <<<",key,value);
		return value;
	}

	public double getDouble(String key, double def)
	{
		double value = preferences.getDouble(key, def);
		// Sysout.println("GET <<<",key,value);
		return value;
	}

	public float getFloat(String key, float def)
	{
		float value = preferences.getFloat(key, def);
		// Sysout.println("GET <<<",key,value);
		return value;
	}

	public int getInt(String key, int def)
	{
		int value = preferences.getInt(key, def);
		// Sysout.println("GET <<<",key,value);
		return value;
	}

	public long getLong(String key, long def)
	{
		long value = preferences.getLong(key, def);
		// Sysout.println("GET <<<",key,value);
		return value;
	}

	public Serializable getSerializableObject(String key, Serializable object)
	{
		try
		{
			String value = getString(key, null);
			Serializable valueAsObject = Serializer.deserializeFromString(value);
			return valueAsObject;

		}
		catch (Exception e)
		{
			return null;
		}
	}
}
