package se.springworks.android.utils.threading;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import se.springworks.android.utils.R;
import android.os.AsyncTask;
import android.view.View;

public abstract class AsyncViewTask<SomeView extends View, Params, Progress, Result> extends AsyncTask<Params, Progress, Result> {

	private static final Logger logger = LoggerFactory.getLogger(AsyncViewTask.class);
	
	protected SomeView view;
	
	public AsyncViewTask() {
	}

	/**
	 * Starts execution of the task and assigns it to the specified view
	 * If the view already has a task assigned to it that task is cancelled
	 * @param view The view to assign this task to
	 * @param params Task parameters
	 * @return This task
	 */
	public final AsyncTask<Params, Progress, Result> execute(SomeView view, Params... params) {
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
	protected final void onPostExecute(Result result) {
		@SuppressWarnings("rawtypes")
		AsyncTask task = (AsyncTask)view.getTag(R.id.ASYNCVIEWTASKID);
		if(task == this) {
			try {
				handleResult(result, view);
			}
			catch(Exception e) {
				logger.warn("onPostExecute()", e);
			}
		}
	}
	
	@Override
	protected final Result doInBackground(Params... params) {
		try {
			return performTask(params);
		}
		catch(Exception e) {
			logger.warn("doInBackground()", e);
		}
		return null;
	}
	
	/**
	 * Perform the task
	 * @param params
	 * @return
	 */
	protected abstract Result performTask(Params... params);
	
	/**
	 * Handles the result. This method will only be called if the view is still valid
	 * @param result
	 */
	protected abstract void handleResult(Result result, SomeView view);
}
