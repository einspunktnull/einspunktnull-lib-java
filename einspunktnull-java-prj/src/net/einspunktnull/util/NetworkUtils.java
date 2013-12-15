package net.einspunktnull.util;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Collections;
import java.util.Enumeration;

import net.einspunktnull.io.Sysout;

public class NetworkUtils
{

	public static void listNetwokInterfaces() throws SocketException
	{
		Enumeration<NetworkInterface> nets = NetworkInterface.getNetworkInterfaces();
		for (NetworkInterface netint : Collections.list(nets))
		{
			Enumeration<InetAddress> inetAddresses = netint.getInetAddresses();
			String adresses = "";
			for (InetAddress inetAddress : Collections.list(inetAddresses))
			{
				adresses += " " + inetAddress;
			}
			Sysout.println(netint.getName(), netint.getDisplayName(), adresses);
		}
	}
}
