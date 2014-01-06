package net.einspunktnull.preferences;

import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

public class FilePreferenzes
{

	public FilePreferenzes(String filename)
	{
		// Prefences logics used by SettigsModel
		System.setProperty("java.util.prefs.PreferencesFactory", FilePreferencesFactory.class.getName());
		System.setProperty(FilePreferencesFactory.SYSTEM_PROPERTY_FILE, filename);

		try
		{
			Preferences p = Preferences.userNodeForPackage(this.getClass());
			for (String s : p.keys())
			{
				System.out.println("p[" + s + "]=" + p.get(s, null));
			}
			p.putBoolean("hi", true);
			p.put("Number", String.valueOf(System.currentTimeMillis()));
		}
		catch (BackingStoreException e)
		{
			e.printStackTrace();
		}

	}
}
