package net.einspunktnull.android.androduino.adk;

import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;


import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.ParcelFileDescriptor;
import android.util.Log;

import com.android.future.usb.UsbAccessory;
import com.android.future.usb.UsbManager;

public abstract class ArduinoAccessoryActivity extends Activity implements Runnable
{

	private static final String TAG = "ArduinoAccessory";
	private static final String ACTION_USB_PERMISSION = "net.einspunktnull.arduino.accessory.action.USB_PERMISSION";
	private UsbManager _usbManager;
	private PendingIntent _permissionIntent;
	private boolean _permissionRequestPending;
	protected UsbAccessory _accessory;
	private ParcelFileDescriptor _fileDescriptor;
	private FileInputStream _inputStream;
	private FileOutputStream _outputStream;

	private final BroadcastReceiver _usbReceiver = new BroadcastReceiver()
	{

		@Override
		public void onReceive(Context context, Intent intent)
		{
			Log.i(TAG, "f_onReceive, action: " + intent.getAction());
			String action = intent.getAction();
			if (ACTION_USB_PERMISSION.equals(action))
			{
				synchronized (this)
				{
					UsbAccessory accessory = UsbManager.getAccessory(intent);
					if (intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, false))
					{
						openAccessory(accessory);
					}
					else
					{
						Log.d(TAG, "permission denied for accessory " + accessory);
					}
					_permissionRequestPending = false;
				}
			}
			else if (UsbManager.ACTION_USB_ACCESSORY_DETACHED.equals(action))
			{
				UsbAccessory accessory = UsbManager.getAccessory(intent);
				if (accessory != null && accessory.equals(_accessory))
				{
					closeAccessory();
				}
			}
		}

	};

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		Log.i(TAG, "f_onCreate");
		super.onCreate(savedInstanceState);
		_usbManager = UsbManager.getInstance(this);
		_permissionIntent = PendingIntent.getBroadcast(this, 0, new Intent(ACTION_USB_PERMISSION), 0);
		IntentFilter filter = new IntentFilter(ACTION_USB_PERMISSION);
		filter.addAction(UsbManager.ACTION_USB_ACCESSORY_DETACHED);
		registerReceiver(_usbReceiver, filter);
		if (getLastNonConfigurationInstance() != null)
		{
			_accessory = (UsbAccessory) getLastNonConfigurationInstance();
			openAccessory(_accessory);
		}
	}

	@Override
	public void onResume()
	{
		Log.i(TAG, "f_onResume");
		super.onResume();

		if (_inputStream != null && _outputStream != null) { return; }

		UsbAccessory[] accessories = _usbManager.getAccessoryList();
		UsbAccessory accessory = (accessories == null ? null : accessories[0]);
		if (accessory != null)
		{
			if (_usbManager.hasPermission(accessory))
			{
				openAccessory(accessory);
			}
			else
			{
				synchronized (_usbReceiver)
				{
					if (!_permissionRequestPending)
					{
						_usbManager.requestPermission(accessory, _permissionIntent);
						_permissionRequestPending = true;
					}
				}
			}
		}
		else
		{
			Log.d(TAG, "_accessory is null");
		}
	}

	@Override
	public void onPause()
	{
		Log.i(TAG, "f_onPause");
		super.onPause();
		closeAccessory();
	}

	@Override
	public void onDestroy()
	{
		Log.i(TAG, "f_onPause");
		unregisterReceiver(_usbReceiver);
		super.onDestroy();
	}

	private void openAccessory(UsbAccessory accessory)
	{
		Log.i(TAG, "f_openAccessory");
		_fileDescriptor = _usbManager.openAccessory(accessory);
		if (_fileDescriptor != null)
		{
			_accessory = accessory;
			FileDescriptor fd = _fileDescriptor.getFileDescriptor();
			_inputStream = new FileInputStream(fd);
			_outputStream = new FileOutputStream(fd);
			Thread thread = new Thread(null, this, "DialPlate4Android");
			thread.start();
			Log.d(TAG, "accessory opened");
			onOpenAccesory();
		}
		else
		{
			Log.d(TAG, "accessory open fail");
		}
	}

	private void closeAccessory()
	{
		Log.i(TAG, "f_closeAccessory");
		try
		{
			if (_fileDescriptor != null)
			{
				_fileDescriptor.close();
			}
		}
		catch (IOException e)
		{
		}
		finally
		{
			_fileDescriptor = null;
			_accessory = null;
			Log.d(TAG, "accessory closed");
			onCloseAccesory();
		}
	}

	@Override
	public void run()
	{
		int ret = 0;
		byte[] buffer = new byte[16384];
		int i;

		while (ret >= 0)
		{
			try
			{
				ret = _inputStream.read(buffer);
			}
			catch (IOException e)
			{
				break;
			}

			i = 0;
			while (i < ret)
			{
				Log.d(TAG, "msg: " + buffer[i] + ", " + buffer[i + 1] + ", " + buffer[i + 2]);
				Message m = Message.obtain(_handler);
				m.obj = new ArduinoAccessoryMessage(buffer[i], buffer[i + 1], buffer[i + 2]);
				_handler.sendMessage(m);
				i += 3;
			}
		}
	}

	private Handler _handler = new Handler()
	{

		@Override
		public void handleMessage(Message msg)
		{
			onMessage((ArduinoAccessoryMessage) msg.obj);
		}
	};

	protected int composeInt(byte hi, byte lo)
	{
		int val = (int) hi & 0xff;
		val *= 256;
		val += (int) lo & 0xff;
		return val;
	}

	public void sendCommand(byte command, byte target, int value)
	{
		byte[] buffer = new byte[3];
		if (value > 255) value = 255;

		buffer[0] = command;
		buffer[1] = target;
		buffer[2] = (byte) value;
		if (_outputStream != null && buffer[1] != -1)
		{
			try
			{
				_outputStream.write(buffer);
			}
			catch (IOException e)
			{
				Log.e(TAG, "write failed", e);
			}
		}
	}

	public boolean getAccessoryOpened()
	{
		return _accessory != null;
	}

	abstract protected void onOpenAccesory();

	abstract protected void onCloseAccesory();

	abstract protected void onMessage(ArduinoAccessoryMessage msg);

}
