package se.springworks.android.utils.threading;

import se.springworks.android.utils.R;
import android.os.AsyncTask;

/**
 * Wrapper for AsyncVoidTask<Void,Void,Void>
 * @author bjornritzl
 *
 */
public abstract class AsyncVoidTask extends AsyncTask<Void, Void, Void> {

	@Override
	protected final Void doInBackground(Void... params) {
		performTask();
		return null;
	}
	
	@Override
	protected final void onPostExecute(Void result) {
		onPostExecute();
	}
	
	public void execute() {
		execute((Void)null);
	}
	
	abstract protected void onPostExecute();
	
	abstract protected void performTask();
}
