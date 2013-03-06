package se.springworks.android.utils.analytics;

import android.app.Activity;
import android.content.Context;

public interface IAnalyticsTracker {
	
	void init(final Context context);
	
	void trackActivityStart(final Activity activity);
	
	void trackActivityStop(final Activity activity);
	
	void trackEvent(final String category, final String action, final String label, final int value);
}
