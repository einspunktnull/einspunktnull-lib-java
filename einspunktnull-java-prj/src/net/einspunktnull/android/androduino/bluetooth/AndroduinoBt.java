package net.einspunktnull.android.androduino.bluetooth;

import java.util.UUID;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

public class AndroduinoBt
{

	/*************************************************************************
	 * CONSTANTS
	 *************************************************************************/
	public static final UUID BLUETOOTH_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
	public static final String PREF_KEY_DEVICE_ADDRESS = "device_address";

	public static final class Adapter
	{

		public static final String ACTION_CHANGE_STATE = "androduino.bluetooth.adapter.intent.action.CHANGE_STATE";
		public static final String EXTRA_TO_STATE = "androduino.bluetooth.adapter.intent.extra.TO";
		public static final int TO_STATE_ON = 1;
		public static final int TO_STATE_OFF = 2;
		public static final int TO_STATE_TOGGLE = 3;

		public static final String ACTION_STATE_CHANGED = "androduino.bluetooth.adapter.intent.action.STATE_CHANGED";
		public static final String EXTRA_STATE = "androduino.bluetooth.adapter.intent.extra.STATE";
		public static final int STATE_TURNING_ON = 1;
		public static final int STATE_ON = 2;
		public static final int STATE_TURNING_OFF = 3;
		public static final int STATE_OFF = 4;

		public static final String ACTION_START_DEVICE_DISCOVERY = "androduino.bluetooth.adapter.intent.action.START_DEVICE_DISCOVERY";
		public static final String ACTION_DISCOVERY_STARTED = "androduino.bluetooth.adapter.intent.action.DISCOVERY_STARTED";
		public static final String ACTION_DISCOVERY_FINISHED = "androduino.bluetooth.adapter.intent.action.DISCOVERY_FINISHED";

		public static final int ERROR = -1;

	}

	public static final class Connection
	{

		public static final String ACTION_CHANGE_STATE = "androduino.bluetooth.connection.intent.action.CHANGE_STATE";
		public static final String EXTRA_DEVICE_ADDRESS = "androduino.bluetooth.connection.intent.extra.DEVICE_ADRESS";
		public static final String EXTRA_TO_STATE = "androduino.bluetooth.connection.intent.extra.TO";
		public static final int TO_STATE_ON = 1;
		public static final int TO_STATE_OFF = 2;
		public static final int TO_STATE_TOGGLE = 3;

		public static final String ACTION_STATE_CHANGED = "androduino.bluetooth.connection.intent.action.STATE_CHANGED";
		public static final String EXTRA_STATE = "androduino.bluetooth.connection.intent.extra.STATE";
		public static final int STATE_TURNING_ON = 1;
		public static final int STATE_ON = 2;
		public static final int STATE_TURNING_OFF = 3;
		public static final int STATE_OFF = 4;

		public static final int ERROR = -1;

	}

	public static final class Device
	{

		public static final String ACTION_FOUND = "androduino.bluetooth.device.intent.action.FOUND";
		public static final String EXTRA_DEVICE = "androduino.bluetooth.device.intent.extra.DEVICE";
	}

	public static final class Communication
	{

		public static final String ACTION_ARDUINO_MESSAGE_SEND = "androduino.bluetooth.communication.intent.action.ARDUINO_MESSAGE_SEND";
		public static final String ACTION_ARDUINO_MESSAGE_RECEIVED = "androduino.bluetooth.communication.intent.action.ARDUINO_MESSAGE_RECEIVED";
		public static final String EXTRA_ARDUINO_MESSAGE_BYTES = "androduino.bluetooth.communication.intent.extra.ARDUINO_MESSAGE";
	}

	/*************************************************************************
	 * SERVICE
	 *************************************************************************/
	public static void startService(Activity activity)
	{
		Intent intent = new Intent(activity, AndroduinoBtService.class);
		activity.startService(intent);
	}

	public static void stopService(Activity activity)
	{
		Intent intent = new Intent(activity, AndroduinoBtService.class);
		activity.stopService(intent);
	}

	/*************************************************************************
	 * BLUETOOTH STATE
	 *************************************************************************/
	public static void enableBluetooth(Context context)
	{
		broadcastBluetoothStateChangeIntent(context, Adapter.TO_STATE_ON);
	}

	public static void disableBluetooth(Context context)
	{
		broadcastBluetoothStateChangeIntent(context, Adapter.TO_STATE_OFF);
	}

	public static void toggleBluetooth(Context context)
	{
		broadcastBluetoothStateChangeIntent(context, Adapter.TO_STATE_TOGGLE);
	}

	private static void broadcastBluetoothStateChangeIntent(Context context, int to)
	{
		Intent intent = new Intent(Adapter.ACTION_CHANGE_STATE);
		intent.putExtra(Adapter.EXTRA_TO_STATE, to);
		context.sendBroadcast(intent);
	}

	/*************************************************************************
	 * CONNECTION
	 *************************************************************************/
	public static void connectDevice(Context context, String address)
	{
		broadcastConnectionStateChangeIntent(context, Connection.TO_STATE_ON, address);
	}

	public static void disconnectDevice(Context context, String address)
	{
		broadcastConnectionStateChangeIntent(context, Connection.TO_STATE_OFF, address);
	}

	public static void toggleDeviceConnection(Context context, String address)
	{
		broadcastConnectionStateChangeIntent(context, Connection.TO_STATE_TOGGLE, address);
	}

	private static void broadcastConnectionStateChangeIntent(Context context, int to, String address)
	{
		Intent intent = new Intent(Connection.ACTION_CHANGE_STATE);
		intent.putExtra(Connection.EXTRA_TO_STATE, to);
		intent.putExtra(Connection.EXTRA_DEVICE_ADDRESS, address);
		context.sendBroadcast(intent);
	}

	/*************************************************************************
	 * DEVICE DISCOVERY
	 *************************************************************************/
	public static void startDeviceDiscovery(Context context)
	{
		Intent intent = new Intent(Adapter.ACTION_START_DEVICE_DISCOVERY);
		context.sendBroadcast(intent);
	}

	/*************************************************************************
	 * COMMUNICATION
	 *************************************************************************/

	public static void sendMessageToArduino(Context context, byte flag, Object... data)
	{
		AndroduinoBtMessage msg = new AndroduinoBtMessage(flag, data);
		sendMessageToArduino(context, msg);
	}

	public static void sendMessageToArduino(Context context, AndroduinoBtMessage msg)
	{
		sendMessageToArduino(context, msg.getBytes());
	}
	
	public static void sendMessageToArduino(Context context, byte[] bytes)
	{
		Intent intent = new Intent(Communication.ACTION_ARDUINO_MESSAGE_SEND);
		intent.putExtra(Communication.EXTRA_ARDUINO_MESSAGE_BYTES, bytes);
		context.sendBroadcast(intent);
	}

}
