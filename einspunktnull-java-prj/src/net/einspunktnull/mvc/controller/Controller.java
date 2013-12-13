package net.einspunktnull.mvc.controller;

import java.lang.reflect.Method;

import net.einspunktnull.mvc.facade.Facade;
import net.einspunktnull.mvc.model.Model;

import org.apache.commons.lang3.reflect.MethodUtils;

public abstract class Controller
{

	protected Facade facade;

	public void setFacade(Facade facade)
	{
		this.facade = facade;
	}

	public abstract void onRegistered();

	public void register()
	{
		onRegistered();
	}

	public void processCommand(Class<?> tgtCls, String cmd, Object... args)
	{
		Class<?>[] argClasses = new Class<?>[args.length];

		for (int i = 0; i < args.length; i++)
		{
			Object arg = args[i];
			argClasses[i] = arg.getClass();
		}

		for (Model model : facade.getModels())
		{
			Class<?> cls = model.getClass();

			if (cls.equals(tgtCls))
			{
				Method method = MethodUtils.getMatchingAccessibleMethod(cls, cmd, argClasses);
				try
				{
					method.invoke(model, args);
				}
				catch (Exception ex)
				{
				}
			}
		}
	}

}
