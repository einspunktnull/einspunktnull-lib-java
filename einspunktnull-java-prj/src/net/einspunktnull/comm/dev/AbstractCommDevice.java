package net.einspunktnull.comm.dev;

import java.util.Timer;
import java.util.TimerTask;

public abstract class AbstractCommDevice
{

	protected String name;
	protected ICommDeviceListener listener;
	protected boolean running;
	private Timer receiveTimer;

	public AbstractCommDevice(String name, ICommDeviceListener listener)
	{
		this.name = name;
		this.listener = listener;
	}

	private class RefreshTimerTask extends TimerTask
	{

		@Override
		public void run()
		{
			onReceivingFinished();
		}

	};

	private void cancelTimer()
	{
		if (receiveTimer != null)
		{
			receiveTimer.cancel();
			receiveTimer = null;
		}

	}

	private void refreshTimer()
	{
		cancelTimer();
		receiveTimer = new Timer();
		receiveTimer.schedule(new RefreshTimerTask(), 1000);
	}

	protected void onByteReceived(byte bite)
	{
		listener.onByteReceived(this, bite);
		onReceivingStarted();
	}

	@Override
	public String toString()
	{
		return "AbstractCommDevice: " + name;
	}

	public boolean isRunning()
	{
		return running;
	}

	protected void onReceivingStarted()
	{
		listener.onReceivingStarted(this);
		refreshTimer();
	}

	protected void onReceivingFinished()
	{
		listener.onReceivingFinished(this);
		cancelTimer();
	}

	public abstract void start() throws CommDeviceException;

	public abstract void stop() throws CommDeviceException;

	public abstract void write(String msg) throws CommDeviceException;

	public abstract void write(byte bite) throws CommDeviceException;

	public abstract void write(byte[] bytes) throws CommDeviceException;
}
