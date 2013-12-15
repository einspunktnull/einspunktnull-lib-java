package net.einspunktnull.android.activity;

import net.einspunktnull.android.io.Logg;
import net.einspunktnull.util.KlazzUtils;
import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class EinspunktnullActivity extends Activity
{

	protected String pkgName;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		Application app = getApplication();
		pkgName = app.getPackageName();
	}

	/*******************************************************
	 * GENERAL
	 ******************************************************/
	protected View getLayout(int id)
	{
		return getLayoutInflater().inflate(id, null);
	}

	protected void startActivity(Class<? extends Activity> actCls)
	{
		Intent intent = new Intent(this, actCls);
		startActivity(intent);
	}

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

}
