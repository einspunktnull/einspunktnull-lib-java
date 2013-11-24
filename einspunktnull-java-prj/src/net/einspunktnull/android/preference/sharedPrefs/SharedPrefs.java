package net.einspunktnull.android.preference.sharedPrefs;

import java.io.IOException;
import java.io.Serializable;
import java.util.Map;

import net.einspunktnull.util.Serializer;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class SharedPrefs
{

	public static final int MODE_PRIVATE = Context.MODE_PRIVATE;
	public static final int MODE_APPEND = Context.MODE_APPEND;
	public static final int MODE_WORLD_READABLE = Context.MODE_WORLD_READABLE;
	public static final int MODE_WORLD_WRITEABLE = Context.MODE_WORLD_WRITEABLE;

	private Context ctx;
	private SharedPreferences sp;
	private SharedPreferences.Editor editor;

	public SharedPrefs(Context ctx, String prefsName, int mode)
	{
		this(ctx, ctx.getSharedPreferences(prefsName, mode));
	}

	public SharedPrefs(Context ctx)
	{
		this(ctx, PreferenceManager.getDefaultSharedPreferences(ctx));
	}

	public SharedPrefs(Context ctx, SharedPreferences sp)
	{
		this.ctx = ctx;
		this.sp = sp;
		this.editor = sp.edit();
	}

	public synchronized Context getContext()
	{
		return ctx;
	}

	public synchronized void setSerializableObject(String key, Serializable object) throws IOException, ClassNotFoundException
	{
		String value = Serializer.serializeToString(object);
		setString(key, value);
	}

	public synchronized Serializable getSerializableObject(String key) throws IOException, ClassNotFoundException
	{
		String value = getString(key);
		Serializable valueAsObject = Serializer.deserializeFromString(value);
		return valueAsObject;
	}

	public synchronized void setString(String key, String value)
	{
		editor.putString(key, value);
		editor.commit();
	}

	public synchronized String getString(String key)
	{
		return sp.getString(key, null);
	}

	public synchronized void setBoolean(String key, boolean value)
	{
		editor.putBoolean(key, value);
		editor.commit();
	}

	public synchronized boolean getBoolean(String key)
	{
		return sp.getBoolean(key, false);
	}

	public synchronized void setFloat(String key, float value)
	{
		editor.putFloat(key, value);
		editor.commit();
	}

	public synchronized float getFLoat(String key)
	{
		return sp.getFloat(key, 0f);
	}

	public synchronized void setInt(String key, int value)
	{
		editor.putInt(key, value);
		editor.commit();
	}

	public synchronized int getInt(String key)
	{
		return sp.getInt(key, 0);
	}

	public synchronized void setLong(String key, long value)
	{
		editor.putLong(key, value);
		editor.commit();
	}

	public synchronized long getLong(String key)
	{
		return sp.getLong(key, 0L);
	}

	public synchronized Map<String, ?> getAll()
	{
		return sp.getAll();
	}
}
