package se.springworks.android.utils.analytics;

import java.util.Collection;
import java.util.Vector;

import android.app.Activity;
import android.content.Context;

public class MultiTracker implements IAnalyticsTracker {

	private Collection<IAnalyticsTracker> trackers = new Vector<IAnalyticsTracker>();
	
	public void addTracker(IAnalyticsTracker tracker) {
		trackers.add(tracker);
	}
	
	@Override
	public void init(Context context) {
		for(IAnalyticsTracker tracker : trackers) {
			tracker.init(context);
		}
	}

	@Override
	public void trackActivityStart(Activity activity) {
		for(IAnalyticsTracker tracker : trackers) {
			tracker.trackActivityStart(activity);
		}
	}

	@Override
	public void trackActivityStop(Activity activity) {
		for(IAnalyticsTracker tracker : trackers) {
			tracker.trackActivityStop(activity);
		}
	}

	@Override
	public void trackEvent(String category, String action, String label, int value) {
		for(IAnalyticsTracker tracker : trackers) {
			tracker.trackEvent(category, action, label, value);
		}
	}

	
}
