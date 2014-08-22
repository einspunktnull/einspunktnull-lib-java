package net.einspunktnull.mvc.view;

import java.awt.Component;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import net.einspunktnull.mvc.facade.Facade;
import net.einspunktnull.mvc.notification.Notification;

public abstract class Mediator implements IMediator, ActionListener
{

	protected String name;
	protected Facade facade;
	protected ArrayList<String> notificationInterests;
	protected Component viewComponent;

	public abstract void onNotification(String type, Object source, Object data);

	public abstract void onRegistered();

	public abstract void initComponents();

	public Mediator(String name)
	{
		this.name = name;
		notificationInterests = new ArrayList<String>();
	}

	public Mediator(String name, Component viewComponent)
	{
		this(name);
		this.viewComponent = viewComponent;
	}

	public void register()
	{
		onRegistered();
		initComponents();
	}

	public Component getViewComponent()
	{
		return viewComponent;
	}

	public void setViewComponent(Component viewComponent)
	{
		this.viewComponent = viewComponent;
	}

	/***************************************************************
	 * NOTIFICATION SEND AND RECEIVE
	 ****************************************************************/
	protected void sendNotfication(Notification noti)
	{
		facade.sendNotification(noti);
	}

	@Override
	public void gotNotification(Notification noti)
	{
		onNotification(noti.getType(), noti.getSource(), noti.getData());
	}

	/***************************************************************
	 * NOTIFICATION INTERESTS
	 ****************************************************************/
	@Override
	public ArrayList<String> getNotificationInterests()
	{
		return notificationInterests;
	}

	protected void addNotificationInterest(String notiType)
	{
		if (!notificationInterests.contains(notiType)) notificationInterests.add(notiType);
	}

	protected void removeNotificationInterest(String notiType)
	{
		if (notificationInterests.contains(notiType)) notificationInterests.remove(notiType);
	}

	/***************************************************************
	 * MVC STUFF
	 ****************************************************************/
	@Override
	public void setFacade(Facade facade)
	{
		this.facade = facade;
	}
}
