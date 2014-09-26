package se.springworks.android.utils.activity;

import se.springworks.android.utils.application.BaseApplication;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;

public abstract class BaseActivity extends ActionBarActivity {
	
	public interface OnActivityResultListener {
		public void onActivityResult(int requestCode, int resultCode, Intent data); 
	}
	
	
	private OnActivityResultListener activityResultListener = null;
	
	private static int defaultStartActivityEnterAnimationId = 0;
	private static int defaultStartActivityExitAnimationId = 0;
	private static int defaultFinishActivityEnterAnimationId = 0;
	private static int defaultFinishActivityExitAnimationId = 0;

	protected int startActivityEnterAnimationId = defaultStartActivityEnterAnimationId;
	protected int startActivityExitAnimationId = defaultStartActivityExitAnimationId;
	protected int finishActivityEnterAnimationId = defaultFinishActivityEnterAnimationId;
	protected int finishActivityExitAnimationId = defaultFinishActivityExitAnimationId;

	private boolean titleBarHidden = false;
	
	protected void hideTitleBar() {
		supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
	}
	
	public void disableTouchEvents() {
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);		
	}
	
	public void enableTouchEvents() {
		getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
	}
	
	@Override
	public boolean supportRequestWindowFeature(int featureId) {
		boolean success = super.supportRequestWindowFeature(featureId);
		titleBarHidden = success && featureId == Window.FEATURE_NO_TITLE;
		return success;
	}
	
	@Override
	public void setContentView(int layoutResId) {
		super.setContentView(layoutResId);
		try {
			ActionBar ab = getSupportActionBar();
			if(ab != null && titleBarHidden) {
				ab.hide();
			}
		}
		catch(NullPointerException npe) {
			// do nothing
		}
	}
	
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
	}
	
	
	@Override
	public final void onResumeFragments() {
		super.onResumeFragments();
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

	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// there's currently a bug in ActionBarCompat causing an activity
		// with FEATURE_NO_TITLE to crash when the menu button is
		// pressed and onCreateOptionsMenu() is called
		// refer to: http://stackoverflow.com/questions/19275447/oncreateoptionsmenu-causing-error-in-an-activity-with-no-actionbar
	    if(keyCode == KeyEvent.KEYCODE_MENU) {
	        return true;
	    }
	    return super.onKeyDown(keyCode, event);
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

	public final void switchActivity(Class<? extends Activity> c, int flags) {
		Intent i = new Intent(this, c);
		i.setFlags(flags);
		startActivity(i);
	}

	public final void switchActivity(Class<? extends Activity> c, Bundle extras) {
		Intent i = new Intent(this, c);
		if(extras != null) {
			i.putExtras(extras);
		}
		startActivity(i);
	}

	public final void switchActivity(Class<? extends Activity> c, Bundle extras, int flags) {
		Intent i = new Intent(this, c);
		i.setFlags(flags);
		if(extras != null) {
			i.putExtras(extras);
		}
		startActivity(i);
	}
	
	@Override
	public void startActivity(Intent intent) {
		startActivity(intent, startActivityEnterAnimationId, startActivityExitAnimationId);
	}
	
	public void startActivity(Intent intent, int enterAnimationId, int exitAnimationId) {
		super.startActivity(intent);
		overridePendingTransition(enterAnimationId, exitAnimationId);
	}
	
	
	public void startActivityForResult(Intent intent, int requestCode, OnActivityResultListener listener) {
		this.activityResultListener = listener;
		startActivityForResult(intent, requestCode);
	}
	
	@Override
	public void startActivityForResult(Intent intent, int requestCode) {
		startActivityForResult(intent, requestCode, startActivityEnterAnimationId, startActivityExitAnimationId);
	}
	
	public void startActivityForResult(Intent intent, int requestCode, int enterAnimationId, int exitAnimationId) {
		if (getParent() != null) {
			getParent().startActivityForResult(intent, requestCode);
			overridePendingTransition(enterAnimationId, exitAnimationId);
		}
		else {
			super.startActivityForResult(intent, requestCode);
			overridePendingTransition(enterAnimationId, exitAnimationId);
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
		
//	@Override
//	public void startActivity(Intent intent, Bundle options) {
//		super.startActivity(intent, options);
//		overridePendingTransition(startActivityEnterAnimationId, startActivityExitAnimationId);
//	}
//	
//	public void startActivity(Intent intent, Bundle options, int enterAnimationId, int exitAnimationId) {
//		super.startActivity(intent, options);
//		overridePendingTransition(enterAnimationId, exitAnimationId);
//	}
	
	@Override
	public void finish() {
		finish(finishActivityEnterAnimationId, finishActivityExitAnimationId);
	}
	
	public void finish(int enterAnimationId, int exitAnimationId) {
		super.finish();
		overridePendingTransition(enterAnimationId, exitAnimationId);
	}
	
	@Override
	public void finishActivity(int requestCode) {
		finishActivity(requestCode, finishActivityEnterAnimationId, finishActivityExitAnimationId);
	}

	public void finishActivity(int requestCode, int enterAnimationId, int exitAnimationId) {
		super.finishActivity(requestCode);
		overridePendingTransition(enterAnimationId, exitAnimationId);
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