package net.einspunktnull.math;

import java.util.ArrayList;

public class MathE
{
	public static double deg2rad(double deg)
	{
		return deg / 180 * Math.PI;
	}

	public static double rad2deg(double rad)
	{
		return rad * 180 / Math.PI;
	}

	public static ArrayList<Double> getQuotientsWithoutRemainder(double dividend, double divisor, double minimum, double maximum)
	{
		ArrayList<Double> matches = new ArrayList<Double>();
		for (double i = divisor; i <= maximum; i += divisor)
		{
			if (dividend % i == 0 && i >= minimum) matches.add(i);
		}
		return matches;
	}

	public static ArrayList<Integer> getQuotientsWithoutRemainder(int dividend, int divisor, int minimum, int maximum)
	{
		ArrayList<Integer> matches = new ArrayList<Integer>();
		for (int i = divisor; i <= maximum; i += divisor)
		{
			if (dividend % i == 0 && i >= minimum) matches.add(i);
		}
		return matches;
	}

	public static ArrayList<Integer> getMaxY(int yStepSize, int minimum, int maximum)
	{
		ArrayList<Integer> matches = new ArrayList<Integer>();
		for (int i = minimum; i <= maximum; i += yStepSize)
		{
			if (i <= maximum) matches.add(i);
		}
		return matches;
	}
	
	public static int minMaxCutOff(int val, int minimum, int maximum)
	{
		if(val>maximum)val=maximum;
		if(val<minimum)val=minimum;
		return val;
	}
	

}
