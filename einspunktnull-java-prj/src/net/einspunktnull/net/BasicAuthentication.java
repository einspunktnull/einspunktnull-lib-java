package net.einspunktnull.net;

import java.net.Authenticator;
import java.net.PasswordAuthentication;

public class BasicAuthentication
{

	public static void authenticate(final String user, final String passwd)
	{
		Authenticator.setDefault(new Authenticator()
		{

			protected PasswordAuthentication getPasswordAuthentication()
			{
				return new PasswordAuthentication(user, passwd.toCharArray());
			}
		});
	}
}
