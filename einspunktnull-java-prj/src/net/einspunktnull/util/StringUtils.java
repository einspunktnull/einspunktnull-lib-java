package net.einspunktnull.util;

import java.text.SimpleDateFormat;
import java.util.Date;


public class StringUtils
{

	public static String long2ShortClassName(String className)
	{
		String[] clsNameArr = className.split("\\.");
		return clsNameArr[clsNameArr.length - 1];
	}
	
	public static String formatDate(String format, Date date)
	{
		SimpleDateFormat dateFormat = new SimpleDateFormat(format);
		return dateFormat.format(date);
	}


}
