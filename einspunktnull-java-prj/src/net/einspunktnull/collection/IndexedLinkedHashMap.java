package net.einspunktnull.collection;

import java.util.LinkedHashMap;
import java.util.Map;

public class IndexedLinkedHashMap<K, V> extends LinkedHashMap<K, V>
{

	private static final long serialVersionUID = 7864817944875625263L;

	public V getValueByIndex(int idx)
	{
		for (Map.Entry<K, V> entry : this.entrySet())
		{
			if (idx-- == 0)
			{
				V value = entry.getValue();
				return value;
			}
		}
		return null;
	}
	
	public K getKeyByIndex(int idx)
	{
		for (Map.Entry<K, V> entry : this.entrySet())
		{
			if (idx-- == 0)
			{
				K key = entry.getKey();
				return key;
			}
		}
		return null;
	}
	
	
}
