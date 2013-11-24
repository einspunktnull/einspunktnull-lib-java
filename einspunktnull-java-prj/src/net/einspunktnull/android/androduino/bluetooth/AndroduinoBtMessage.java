package net.einspunktnull.android.androduino.bluetooth;

import java.util.ArrayList;

import net.einspunktnull.android.io.Logg;
import net.einspunktnull.util.ConvertUtil;

public class AndroduinoBtMessage
{

	public static final byte BYTE_OUT_BREAK = 0x12;
	public static final byte BYTE_OUT_END = 0x13;
	public static final byte BYTE_OUT_DELIM = 0x14;
	public static final byte BYTE_IN_START = 0x11;

	private byte flag;
	private Object[] objData;
	private ArrayList<ArrayList<Byte>> byteData;
	private byte[] bytes;

	public AndroduinoBtMessage(byte flag, Object[] data)
	{
		this.flag = flag;
		this.objData = data;
		data2Bytes();
	}

	public AndroduinoBtMessage(byte[] bytes)
	{
		this.bytes = bytes;
		bytes2Data();
	}

	/*****************************************************************
	 * TRANSLATION
	 *****************************************************************/
	private void data2Bytes()
	{
		ArrayList<Byte> bytesList = new ArrayList<Byte>();

		bytesList.add(flag);
		for (int i = 0; i < objData.length; i++)
		{
			Object elm = objData[i];
			byte[] elmAsBytes = ConvertUtil.toBytes(elm);

			if (i > 0) bytesList.add(BYTE_OUT_DELIM);
			
			for (int j = 0; j < elmAsBytes.length; j++)
			{
				byte elm2 = elmAsBytes[j];
				bytesList.add(elm2);
			}
		}
		bytesList.add(BYTE_OUT_END);

		bytes = new byte[bytesList.size()];

		for (int k = 0; k < bytesList.size(); k++)
		{
			Byte beit = bytesList.get(k);
			bytes[k] = beit.byteValue();
		}
	}

	private void bytes2Data()
	{
		flag = bytes[0];

		// 1 TO ARRAYLIST
		byteData = new ArrayList<ArrayList<Byte>>();
		ArrayList<Byte> currObjBytes = new ArrayList<Byte>();
		for (int i = 1; i < bytes.length - 1; i++)
		{
			byte currByte = bytes[i];
			if (currByte != BYTE_OUT_DELIM)
			{
				currObjBytes.add(currByte);
			}
			else
			{
				byteData.add(currObjBytes);
				currObjBytes = new ArrayList<Byte>();
			}
		}
		if (currObjBytes.size() > 0) byteData.add(currObjBytes);
	}

	/*****************************************************************
	 * GET HELPER
	 *****************************************************************/
	private byte[] getBytesAt(int index)
	{
		ArrayList<Byte> bytes = byteData.get(index);
		int len = bytes.size();
		byte[] byteArr = new byte[len];
		for (int j = 0; j < len; j++)
		{
			byteArr[j] = bytes.get(j);
		}
		return byteArr;
	}

	/*****************************************************************
	 * GETTER
	 *****************************************************************/
	public byte[] getBytes()
	{
		return bytes;
	}

	public byte getFlag()
	{
		return flag;
	}

	/*****************************************************************
	 * GETTER TYPES
	 *****************************************************************/
	public boolean getBooleanAt(int index)
	{
		byte[] bytes = getBytesAt(index);
		String bytesTxt = new String(bytes);
		return bytesTxt.equals("1");
	}

	public boolean getBoolean()
	{
		return getBooleanAt(0);
	}

	public boolean[] getArrayBoolean()
	{
		byte[] bytes = getBytesAt(0);
		
		int len = bytes.length;
		boolean[] bools = new boolean[len];
		for (int i = 0; i < len; i++)
		{
			bools[i] = getBooleanAt(i);
		}
		return bools;
	}

}
