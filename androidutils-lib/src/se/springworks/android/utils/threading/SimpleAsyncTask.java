package se.springworks.android.utils.threading;

import android.os.AsyncTask;

public abstract class SimpleAsyncTask<Param, Result> extends AsyncTask<Param, Void, Result> {

	@Override
	protected Result doInBackground(Param... params) {
		final boolean validParams = ((params != null) && (params.length > 0));
		return performTask(validParams ? params[0] : null);
	}
	
	@Override
	protected final void onPostExecute(Result result) {
		handleResult(result);
	}
	
	public abstract Result performTask(Param param);
	
	public abstract void handleResult(Result result);
}
