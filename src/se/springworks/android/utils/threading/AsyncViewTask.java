package se.springworks.android.utils.threading;

import se.springworks.android.utils.R;
import android.os.AsyncTask;
import android.view.View;

public abstract class AsyncViewTask<S extends View, T, U, V> extends AsyncTask<T, U, V> {

	protected S view;
	
	public AsyncViewTask() {
	}

	/**
	 * Starts execution of the task and assigns it to the specified view
	 * If the view already has a task assigned to it that task is cancelled
	 * @param view The view to assign this task to
	 * @param params Task parameters
	 * @return This task
	 */
	public final AsyncTask<T, U, V> execute(S view, T... params) {
		this.view = view;		

		// check if there's another task assigned to view
		// if that is the case we need to cancel the previous task
		@SuppressWarnings("rawtypes")
		AsyncTask task = (AsyncTask)view.getTag(R.id.ASYNCVIEWTASKID);
		if(task != null && task != this) {
			task.cancel(true);			
		}
		
		// set this task as the tag on the view and start executing the task
		this.view.setTag(R.id.ASYNCVIEWTASKID, this);
		return super.execute(params);
	}
	
	/**
	 * Handles the result from the execution. This method will only handle the result
	 * if the view is still assigned to this task
	 */
	@Override
	protected final void onPostExecute(V result) {
		@SuppressWarnings("rawtypes")
		AsyncTask task = (AsyncTask)view.getTag(R.id.ASYNCVIEWTASKID);
		if(task == this) {
			handleResult(result, view);
		}
	}
	
	/**
	 * Handles the result. This method will only be called if the view is still valid
	 * @param result
	 */
	public abstract void handleResult(V result, S view);
}
