package net.einspunktnull.io;

public class Sysout
{

	public static void println(Object... objects)
	{
		System.out.println(objPrintString(objects));
	}

	public static void print(Object... objects)
	{
		System.out.print(objPrintString(objects));
	}

	private static String objPrintString(Object[] objects)
	{
		String out = "";
		for (int i = 0; i < objects.length; i++)
		{
			Object obj = objects[i];
			out += (i == 0) ? "" : " ";
			out += obj;
		}
		return out;
	}

}
