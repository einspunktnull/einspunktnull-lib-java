package net.einspunktnull.android.preference.sharedPrefs;

import net.einspunktnull.android.activity.EinspunktnullActivity;
import android.app.Application;
import android.os.Bundle;

public class SharedPrefsActivity extends EinspunktnullActivity
{

	protected SharedPrefs preferences;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		Application app = getApplication();
		preferences = new SharedPrefs(app);
	}

}
