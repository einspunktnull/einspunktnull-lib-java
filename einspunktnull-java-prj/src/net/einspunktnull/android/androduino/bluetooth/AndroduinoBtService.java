package net.einspunktnull.android.androduino.bluetooth;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Set;

import net.einspunktnull.android.service.EinspunktnullService;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Binder;
import android.os.IBinder;

public class AndroduinoBtService extends EinspunktnullService
{

	private BroadcastReceiver receiver;
	private BluetoothAdapter adapter;
	private int adapterState;
	private int connectionState = AndroduinoBt.Connection.STATE_OFF;
	private final IBinder binder = new AndroduinoBtServiceBinder();
	public BluetoothDevice device;
	public BluetoothSocket socket;
	private ConnectThread connectThread;
	public ConnectedThread connectedThread;

	public class AndroduinoBtServiceBinder extends Binder
	{

		public AndroduinoBtService getService()
		{
			return AndroduinoBtService.this;
		}
	}

	/*************************************************************************
	 * GENERAL
	 *************************************************************************/

	@Override
	public void onCreate()
	{
		super.onCreate();

		setupBluetooth();
		initBroadcastReceiver();
	}

	
	@Override
	public void onDestroy()
	{
		super.onDestroy();
		unregisterReceiver(receiver);
	}

	@Override
	public IBinder onBind(Intent intent)
	{
		return binder;
	}

	private void setupBluetooth()
	{
		adapter = BluetoothAdapter.getDefaultAdapter();
		switch (adapter.getState())
		{
			case BluetoothAdapter.STATE_OFF:
				adapterState = AndroduinoBt.Adapter.STATE_OFF;
				break;
			case BluetoothAdapter.STATE_ON:
				adapterState = AndroduinoBt.Adapter.STATE_ON;
				break;
			case BluetoothAdapter.STATE_TURNING_OFF:
				adapterState = AndroduinoBt.Adapter.STATE_TURNING_OFF;
				break;
			case BluetoothAdapter.STATE_TURNING_ON:
				adapterState = AndroduinoBt.Adapter.STATE_TURNING_ON;
				break;
		}
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId)
	{
		return START_STICKY;
	}

