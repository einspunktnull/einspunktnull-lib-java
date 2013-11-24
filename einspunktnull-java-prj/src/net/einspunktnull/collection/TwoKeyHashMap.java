package net.einspunktnull.collection;

import java.util.Collection;
import java.util.HashMap;
import java.util.Set;

public class TwoKeyHashMap<Key1, Key2, Value>
{

	private HashMap<Key1, Value> map1 = new HashMap<Key1, Value>();
	private HashMap<Key2, Value> map2 = new HashMap<Key2, Value>();

	public void put(Key1 key1, Key2 key2, Value value)
	{
		map1.put(key1, value);
		map2.put(key2, value);
	}

	public Value getByKey1(Key1 key1)
	{
		return map1.get(key1);
	}

	public Value getByKey2(Key2 key2)
	{
		return map2.get(key2);
	}

	public int size()
	{
		return map1.size();
	}

	public Collection<Value> values()
	{
		return map1.values();
	}

	public Set<Key1> keySet1()
	{
		return map1.keySet();
	}

	public Set<Key2> keySet2()
	{
		return map2.keySet();
	}

}
