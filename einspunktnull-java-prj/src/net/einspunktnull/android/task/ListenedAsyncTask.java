package net.einspunktnull.android.task;

import android.os.AsyncTask;

public abstract class ListenedAsyncTask<Params, Progress, Result> extends AsyncTask<Params, Progress, Result>
{

	protected AsyncTaskListener listener;

	public ListenedAsyncTask(AsyncTaskListener listener)
	{
		this.listener = listener;
	}
	
	@Override
	protected void onPostExecute(Result result)
	{
		listener.onTaskReady(this, result);
	}
	
	@Override
	protected void onCancelled()
	{
		listener.onTaskAborted(this);
	}


}
