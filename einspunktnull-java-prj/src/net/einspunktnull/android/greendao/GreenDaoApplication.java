package net.einspunktnull.android.greendao;

import android.app.Application;

public class GreenDaoApplication extends Application
{

	protected GreenDaoDatabase greenDaoDB;

	@Override
	public void onCreate()
	{
		super.onCreate();
	}

	public GreenDaoDatabase getDb()
	{
		return greenDaoDB;
	}

	public void setDb(GreenDaoDatabase db)
	{
		this.greenDaoDB = db;
	}

	public boolean hasDb()
	{
		return getDb() != null;
	}

}
