package se.springworks.android.utils.analytics;

import se.springworks.android.utils.resource.ParameterLoader;

import com.flurry.android.FlurryAgent;

import android.app.Activity;
import android.content.Context;

public class FlurryTracker implements IAnalyticsTracker {
	
	private static final String KEY_CAPTUREUNCAIGHTEXCEPTIONS = "flurry_captureuncaughtexceptions";
	private static final String KEY_REPORTLOCATION = "flurry_reportlocation";
	private static final String KEY_APPID = "flurry_app_id";
	private static final String KEY_CONTINUESESSIONMILLIS = "flurry_continuesessionmillis";
	private static final String KEY_LOGENABLED = "flurry_logenabled";
	private static final String KEY_USEHTTPS = "flurry_usehttps";
	
	private static FlurryTracker instance;
	
	private String appId;
	
	public FlurryTracker() {
		
	}
	
	public static FlurryTracker getTracker() {
		if(instance == null) {
			instance = new FlurryTracker();
		}
		return instance;
	}
	
	@Override
	public void init(final Context context) {
		ParameterLoader loader = new ParameterLoader(context);
		appId = loader.getString(KEY_APPID);
		if(loader.hasBoolean(KEY_CAPTUREUNCAIGHTEXCEPTIONS)) {
			FlurryAgent.setCaptureUncaughtExceptions(loader.getBoolean(KEY_CAPTUREUNCAIGHTEXCEPTIONS));
		}
		if(loader.hasBoolean(KEY_REPORTLOCATION)) {
			FlurryAgent.setReportLocation(loader.getBoolean(KEY_REPORTLOCATION));
		}
		if(loader.hasInt(KEY_CONTINUESESSIONMILLIS)) {
			FlurryAgent.setContinueSessionMillis(loader.getInt(KEY_CONTINUESESSIONMILLIS, 10 * 1000));
		}
		if(loader.hasBoolean(KEY_LOGENABLED)) {
			FlurryAgent.setLogEnabled(loader.getBoolean(KEY_LOGENABLED));
		}
		if(loader.hasBoolean(KEY_USEHTTPS)) {
			FlurryAgent.setUseHttps(loader.getBoolean(KEY_USEHTTPS));
		}
	}
	
	@Override
	public void trackActivityStart(final Activity activity) {
		FlurryAgent.onStartSession(activity, appId);
		FlurryAgent.onPageView();
	}
	
	@Override
	public void trackActivityStop(final Activity activity) {
		FlurryAgent.onEndSession(activity);
	}

	@Override
	public void trackEvent(String category, String action, String label, int value) {
		FlurryAgent.logEvent(category + "_" + action + "_" + label);
	}
}
