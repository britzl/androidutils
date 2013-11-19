package se.springworks.android.utils.analytics;

import ly.count.android.api.Countly;
import se.springworks.android.utils.resource.ParameterLoader;
import android.app.Activity;
import android.content.Context;

/**
 * Wrapper for Countly analytics tracking
 * You need to provide the wrapper with your Countly app key and you can optinally also define an alternative
 * Countly server to send analytics data to. This wrapper can be configured in two ways:
 * 
 * 1. Define app key and server configuration as two string resources named according to the {@link CountlyTracker#KEY_SERVER} and {@link CountlyTracker#KEY_APPKEY}
 * 2. Pass app key and optionally also server to the constructor
 * @author bjornritzl
 *
 */
public class CountlyTracker implements IAnalyticsTracker {
	
	private static final String KEY_SERVER = "countly_server";
	private static final String KEY_APPKEY = "countly_appkey";
	
	private String appKey;
	private String server = "http://cloud.count.ly";
	
	private boolean initialized = false;
	
	public CountlyTracker() {
		// empty constructor for dependency injection
	}
	
	public CountlyTracker(String appKey) {
		this.appKey = appKey;
	}
	
	public CountlyTracker(String appKey, String server) {
		this.appKey = appKey;
		this.server = server;
	}

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
		
		if(loader.hasString(KEY_SERVER)) {
			server = loader.getString(KEY_SERVER);
		}
		
		if(appKey == null) {
			appKey = loader.getString(KEY_APPKEY);
		}
		
        Countly.sharedInstance().init(context, server, appKey);
		initialized = true;
	}

	@Override
	public void trackScreen(String name) {
		Countly.sharedInstance().recordEvent(name, 1);
	}

}
