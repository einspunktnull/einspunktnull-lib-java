package net.einspunktnull.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.List;

import org.apache.commons.lang3.ClassUtils;

public class KlazzUtils
{

	public static Object createInstance(Class<?> clazz, Object[] argz)
	{
		Constructor<?> constructor = clazz.getConstructors()[0];
		Class<?>[] parameterTypes = constructor.getParameterTypes();
		Object[] parameters = new Object[parameterTypes.length];

		int len = parameterTypes.length;
		for (int i = 0; i < len; i++)
		{
			Class<?> paramType = parameterTypes[i];
			Object arg = argz[i];

			boolean needsToBeCasted = true;
			if (arg instanceof Long && !paramType.equals(Long.class)) needsToBeCasted = false;
			else if (arg instanceof Integer && !paramType.equals(Integer.class)) needsToBeCasted = false;

			parameters[i] = needsToBeCasted ? paramType.cast(arg) : arg;
		}

		Object retObj = null;
		try
		{
			retObj = constructor.newInstance(parameters);
		}
		catch (InvocationTargetException e)
		{
			e.printStackTrace();
		}
		catch (IllegalAccessException e)
		{
			e.printStackTrace();
		}
		catch (InstantiationException e)
		{
			e.printStackTrace();
		}
		catch (IllegalArgumentException e)
		{
			e.printStackTrace();
		}

		return retObj;
	}

	public static Object findByClass(Class<?> clazz, Collection<?> instances)
	{

		for (Object instance : instances)
		{
			Class<?> instanceClass = instance.getClass();

			if (instanceClass.equals(clazz)) return instance;

			List<Class<?>> superClasses = ClassUtils.getAllSuperclasses(instanceClass);

			for (Class<?> superClass : superClasses)
			{
				if (superClass.equals(clazz)) return instance;
			}
		}

		return null;
	}

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
