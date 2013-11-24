package net.einspunktnull.android.io;

import android.util.Log;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.ScrollView;
import android.widget.TextView;

public class Out
{

	private static Out instance = null;
	private static TextView tv = null;
	private static String tag = null;
	private static ScrollView parent = null;

	public static Out init(String tag, TextView tv)
	{
		Out.tag = tag;
		Out.tv = tv;
		if (tv.getParent() instanceof ScrollView)
		{
			Out.parent = (ScrollView) tv.getParent();
			Out.parent.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener()
			{

				@Override
				public void onGlobalLayout()
				{
					if (Out.parent != null)
					{
						parent.scrollTo(0, Out.tv.getMeasuredHeight());
					}
				}
			});

		}
		return getInstance();
	}

	private static Out getInstance()
	{
		if (instance == null)
		{
			if (tv != null && tag != null)
			{
				instance = new Out();
			}
		}
		return instance;
	}

	public static void d(String txt)
	{
		Log.d(tag, txt);
		String text = (String) tv.getText();
		text += "# " + txt + "\n";
		tv.setText(text);
	}

	public static void d(Object... objects)
	{
		String txt = "";
		String text = (String) tv.getText();
		for (int i = 0; i < objects.length; i++)
		{
			Object object = objects[i];
			if (i > 0) txt += " ";
			txt += object.toString();
		}
		txt += "\n";
		Log.d(tag, txt);
		text += "# " + txt;
		tv.setText(text);
	}

}
