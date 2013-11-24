package net.einspunktnull.date;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil
{

	public static final int SECOND_IN_MILLISECONDS = 1000;
	public static final int MINUTE_IN_MILLISECONDS = 60 * SECOND_IN_MILLISECONDS;
	public static final int HOUR_IN_MILLISECONDS = 60 * MINUTE_IN_MILLISECONDS;
	public static final int DAY_IN_MILLISECONDS = 24 * HOUR_IN_MILLISECONDS;
	public static final int WEEK_IN_MILLISECONDS = 7 * DAY_IN_MILLISECONDS;

	public static Date string2Date(String dateString, String dateFormat)
	{
		SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
		try
		{
			return sdf.parse(dateString);
		}
		catch (ParseException e)
		{
			return null;
		}
	}

	public static Date dayBefore(Date date)
	{
		return new Date(date.getTime() - DAY_IN_MILLISECONDS);
	}

	public static Date dayAfter(Date date)
	{
		return new Date(date.getTime() + DAY_IN_MILLISECONDS);
	}

	public static long numDaysBetween(Date from, Date to)
	{
		return (to.getTime() - from.getTime()) / DateUtil.DAY_IN_MILLISECONDS;
	}

	public static long daysBetweenAbs(Date from, Date to)
	{
		return Math.abs(numDaysBetween(from, to));
	}

	public static void floorToSeconds(Date date)
	{
		long oldMillis = date.getTime();
		long newMillis = ((long) (Math.floor(oldMillis / SECOND_IN_MILLISECONDS))) * SECOND_IN_MILLISECONDS;
		date.setTime(newMillis);
	}

	public static Date floorDay(Date date)
	{
		return floor(date, 4);
	}

	private static Date floor(Date date, int depth)
	{
		Date newDate = (Date) date.clone();
		if (depth >= 4) newDate.setHours(0);
		if (depth >= 3) newDate.setMinutes(0);
		if (depth >= 2) newDate.setSeconds(0);
		if (depth >= 1) floorToSeconds(newDate);
		return newDate;

	}

	public static Date[] getDaysFromTo(Date from, Date to)
	{
		if (to.before(from)) return null;
		int daysBetween = (int) numDaysBetween(from, to);
		int daysInclusiveToday = daysBetween + 1;
		Date[] dates = new Date[daysInclusiveToday];
		long millis = from.getTime();
		for (int i = 0; i < daysInclusiveToday; i++)
		{
			long dateMillis = millis + (DAY_IN_MILLISECONDS * i);
			dates[i] = new Date(dateMillis);
		}
		return dates;
	}

}