	/*************************************************************************
	 * EVENTFICKE
	 *************************************************************************/
	private void initBroadcastReceiver()
	{
		IntentFilter filter = new IntentFilter();
		// ADAPTER STATE
		filter.addAction(AndroduinoBt.Adapter.ACTION_CHANGE_STATE);
		filter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
		// CONNECTION STATE
		filter.addAction(AndroduinoBt.Connection.ACTION_CHANGE_STATE);
		// DISCOVERY
		filter.addAction(AndroduinoBt.Adapter.ACTION_START_DEVICE_DISCOVERY);
		filter.addAction(BluetoothDevice.ACTION_FOUND);
		filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
		filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
		// COMMUNICATION
		filter.addAction(AndroduinoBt.Communication.ACTION_ARDUINO_MESSAGE_SEND);

		receiver = new BroadcastReceiver()
		{

			@Override
			public void onReceive(Context context, Intent intent)
			{
				final String action = intent.getAction();
				if (action == null) return;

				// ADAPTER STATE
				if (action.equals(AndroduinoBt.Adapter.ACTION_CHANGE_STATE))
				{
					int toState = intent.getIntExtra(AndroduinoBt.Adapter.EXTRA_TO_STATE, AndroduinoBt.Adapter.ERROR);
					onBluetoothAdapterChangeState(toState);
				}
				else if (action.equals(BluetoothAdapter.ACTION_STATE_CHANGED))
				{
					int currState = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR);
					onBluetoothAdapterStateHasChanged(currState);
				}
				// CONNECTION STATE
				else if (action.equals(AndroduinoBt.Connection.ACTION_CHANGE_STATE))
				{
					String address = intent.getStringExtra(AndroduinoBt.Connection.EXTRA_DEVICE_ADDRESS);
					int toState = intent.getIntExtra(AndroduinoBt.Connection.EXTRA_TO_STATE, AndroduinoBt.Connection.ERROR);
					onDeviceConnectionChangeState(address, toState);
				}
				// DISCOVERY
				else if (action.equals(AndroduinoBt.Adapter.ACTION_START_DEVICE_DISCOVERY))
				{
					startDeviceDiscovery();
				}
				else if (action.equals(BluetoothAdapter.ACTION_DISCOVERY_STARTED))
				{
					broadcastDeviceDiscoveryStarted();
				}
				else if (action.equals(BluetoothAdapter.ACTION_DISCOVERY_FINISHED))
				{
					broadcastDeviceDiscoveryFinished();
				}
				else if (action.equals(BluetoothDevice.ACTION_FOUND))
				{
					BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
					broadcastDeviceDiscovered(device);
				}
				// COMMUNICATION
				else if (action.equals(AndroduinoBt.Communication.ACTION_ARDUINO_MESSAGE_SEND))
				{
					byte[] msgBytes = intent.getByteArrayExtra(AndroduinoBt.Communication.EXTRA_ARDUINO_MESSAGE_BYTES);
					sendMessageToArduino(msgBytes);
				}
			}

		};
		registerReceiver(receiver, filter);
	}

	/*************************************************************************
	 * ADAPTER STATE
	 *************************************************************************/
	private void onBluetoothAdapterChangeState(int toState)
	{
		switch (toState)
		{
			case AndroduinoBt.Adapter.TO_STATE_ON:
				enableBluetoothAdapter();
				break;
			case AndroduinoBt.Adapter.TO_STATE_OFF:
				disableBluetoothAdapter();
				break;
			case AndroduinoBt.Adapter.TO_STATE_TOGGLE:
				toggleBluetoothAdapter();
				break;
		}
	}

	public void enableBluetoothAdapter()
	{
		if (!adapter.isEnabled() && !isAdapterStateTurning()) adapter.enable();
	}

	public void disableBluetoothAdapter()
	{
		if (adapter.isEnabled() && !isAdapterStateTurning()) adapter.disable();
	}

	public void toggleBluetoothAdapter()
	{
		if (!adapter.isEnabled())
		{
			enableBluetoothAdapter();
		}
		else
		{
			disableBluetoothAdapter();
		}
	}

	protected void onBluetoothAdapterStateHasChanged(int currState)
	{
		switch (currState)
		{
			case BluetoothAdapter.ERROR:
				setAndBroadcastAdapterStateHasChanged(AndroduinoBt.Adapter.ERROR);
				break;
			case BluetoothAdapter.STATE_ON:
				setAndBroadcastAdapterStateHasChanged(AndroduinoBt.Adapter.STATE_ON);
				break;
			case BluetoothAdapter.STATE_OFF:
				setAndBroadcastAdapterStateHasChanged(AndroduinoBt.Adapter.STATE_OFF);
				break;
			case BluetoothAdapter.STATE_TURNING_ON:
				setAndBroadcastAdapterStateHasChanged(AndroduinoBt.Adapter.STATE_TURNING_ON);
				break;
			case BluetoothAdapter.STATE_TURNING_OFF:
				setAndBroadcastAdapterStateHasChanged(AndroduinoBt.Adapter.STATE_TURNING_OFF);
				break;
		}
	}

	private void setAndBroadcastAdapterStateHasChanged(int state)
	{
		adapterState = state;
		Intent intent = new Intent(AndroduinoBt.Adapter.ACTION_STATE_CHANGED);
		intent.putExtra(AndroduinoBt.Adapter.EXTRA_STATE, state);
		sendBroadcast(intent);
	}

	public int getAdapterState()
	{
		return adapterState;
	}

	public boolean isAdapterStateTurning()
	{
		return adapterState == AndroduinoBt.Adapter.STATE_TURNING_OFF || adapterState == AndroduinoBt.Adapter.STATE_TURNING_ON;
	}

	public boolean isAdapterStateOn()
	{
		return adapterState == AndroduinoBt.Adapter.STATE_ON;
	}

	/*************************************************************************
	 * CONNECTION
	 *************************************************************************/

	private void onDeviceConnectionChangeState(String address, int toState)
	{
		switch (toState)
		{
			case AndroduinoBt.Connection.TO_STATE_ON:
				try
				{
					connectDevice(address);
				}
				catch (IOException e)
				{
					loggE(e);
				}
				break;
			case AndroduinoBt.Connection.TO_STATE_OFF:
				disconnectDevice(address);
				break;
			case AndroduinoBt.Connection.TO_STATE_TOGGLE:
				try
				{
					toggleDeviceConnection(address);
				}
				catch (IOException e)
				{
					loggE(e);
				}
				break;
		}
	}

	public void connectDevice(String address) throws IOException
	{
		if (isDeviceConnectionTurning()) return;
		connectThread = new ConnectThread(address);
		connectThread.start();
	}

	public void disconnectDevice(String address)
	{
		if (isDeviceConnectionTurning()) return;
		setAndBroadcastConnectionStateHasChanged(AndroduinoBt.Connection.STATE_TURNING_OFF);
		if (connectionState == AndroduinoBt.Connection.STATE_ON && connectedThread != null && connectedThread.isAlive())
		{
			connectedThread.stop();
		}
		try
		{
			socket.close();
		}
		catch (IOException e)
		{
			setAndBroadcastConnectionStateHasChanged(AndroduinoBt.Connection.STATE_OFF);
			return;
		}
		setAndBroadcastConnectionStateHasChanged(AndroduinoBt.Connection.STATE_OFF);
	}

	public void toggleDeviceConnection(String address) throws IOException
	{
		if (isDeviceConnectionTurning()) return;
		if (!isDeviceConnected())
		{
			connectDevice(address);
		}
		else
		{
			disconnectDevice(address);
		}
	}

	private class ConnectThread extends Thread
	{

		public ConnectThread(String address) throws IOException
		{
			device = adapter.getRemoteDevice(address);
			// Get a BluetoothSocket to connect with the given BluetoothDevice
			// MY_UUID is the app's UUID string, also used by the server code
			socket = device.createRfcommSocketToServiceRecord(AndroduinoBt.BLUETOOTH_UUID);
		}

		@Override
		public void run()
		{
			setAndBroadcastConnectionStateHasChanged(AndroduinoBt.Connection.STATE_TURNING_ON);
			adapter.cancelDiscovery();
			try
			{
				socket.connect();
			}
			catch (IOException connectException)
			{
				setAndBroadcastConnectionStateHasChanged(AndroduinoBt.Connection.STATE_OFF);
				return;
			}
			connectedThread = new ConnectedThread();
			connectedThread.start();
			setAndBroadcastConnectionStateHasChanged(AndroduinoBt.Connection.STATE_ON);
		}
	}

	private void setAndBroadcastConnectionStateHasChanged(int state)
	{
		connectionState = state;
		Intent intent = new Intent(AndroduinoBt.Connection.ACTION_STATE_CHANGED);
		intent.putExtra(AndroduinoBt.Connection.EXTRA_STATE, state);
		sendBroadcast(intent);
	}

	public int getConnectionState()
	{
		return connectionState;
	}

	public boolean isDeviceConnected()
	{
		return connectionState == AndroduinoBt.Connection.STATE_ON;
	}

	public boolean isDeviceConnectionTurning()
	{
		return connectionState == AndroduinoBt.Connection.STATE_TURNING_OFF || connectionState == AndroduinoBt.Connection.STATE_TURNING_ON;

	}

	/*************************************************************************
	 * DISCOVERY
	 *************************************************************************/

	private void broadcastDeviceDiscoveryStarted()
	{
		Intent intent = new Intent(AndroduinoBt.Adapter.ACTION_DISCOVERY_STARTED);
		sendBroadcast(intent);
	}

	private void broadcastDeviceDiscoveryFinished()
	{
		Intent intent = new Intent(AndroduinoBt.Adapter.ACTION_DISCOVERY_FINISHED);
		sendBroadcast(intent);
	}

	private void broadcastDeviceDiscovered(BluetoothDevice device)
	{
		Intent intent = new Intent(AndroduinoBt.Device.ACTION_FOUND);
		intent.putExtra(AndroduinoBt.Device.EXTRA_DEVICE, device);
		sendBroadcast(intent);
	}

	public Set<BluetoothDevice> getBondedDevices()
	{
		return adapter.getBondedDevices();
	}

	public boolean isDeviceDiscovering()
	{
		return adapter.isDiscovering();
	}

	public void startDeviceDiscovery()
	{
		adapter.startDiscovery();
	}

	/*************************************************************************
	 * COMMUNICATION
	 *************************************************************************/

	private class ConnectedThread extends Thread
	{

		private InputStream inStream;
		private OutputStream outStream;

		public ConnectedThread()
		{

			try
			{
				inStream = socket.getInputStream();
				outStream = socket.getOutputStream();
			}
			catch (IOException e)
			{
			}
		}

		public void run()
		{
			byte[] buffer = new byte[1024];
			int count = 0;

			while (true)
			{
				try
				{
					inStream.read(buffer, count, 1);
					int currByte = buffer[count];

					if (currByte == AndroduinoBtMessage.BYTE_OUT_END)
					{
						String msg = new String(buffer, 0, count + 1);
						broadcastMessageReceived(msg.getBytes());
						count = 0;
					}
					else
					{
						count++;
					}

				}
				catch (IOException e)
				{
					break;
				}
			}
		}

		/* Call this from the main activity to send data to the remote device */
		public void write(byte[] bytes)
		{
			try
			{
				outStream.write(bytes);
			}
			catch (IOException e)
			{
				loggE(e);
			}
		}
	}

	public void sendMessageToArduino(AndroduinoBtMessage msg)
	{
		sendMessageToArduino(msg.getBytes());
	}

	public void sendMessageToArduino(byte[] bytes)
	{
		if (connectionState == AndroduinoBt.Connection.STATE_ON && connectedThread != null)
		{
			connectedThread.write(bytes);
		}
	}

	public void sendMessageToArduino(byte flag, Object... data)
	{
		AndroduinoBtMessage msg = new AndroduinoBtMessage(flag, data);
		sendMessageToArduino(msg);
	}

	private void broadcastMessageReceived(byte[] bytes)
	{
		Intent intent = new Intent(AndroduinoBt.Communication.ACTION_ARDUINO_MESSAGE_RECEIVED);
		intent.putExtra(AndroduinoBt.Communication.EXTRA_ARDUINO_MESSAGE_BYTES, bytes);
		sendBroadcast(intent);
	}

	/*************************************************************************
	 * MISC
	 *************************************************************************/
	public BluetoothDevice getDevice()
	{
		return device;
	}

	public BluetoothAdapter getAdapter()
	{
		return adapter;
	}
}
