package net.einspunktnull.util;

import java.io.IOException;
import java.io.Serializable;

public class Serializer
{

	public static String serializeToString(Serializable serializable) throws IOException
	{
		return Base64.encodeObject(serializable);
	}

	public static Serializable deserializeFromString(String serialized) throws IOException, ClassNotFoundException
	{
		return (Serializable) Base64.decodeToObject(serialized);
	}
}
