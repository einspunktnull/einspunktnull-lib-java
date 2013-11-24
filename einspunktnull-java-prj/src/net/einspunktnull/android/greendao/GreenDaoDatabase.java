package net.einspunktnull.android.greendao;

import java.util.ArrayList;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.AbstractDaoMaster;
import de.greenrobot.dao.AbstractDaoSession;

public abstract class GreenDaoDatabase
{

	protected SQLiteDatabase db;
	protected AbstractDaoMaster daoMaster;
	protected AbstractDaoSession daoSession;

	private static ArrayList<AbstractDao<Object, Long>> daos = new ArrayList<AbstractDao<Object, Long>>();

	abstract protected void create(Context applicatonContext);

	public GreenDaoDatabase(Context applicatonContext)
	{
		create(applicatonContext);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected void addDao(AbstractDao dao)
	{
		daos.add(dao);
	}

}
