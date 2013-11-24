package net.einspunktnull.collection;

import java.util.HashMap;

public class BilateralHashMap<Key, Value>
{

	private HashMap<Key, Value> mapKeyValue = new HashMap<Key, Value>();
	private HashMap<Value, Key> mapValueKey = new HashMap<Value, Key>();


	public void put(Key key, Value value)
	{
		mapKeyValue.put(key, value);
		mapValueKey.put(value, key);
	}

	public Value getValue(Key key)
	{
		return mapKeyValue.get(key);
	}

	public Key getKey(Value value)
	{
		return mapValueKey.get(value);
	}
	
	public int size()
	{
		return mapValueKey.size();
	}
}
