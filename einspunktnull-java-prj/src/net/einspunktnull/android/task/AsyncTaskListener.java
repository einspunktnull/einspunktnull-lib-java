package net.einspunktnull.android.task;


public interface AsyncTaskListener
{

	public void onTaskReady(ListenedAsyncTask<?, ?, ?> task, Object result);
	public void onTaskAborted(ListenedAsyncTask<?, ?, ?> task);
}
