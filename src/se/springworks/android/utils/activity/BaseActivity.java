package se.springworks.android.utils.activity;

import java.io.IOException;

import org.slf4j.Logger;

import com.google.android.apps.analytics.easytracking.EasyTracker;

import roboguice.activity.RoboFragmentActivity;
import se.springworks.android.utils.guice.InjectLogger;
import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

public abstract class BaseActivity extends RoboFragmentActivity {

	@InjectLogger Logger logger;

	private Resources resources;


	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.logger.debug("onCreate() " + this);

		try {
			EasyTracker.getTracker().setContext(this);
			createActivity(savedInstanceState);
		}
		catch(Exception e) {
			handleError(e);
		}
	}

	@Override
	public final void onRestart() {
		super.onRestart();
		this.logger.debug("onRestart() " + this);

		try {
			restartActivity();
		}
		catch(Exception e) {
			handleError(e);
		}
	}

	@Override
	public final void onStart() {
		super.onStart();
		this.logger.debug("onStart() " + this);

		try {
			EasyTracker.getTracker().trackActivityStart(this);
			startActivity();
		}
		catch(Exception e) {
			handleError(e);
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		this.logger.debug("onResume() " + this);

		try {
			resumeActivity();
		}
		catch(Exception e) {
			handleError(e);
		}
	}

	@Override
	public void onPause() {
		super.onPause();
		this.logger.debug("onPause() " + this);

		try {
			pauseActivity();
		}
		catch(Exception e) {
			handleError(e);
		}
	}

	@Override
	public final void onStop() {
		super.onStop();
		this.logger.debug("onStop() " + this);

		try {
			EasyTracker.getTracker().trackActivityStop(this);
			stopActivity();
		}
		catch(Exception e) {
			handleError(e);
		}
	}

	@Override
	public final void onDestroy() {
		super.onDestroy();
		this.logger.debug("onDestroy() " + this);

		try {
			destroyActivity();
		}
		catch(Exception e) {
			handleError(e);
		}
	}



	@Override
	public void startActivityForResult(android.content.Intent intent, int requestCode) {
		if(getParent() != null) {
			getParent().startActivityForResult(intent, requestCode);
		}
		else {
			super.startActivityForResult(intent, requestCode);
		}
	}

	public void handleActivityResult(int requestCode, int resultCode, Intent data) {
		// override
	}

	protected final String getResourceString(int id) {
		if(this.resources == null) {
			this.resources = getResources();
		}
		return this.resources.getString(id);
	}

	protected final Drawable getResourceDrawable(int id) {
		if(this.resources == null) {
			this.resources = getResources();
		}
		return this.resources.getDrawable(id);
	}

	private final void handleError(Exception e) {
		this.logger.debug(e.getMessage(), e);
	}

	protected final Toast showToast(String text) {
		Toast toast = Toast.makeText(this, text, Toast.LENGTH_LONG);
		toast.show();
		return toast;
	}

	protected final void switchActivity(Class<? extends Activity> c) {
		Intent i = new Intent(this, c);
		startActivity(i);
		overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
	}

	protected final void switchActivity(Class<? extends Activity> c, Bundle extras) {
		Intent i = new Intent(this, c);
		i.putExtras(extras);
		startActivity(i);
		overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
	}

	protected TextView createTextView(String text, int styleId) {
		TextView tv = new TextView(this);
		tv.setTextAppearance(this, styleId);
		tv.setText(text);
		return tv;
	}

	protected Bitmap getBitmapFromAssets(String name) {
		AssetManager assetManager = getAssets();
		Bitmap bitmap = null;
		try {
			bitmap = BitmapFactory.decodeStream(assetManager.open(name));
		} catch (IOException e) {
			logger.warn("Unable to load bitmap from assets" , e);
		}
		return bitmap;
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

	@Override
	public void onBackPressed() {
		this.logger.debug("onBackPressed()");
		super.onBackPressed();
	}
}