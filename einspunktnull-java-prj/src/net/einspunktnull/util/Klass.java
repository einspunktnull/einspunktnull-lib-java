package net.einspunktnull.util;

import java.util.Collection;

public class Klass
{

	public static String getClassName(Object obj)
	{
		return obj.getClass().getName();
	}
	
	public static String getClassSimpleName(Object obj)
	{
		return obj.getClass().getSimpleName();
	}
	
	public static String getSuperClassName(Object obj)
	{
		return obj.getClass().getSuperclass().getName();
	}
	
	public static String getSuperClassSimpleName(Object obj)
	{
		return obj.getClass().getSuperclass().getSimpleName();
	}
	
}
