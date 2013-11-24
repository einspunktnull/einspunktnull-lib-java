package net.einspunktnull.android.preference.sharedPrefs;

import net.einspunktnull.android.service.EinspunktnullService;
import android.app.Application;
import android.content.Intent;
import android.os.IBinder;


public abstract class SharedPrefsService extends EinspunktnullService
{

	protected SharedPrefs preferences;
	
	@Override
	public void onCreate()
	{
		super.onCreate();
		Application app = getApplication();
		preferences = new SharedPrefs(app);
	}

	public abstract IBinder onBind(Intent intent);
	

}
