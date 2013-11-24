package net.einspunktnull.util;


public class ConvertUtil
{

	public static byte[] toBytes(Object obj)
	{
		String byteTxt = obj.toString();
		return byteTxt.getBytes();
	}
}
