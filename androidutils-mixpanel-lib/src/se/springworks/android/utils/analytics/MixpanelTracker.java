package se.springworks.android.utils.analytics;

import se.springworks.android.utils.resource.ParameterLoader;
import android.app.Activity;
import android.content.Context;

import com.mixpanel.android.mpmetrics.MixpanelAPI;

public class MixpanelTracker implements IAnalyticsTracker {

	private static final String KEY_APITOKEN = "mixpanel_apitoken";
	
	private MixpanelAPI mixpanel;
	
	@Override
	public void init(Context context) {
		if(mixpanel != null) {
			return;
		}
		ParameterLoader loader = new ParameterLoader(context);
		String apiToken = loader.getString(KEY_APITOKEN);
		mixpanel = MixpanelAPI.getInstance(context, apiToken);
	}

	@Override
	public void trackActivityStart(Activity activity) {
		// do nothing
	}

	@Override
	public void trackActivityStop(Activity activity) {
		// do nothing
	}

	@Override
	public void trackEvent(String category, String action, String label, int value) {
		mixpanel.track(category + "_" + label + "_" + action, null);
        mixpanel.flush();
	}

	@Override
	public void trackScreen(String name) {
		mixpanel.track(name, null);
	}

}
