package se.springworks.android.utils.analytics;

import com.crittercism.app.Crittercism;

import se.springworks.android.utils.resource.ParameterLoader;
import android.content.Context;

/**
 * 
 * IMPORTANT: If you use ProGuard for obfuscation and optimization you need to add
 * the following lines to proguard-project.txt:

-keep public class com.crittercism.**
-keepclassmembers public class com.crittercism.*
{
    *;
}

 * @author bjornritzl
 *
 */
public class CrittercismCrashHandler implements ICrashHandler {

	private static final String KEY_CRITTERCISM_APP_ID = "crittercism_appid";
	
	@Override
	public void init(Context context) {
		ParameterLoader loader = new ParameterLoader(context);
		String appid = loader.getString(KEY_CRITTERCISM_APP_ID);
		Crittercism.init(context, appid);
	}

	@Override
	public void sendException(Exception e) {
		Crittercism.logHandledException(e);
	}

	@Override
	public void leaveBreadcrumb(String breadcrumb) {
		Crittercism.leaveBreadcrumb(breadcrumb);
	}

}
