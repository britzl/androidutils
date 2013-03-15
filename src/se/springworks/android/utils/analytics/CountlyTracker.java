package se.springworks.android.utils.analytics;

import se.springworks.android.utils.resource.ParameterLoader;
import ly.count.android.api.Countly;
import android.app.Activity;
import android.content.Context;

public class CountlyTracker implements IAnalyticsTracker {
	
	private static final String KEY_SERVER = "countly_server";
	private static final String KEY_APPKEY = "countly_appkey";
	
	private boolean initialized = false;

	@Override
	public void trackActivityStart(Activity activity) {
		Countly.sharedInstance().onStart();
	}

	@Override
	public void trackActivityStop(Activity activity) {
		Countly.sharedInstance().onStop();
	}

	@Override
	public void trackEvent(String category, String action, String label, int value) {
		Countly.sharedInstance().recordEvent(category + "_" + action + "_" + label, 1);
	}

	@Override
	public void init(Context context) {
		if(initialized) {
			return;
		}
		ParameterLoader loader = new ParameterLoader(context);
		String url = "http://cloud.count.ly";
		if(loader.hasString(KEY_SERVER)) {
			url = loader.getString(KEY_SERVER);
		}
		String appKey = loader.getString(KEY_APPKEY);
        Countly.sharedInstance().init(context, url, appKey);
		initialized = true;
	}

	@Override
	public void trackScreen(String name) {
		Countly.sharedInstance().recordEvent(name, 1);
	}

}
