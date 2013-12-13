package net.einspunktnull.util;

import java.util.Collection;
import java.util.List;

import org.apache.commons.lang3.ClassUtils;

public class KlazzUtils
{

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
}
