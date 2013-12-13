package net.einspunktnull.mvc.view;

import java.util.ArrayList;

import net.einspunktnull.mvc.facade.Facade;
import net.einspunktnull.mvc.notification.Notification;

public interface IMediator
{

	public void register();

	public void setFacade(Facade facade);

	public void gotNotification(Notification noti);

	public ArrayList<String> getNotificationInterests();

}
