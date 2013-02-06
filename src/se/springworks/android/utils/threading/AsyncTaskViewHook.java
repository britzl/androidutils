package se.springworks.android.utils.threading;

import se.springworks.android.utils.R;
import android.os.AsyncTask;
import android.view.View;

public class AsyncTaskViewHook {

	private static final int TASKKEY = 10001;
	
	private AsyncTask<?, ?, ?> task;
	
	private View view;
	
	public AsyncTaskViewHook() {
	}
	
	public void assign(AsyncTask<?, ?, ?> task) {
		this.task = task;
	}
	
	public void assign(View view) {
		
		AsyncTaskViewHook hook = (AsyncTaskViewHook)view.getTag(TASKKEY);
		if(hook != null && hook.task != null) {
			hook.task.cancel(true);
		}
		this.view = view;
		view.setTag(R.id.viewhookid, this);
	}

	
	public boolean isValid(View v) {
		return view == v;
	}
	
}
