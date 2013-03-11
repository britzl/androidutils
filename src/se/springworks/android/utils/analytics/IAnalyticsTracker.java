package se.springworks.android.utils.analytics;

import android.app.Activity;
import android.content.Context;

public interface IAnalyticsTracker {
	
	void init(final Context context);
	
	/**
	 * Call this in onStart() on your Activity and pass the activity as a parameter
	 * @param activity
	 */
	void trackActivityStart(final Activity activity);
	
	/**
	 * Call this in onStop() on your Activity and pass the activity as a parameter. It is important
	 * that each call to {@link #trackActivityStart(Activity)} has a corresponding call to this
	 * method
	 * @param activity
	 */
	void trackActivityStop(final Activity activity);
	
	/**
	 * Tracks an event within your application. This can be anything from a button press to the user
	 * dragging a slider.
	 * @param category
	 * @param action
	 * @param label
	 * @param value
	 */
	void trackEvent(final String category, final String action, final String label, final int value);
}
