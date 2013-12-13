package net.einspunktnull.mvc.model;

import java.util.ArrayList;

import net.einspunktnull.mvc.controller.Controller;
import net.einspunktnull.mvc.facade.Facade;
import net.einspunktnull.mvc.notification.Notification;

public abstract class Model
{

	protected ArrayList<Controller> controllers;
	protected Facade facade;

	public Model()
	{
		controllers = new ArrayList<Controller>();
	}

	protected void sendNotification(Notification noti)
	{
		facade.sendNotification(noti);
	}

	protected Model receiveModel(Class<Model> modelClass)
	{
		return null;
	}

	public abstract void initialize();

	public void setFacade(Facade facade)
	{
		this.facade = facade;
	}

	protected Model getModel(Class<?> clazz)
	{
		return facade.getModel(clazz);
	}

	public abstract void onRegistered();

	public void register()
	{
		onRegistered();
	}
}
