package net.einspunktnull.util;

public class CollectionUtil
{

	public static String[] join(String[]... parms)
	{
		int size = 0;
		for (String[] array : parms)
		{
			size += array.length;
		}

		String[] result = new String[size];

		int j = 0;
		for (String[] array : parms)
		{
			for (String s : array)
			{
				result[j++] = s;
			}
		}
		return result;
	}

	public static byte[] join(byte[]... parms)
	{
		int size = 0;
		for (byte[] array : parms)
		{
			size += array.length;
		}

		byte[] result = new byte[size];

		int j = 0;
		for (byte[] array : parms)
		{
			for (byte s : array)
			{
				result[j++] = s;
			}
		}
		return result;
	}
}
