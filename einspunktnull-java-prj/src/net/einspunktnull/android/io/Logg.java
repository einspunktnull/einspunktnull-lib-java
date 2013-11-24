package net.einspunktnull.android.io;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;

import net.einspunktnull.util.Klass;

import android.util.Log;

public class Logg
{

	public static void d(String TAG, Object... objects)
	{
		Log.d(TAG, getStrings(false, objects));
	}

	public static void cd(String TAG, String channel, Object[] objects)
	{
		Log.d(TAG, "[" + channel + "] " + getStrings(false, objects));
	}

	public static void dr(String TAG, Object... objects)
	{
		Log.d(TAG, getStrings(true, objects));
	}

	public static void cdr(String TAG, String channel, Object[] objects)
	{
		Log.d(TAG, channel + " " + getStrings(true, objects));
	}

	public static void e(String TAG, Object... objects)
	{
		Log.e(TAG, getStrings(false, objects));
	}

	public static void ce(String TAG, String channel, Object[] objects)
	{
		Log.e(TAG, "[" + channel + "] " + getStrings(false, objects));
	}

	public static void er(String TAG, Object... objects)
	{
		Log.e(TAG, getStrings(true, objects));
	}

	public static void i(String TAG, Object... objects)
	{
		Log.i(TAG, getStrings(false, objects));
	}

	public static void ir(String TAG, Object... objects)
	{
		Log.i(TAG, getStrings(true, objects));
	}

	public static void v(String TAG, Object... objects)
	{
		Log.v(TAG, getStrings(false, objects));
	}

	public static void vr(String TAG, Object... objects)
	{
		Log.v(TAG, getStrings(true, objects));
	}

	public static void w(String TAG, Object... objects)
	{
		Log.w(TAG, getStrings(false, objects));
	}

	public static void wr(String TAG, Object... objects)
	{
		Log.w(TAG, getStrings(true, objects));
	}

	public static void wtf(String TAG, Object... objects)
	{
		Log.wtf(TAG, getStrings(false, objects));
	}

	public static void wtfr(String TAG, Object... objects)
	{
		Log.wtf(TAG, getStrings(true, objects));
	}

	private static String getStrings(boolean extra, Object[] objects)
	{
		String txt = "";
		for (int i = 0; i < objects.length; i++)
		{
			Object obj = objects[i];
			String objString = extra ? getObjAsString(obj) : obj != null ? obj.toString() : "null";
			txt += i == 0 ? objString : " " + objString;
		}
		return txt;
	}

	@SuppressWarnings("unchecked")
	public static String getObjAsString(Object obj)
	{
		if (obj == null) return "null";
		if (obj instanceof String) { return (String) obj; }
		if (obj instanceof Boolean || obj instanceof Double || obj instanceof Float || obj instanceof Integer) { return String.valueOf(obj); }
		String tab = "    ";
		String result = "class " + Klass.getClassSimpleName(obj) + "\n\r";
		Field[] fields = obj.getClass().getFields();
		for (Field field : fields)
		{
			result += tab + field.getType().getSimpleName() + " " + field.getName() + "\n\r";
		}
		Method[] methods = obj.getClass().getMethods();
		for (Method method : methods)
		{
			result += tab + method.getReturnType().getSimpleName() + " " + method.getName() + "(";
			Type[] types = method.getParameterTypes();
			for (int i = 0; i < types.length; i++)
			{
				result += ((Class<? extends Object>) types[i]).getSimpleName() + " arg" + i;
				if (i < types.length - 1) result += ", ";
			}
			result += ")\n\r";
		}
		return result;

	}

}
