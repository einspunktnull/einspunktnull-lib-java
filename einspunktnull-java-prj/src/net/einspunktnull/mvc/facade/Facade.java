package net.einspunktnull.mvc.facade;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import net.einspunktnull.mvc.controller.Controller;
import net.einspunktnull.mvc.model.Model;
import net.einspunktnull.mvc.notification.Notification;
import net.einspunktnull.mvc.view.IMediator;
import net.einspunktnull.util.KlazzUtils;

public class Facade
{

	private HashMap<Class<Model>, Model> models;
	private HashMap<Class<IMediator>, IMediator> views;
	private HashMap<Class<Controller>, Controller> controllers;

	public Facade()
	{

		models = new HashMap<Class<Model>, Model>();
		views = new HashMap<Class<IMediator>, IMediator>();
		controllers = new HashMap<Class<Controller>, Controller>();
	}

	@SuppressWarnings("unchecked")
	public void registerModel(Model model)
	{
		model.setFacade(this);
		model.register();

		models.put((Class<Model>) model.getClass(), model);
	}

	public Model getModel(Class<?> clazz)
	{
		return models.get(clazz);
	}

	@SuppressWarnings("unchecked")
	public void registerMediator(IMediator view)
	{
		view.setFacade(this);
		view.register();
		views.put((Class<IMediator>) view.getClass(), view);
	}

	public IMediator getView(Class<?> clazz)
	{
		return views.get(clazz);
	}

	@SuppressWarnings("unchecked")
	public void registerController(Controller controller)
	{
		controller.setFacade(this);
		controller.register();
		controllers.put((Class<Controller>) controller.getClass(), controller);

	}

	public Controller getController(Class<?> clazz)
	{
		return controllers.get(clazz);
	}

	public void sendNotification(Notification noti)
	{
		for (IMediator view : views.values())
		{
			ArrayList<String> notiInterests = view.getNotificationInterests();
			if (notiInterests == null) continue;
			if (notiInterests.contains(noti.getType())) view.gotNotification(noti);
		}
	}

	public Collection<Model> getModels()
	{
		return models.values();
	}

	public Collection<IMediator> getViews()
	{
		return views.values();
	}

	public Collection<Controller> getControllers()
	{
		return controllers.values();
	}

	public Model findModelBySuperClass(Class<?> clazz)
	{
		return (Model) KlazzUtils.findByClass(clazz, models.values());

	}

}
