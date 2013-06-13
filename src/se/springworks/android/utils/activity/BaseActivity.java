package se.springworks.android.utils.activity;

import se.springworks.android.utils.application.BaseApplication;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.actionbarsherlock.app.SherlockFragmentActivity;

public abstract class BaseActivity extends SherlockFragmentActivity {
	
	public interface OnActivityResultListener {
		public void onActivityResult(int requestCode, int resultCode, Intent data); 
	}
	
	
	private OnActivityResultListener activityResultListener = null;
	
	private static int defaultStartActivityEnterAnimationId = 0;
	private static int defaultStartActivityExitAnimationId = 0;
	private static int defaultFinishActivityEnterAnimationId = 0;
	private static int defaultFinishActivityExitAnimationId = 0;

	/** Called when the activity is first created. */
	@Override
	public final void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		BaseApplication.getInstance().onActivityCreated(this);

		try {
			createActivity(savedInstanceState);
		}
		catch (Exception e) {
			handleError(e);
		}
	}

	@Override
	public final void onRestart() {
		super.onRestart();
		try {
			restartActivity();
		}
		catch (Exception e) {
			handleError(e);
		}
	}

	@Override
	public final void onStart() {
		super.onStart();
		try {
			startActivity();
		}
		catch (Exception e) {
			handleError(e);
		}
	}

	@Override
	public final void onResume() {
		super.onResume();
		BaseApplication.getInstance().onActivityResumed(this);

		try {
			resumeActivity();
		}
		catch (Exception e) {
			handleError(e);
		}
	}

	@Override
	public final void onPause() {
		super.onPause();
		BaseApplication.getInstance().onActivityPaused(this);

		try {
			pauseActivity();
		}
		catch (Exception e) {
			handleError(e);
		}
	}

	@Override
	public void onStop() {
		super.onStop();

		try {
			stopActivity();
		}
		catch (Exception e) {
			handleError(e);
		}
	}

	@Override
	public final void onDestroy() {
		super.onDestroy();
		BaseApplication.getInstance().onActivityDestroyed(this);

		try {
			destroyActivity();
		}
		catch (Exception e) {
			handleError(e);
		}
	}

	protected void handleError(Exception e) {
		e.printStackTrace();
	}
	
	/**
	 * Set the default start activity transitions
	 * @param enterId
	 * @param exitId
	 */
	public static void setDefaultStartActivityTransition(int enterId, int exitId) {
		defaultStartActivityEnterAnimationId = enterId;
		defaultStartActivityExitAnimationId = exitId;
	}
	
	public static void setDefaultFinishActivityTransition(int enterId, int exitId) {
		defaultFinishActivityEnterAnimationId = enterId;
		defaultFinishActivityExitAnimationId = exitId;
	}

	public final void switchActivity(Class<? extends Activity> c) {
		Intent i = new Intent(this, c);
		startActivity(i);
	}

	public final void switchActivity(Class<? extends Activity> c, Bundle extras) {
		Intent i = new Intent(this, c);
		if(extras != null) {
			i.putExtras(extras);
		}
		startActivity(i);
	}
	
	@Override
	public void startActivity(Intent intent) {
		super.startActivity(intent);
		overridePendingTransition(defaultStartActivityEnterAnimationId, defaultStartActivityExitAnimationId);
	}
	
	
	public void startActivityForResult(Intent intent, int requestCode, OnActivityResultListener listener) {
		this.activityResultListener = listener;
		startActivityForResult(intent, requestCode);
	}
	
	@Override
	public void startActivityForResult(Intent intent, int requestCode) {
		if (getParent() != null) {
			getParent().startActivityForResult(intent, requestCode);
			overridePendingTransition(defaultStartActivityEnterAnimationId, defaultStartActivityExitAnimationId);
		}
		else {
			super.startActivityForResult(intent, requestCode);
			overridePendingTransition(defaultStartActivityEnterAnimationId, defaultStartActivityExitAnimationId);
		}
	}
	
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	if(activityResultListener != null) {
    		activityResultListener.onActivityResult(requestCode, resultCode, data);
    		activityResultListener = null;
    	}
    	super.onActivityResult(requestCode, resultCode, data);
    }
		
	@Override
	public void startActivity(Intent intent, Bundle options) {
		super.startActivity(intent, options);
		overridePendingTransition(defaultStartActivityEnterAnimationId, defaultStartActivityExitAnimationId);
	}
	
	@Override
	public void finish() {
		super.finish();
		overridePendingTransition(defaultFinishActivityEnterAnimationId, defaultFinishActivityExitAnimationId);
	}
	
	@Override
	public void finishActivity(int requestCode) {
		super.finishActivity(requestCode);
		overridePendingTransition(defaultFinishActivityEnterAnimationId, defaultFinishActivityExitAnimationId);
	}

	abstract protected void createActivity(Bundle savedInstanceState);

	protected void destroyActivity() {
		// override if needed
	}

	protected void stopActivity() {
		// override if needed
	}

	protected void restartActivity() {
		// override if needed
	}

	protected void startActivity() {
		// override if needed
	}

	protected void resumeActivity() {
		// override if needed
	}

	protected void pauseActivity() {
		// override if needed
	}
}