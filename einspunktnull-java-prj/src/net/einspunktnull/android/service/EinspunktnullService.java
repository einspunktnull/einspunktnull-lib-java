package net.einspunktnull.android.service;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import net.einspunktnull.android.io.Logg;
import net.einspunktnull.util.KlazzUtils;
import android.app.Application;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public abstract class EinspunktnullService extends Service
{

	protected String pkgName;

	private NotificationManager notiMgr;

	private Method startForegroundMethod;

	private Method stopForegroundMethod;

	private static final Class[] startForegroundSignature = new Class[]
	{ int.class, Notification.class };
	private static final Class[] stopForegroundSignature = new Class[]
	{ boolean.class };

	private Object[] startForegroundArgs = new Object[2];
	private Object[] stopForegroundArgs = new Object[1];

	@Override
	public void onCreate()
	{
		super.onCreate();
		Application app = getApplication();
		pkgName = app.getPackageName();

		notiMgr = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		try
		{
			startForegroundMethod = getClass().getMethod("startForeground", startForegroundSignature);
			stopForegroundMethod = getClass().getMethod("stopForeground", stopForegroundSignature);
		}
		catch (NoSuchMethodException e)
		{
			// Running on an older platform.
			startForegroundMethod = stopForegroundMethod = null;
		}

	}

	public abstract IBinder onBind(Intent intent);

	/*******************************************************
	 * LOGGING
	 ******************************************************/
	public void loggD(Object... objects)
	{
		String channel = KlazzUtils.getClassSimpleName(this);
		logD(channel, objects);
	}

	private void logD(String channel, Object[] objects)
	{
		Logg.cd(pkgName, channel, objects);
	}

	public void loggE(Object... objects)
	{
		String channel = KlazzUtils.getClassSimpleName(this);
		logE(channel, objects);
	}

	private void logE(String channel, Object[] objects)
	{
		Logg.ce(pkgName, channel, objects);
	}

	/*******************************************************
	 * FOREGROUND SERVICE COPMATIBILITY
	 ******************************************************/

	/**
	 * This is a wrapper around the new startForeground method, using the older
	 * APIs if it is not available.
	 */
	void startForegroundCompat(int id, Notification notification)
	{
		// If we have the new startForeground API, then use it.
		if (startForegroundMethod != null)
		{
			startForegroundArgs[0] = Integer.valueOf(id);
			startForegroundArgs[1] = notification;
			try
			{
				startForegroundMethod.invoke(this, startForegroundArgs);
			}
			catch (InvocationTargetException e)
			{
				// Should not happen.
				loggD("Unable to invoke startForeground", e);
			}
			catch (IllegalAccessException e)
			{
				// Should not happen.
				loggD("Unable to invoke startForeground", e);
			}
			return;
		}

		// Fall back on the old API.
//		setForeground(true);
		notiMgr.notify(id, notification);
	}

	/**
	 * This is a wrapper around the new stopForeground method, using the older
	 * APIs if it is not available.
	 */
	void stopForegroundCompat(int id)
	{
		// If we have the new stopForeground API, then use it.
		if (stopForegroundMethod != null)
		{
			stopForegroundArgs[0] = Boolean.TRUE;
			try
			{
				stopForegroundMethod.invoke(this, stopForegroundArgs);
			}
			catch (InvocationTargetException e)
			{
				// Should not happen.
				loggD("Unable to invoke stopForeground", e);
			}
			catch (IllegalAccessException e)
			{
				// Should not happen.
				loggD("Unable to invoke stopForeground", e);
			}
			return;
		}

		// Fall back on the old API. Note to cancel BEFORE changing the
		// foreground state, since we could be killed at that point.
		notiMgr.cancel(id);
//		setForeground(false);
	}

}
