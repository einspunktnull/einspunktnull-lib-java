package net.einspunktnull.android.greendao;

import net.einspunktnull.android.preference.sharedPrefs.SharedPrefsActivity;
import android.os.Bundle;

public abstract class GreenDaoActivity extends SharedPrefsActivity
{

	protected GreenDaoApplication applicationContext;
	private GreenDaoDatabase db;

	protected abstract GreenDaoDatabase setupGreenDaoDb(GreenDaoApplication applicationContext);

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		applicationContext = (GreenDaoApplication) getApplication();
		if (!applicationContext.hasDb())
		{
			applicationContext.setDb(setupGreenDaoDb(applicationContext));
		}

		db = applicationContext.getDb();
	}

	protected GreenDaoDatabase getDb()
	{
		return db;
	}

}
