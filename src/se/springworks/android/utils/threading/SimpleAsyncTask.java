package se.springworks.android.utils.threading;

import android.os.AsyncTask;

public abstract class SimpleAsyncTask<Param, Result> extends AsyncTask<Param, Void, Result> {

	@Override
	protected Result doInBackground(Param... params) {
		return performTask(params != null ? params[0] : null);
	}
	
	@Override
	protected final void onPostExecute(Result result) {
		handleResult(result);
	}
	
	public abstract Result performTask(Param param);
	
	public abstract void handleResult(Result result);
}
