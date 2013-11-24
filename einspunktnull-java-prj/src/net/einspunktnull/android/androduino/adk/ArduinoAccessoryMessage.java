package net.einspunktnull.android.androduino.adk;

public class ArduinoAccessoryMessage
{

	private byte _id;
	private byte _v1;
	private byte _v2;

	public ArduinoAccessoryMessage(byte id, byte v1, byte v2)
	{
		_id = id;
		_v1 = v1;
		_v2 = v2;
	}

	public byte getID()
	{
		return _id;
	}

	public byte getV1()
	{
		return _v1;
	}

	public byte getV2()
	{
		return _v2;
	}

}
