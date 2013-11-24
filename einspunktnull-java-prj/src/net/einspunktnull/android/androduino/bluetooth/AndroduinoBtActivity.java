package net.einspunktnull.android.androduino.bluetooth;

import java.io.IOException;
import java.util.Set;

import net.einspunktnull.android.androduino.bluetooth.AndroduinoBtService.AndroduinoBtServiceBinder;
import net.einspunktnull.android.preference.sharedPrefs.SharedPrefsActivity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;

public class AndroduinoBtActivity extends SharedPrefsActivity
{

	// LOGIX
	private BroadcastReceiver receiver;
	protected AndroduinoBtService service;
	private boolean serviceBound = false;
	private IntentFilter filter;

	private ServiceConnection serviceConnection = new ServiceConnection()
	{

		@Override
		public void onServiceConnected(ComponentName name, IBinder service)
		{
			AndroduinoBtServiceBinder binder = (AndroduinoBtServiceBinder) service;
			AndroduinoBtActivity.this.service = binder.getService();
			serviceBound = true;

			AndroduinoBtActivity.this.onServiceConnected();
		}

		@Override
		public void onServiceDisconnected(ComponentName name)
		{
			AndroduinoBtActivity.this.service = null;
			serviceBound = false;
		}

	};

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		createBroadcastReceiver();
	}
	
	@Override
	protected void onPause()
	{
		super.onPause();
		unregisterReceiver(receiver);
	}
	
	@Override
	protected void onResume()
	{
		super.onResume();
		registerReceiver(receiver, filter);
	}
	

	@Override
	protected void onStart()
	{
		super.onStart();
		Intent intent = new Intent(this, AndroduinoBtService.class);
		bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
	}

	@Override
	protected void onStop()
	{
		super.onStop();
		if (serviceBound)
		{
			unbindService(serviceConnection);
			serviceBound = false;
		}
	}

	private void createBroadcastReceiver()
	{
		filter = new IntentFilter();
		// ADAPTER STATE
		filter.addAction(AndroduinoBt.Adapter.ACTION_STATE_CHANGED);
		// CONNECTION STATE
		filter.addAction(AndroduinoBt.Connection.ACTION_STATE_CHANGED);
		// DISCOVERY
		filter.addAction(AndroduinoBt.Device.ACTION_FOUND);
		filter.addAction(AndroduinoBt.Adapter.ACTION_DISCOVERY_STARTED);
		filter.addAction(AndroduinoBt.Adapter.ACTION_DISCOVERY_FINISHED);
		// COMMUNICATION
		filter.addAction(AndroduinoBt.Communication.ACTION_ARDUINO_MESSAGE_RECEIVED);

		receiver = new BroadcastReceiver()
		{

			@Override
			public void onReceive(Context context, Intent intent)
			{
				String action = intent.getAction();
				// ADAPTER STATE
				if (action.equals(AndroduinoBt.Adapter.ACTION_STATE_CHANGED))
				{
					int state = intent.getIntExtra(AndroduinoBt.Adapter.EXTRA_STATE, AndroduinoBt.Adapter.ERROR);
					onAdapterStateChanged(state);
				}
				// CONNECTION STATE
				else if (action.equals(AndroduinoBt.Connection.ACTION_STATE_CHANGED))
				{
					int state = intent.getIntExtra(AndroduinoBt.Connection.EXTRA_STATE, AndroduinoBt.Connection.ERROR);
					onConnectionStateChanged(state);
				}
				else if (action.equals(AndroduinoBt.Device.ACTION_FOUND))
				{
					BluetoothDevice device = intent.getParcelableExtra(AndroduinoBt.Device.EXTRA_DEVICE);
					onDeviceDiscovered(device);
				}
				// DISCOVERY
				else if (action.equals(AndroduinoBt.Adapter.ACTION_DISCOVERY_STARTED))
				{
					onDeviceDiscoveryStarted();
				}
				else if (action.equals(AndroduinoBt.Adapter.ACTION_DISCOVERY_FINISHED))
				{
					onDeviceDiscoveryFinished();
				}
				else if (action.equals(AndroduinoBt.Communication.ACTION_ARDUINO_MESSAGE_RECEIVED))
				{
					byte[] bytes = intent.getByteArrayExtra(AndroduinoBt.Communication.EXTRA_ARDUINO_MESSAGE_BYTES);
					AndroduinoBtMessage msg = new AndroduinoBtMessage(bytes);
					onMessageReceived(msg);
				}
			}
		};
	}

	protected void onServiceConnected()
	{
	}

	protected AndroduinoBtService getService()
	{
		return service;
	}

	protected String getRegisteredDeviceAddress()
	{
		return preferences.getString(AndroduinoBt.PREF_KEY_DEVICE_ADDRESS);
	}

	protected boolean hasRegisteredDeviceAddress()
	{
		return getRegisteredDeviceAddress() != null;
	}

	/*****************************************************************
	 * ADAPTER STATE
	 *****************************************************************/
	protected void onAdapterStateChanged(int adapterState)
	{
	}

	protected void toggleBluetoothAdapter()
	{
		if (serviceBound) service.toggleBluetoothAdapter();
	}

	protected boolean isAdapterStateTurning()
	{
		if (!serviceBound) return false;
		return service.isAdapterStateTurning();
	}

	protected boolean isAdapterStateOn()
	{
		if (!serviceBound) return false;
		return service.isAdapterStateOn();
	}

	/*****************************************************************
	 * CONNECTION
	 *****************************************************************/
	protected void onConnectionStateChanged(int connectionState)
	{
	}

	protected void toggleDeviceConnection() throws IOException
	{
		if (serviceBound) service.toggleDeviceConnection(getRegisteredDeviceAddress());
	}

	protected void connectDevice(String address) throws IOException
	{
		if (serviceBound) service.connectDevice(getRegisteredDeviceAddress());
	}

	protected void disconnectDevice(String address)
	{
		if (serviceBound) service.disconnectDevice(getRegisteredDeviceAddress());
	}

	protected boolean isDeviceConnected()
	{
		if (!serviceBound) return false;
		return service.isDeviceConnected();
	}

	protected boolean isDeviceConnectionTurning()
	{
		if (!serviceBound) return true;
		return service.isDeviceConnectionTurning();
	}

	/*****************************************************************
	 * DISCOVERY
	 *****************************************************************/
	protected Set<BluetoothDevice> getBondedDevices()
	{
		if (!serviceBound) return null;
		return service.getBondedDevices();
	}

	protected boolean isDeviceDiscovering()
	{
		if (!serviceBound) return true;
		return service.isDeviceDiscovering();
	}

	protected void startDeviceDiscovery()
	{
		if (serviceBound) service.startDeviceDiscovery();
	}

	protected void onDeviceDiscovered(BluetoothDevice device)
	{
	}

	protected void onDeviceDiscoveryStarted()
	{
	}

	protected void onDeviceDiscoveryFinished()
	{
	}

	/*****************************************************************
	 * MISC
	 *****************************************************************/

	protected BluetoothAdapter getAdapter()
	{
		if (!serviceBound) return null;
		return service.getAdapter();
	}

	protected BluetoothDevice getDevice()
	{
		if (!serviceBound) return null;
		return service.getDevice();
	}

	/*****************************************************************
	 * COMMUNICATION
	 *****************************************************************/
	protected void sendMessageToArduino(byte flag, Object... data)
	{
		AndroduinoBtMessage msg = new AndroduinoBtMessage(flag, data);
		sendMessageToArduino(msg);
	}

	protected void sendMessageToArduino(AndroduinoBtMessage msg)
	{
		sendMessageToArduino(msg.getBytes());
	}

	protected void sendMessageToArduino(byte[] bytes)
	{
		if (!serviceBound) return;
		service.sendMessageToArduino(bytes);
	}

	protected void onMessageReceived(AndroduinoBtMessage msg)
	{
	}
}
