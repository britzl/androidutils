package se.springworks.android.utils.analytics;

import se.springworks.android.utils.resource.ParameterLoader;
import android.content.Context;

import com.bugsense.trace.BugSenseHandler;

/**
 * IMPORTANT: If you use ProGuard you need to add the following line to your
 * proguard-project-txt file:
 * 
 * -keep public class com.bugsense.*
 * 
 * @author bjornritzl
 *
 */
public class BugSenseCrashHandler implements ICrashHandler {

	private static final String KEY_APIKEY = "bugsense_apikey";
	
	@Override
	public void init(Context context) {
		ParameterLoader loader = new ParameterLoader(context);
		String apiKey = loader.getString(KEY_APIKEY);
		BugSenseHandler.initAndStartSession(context, apiKey);
	}
	
	@Override
	public void sendException(Exception e) {
		BugSenseHandler.sendException(e);
	}

	@Override
	public void leaveBreadcrumb(String breadcrumb) {
		BugSenseHandler.leaveBreadcrumb(breadcrumb);
	}
}
