package se.springworks.android.utils.analytics;

import android.content.Context;

public interface ICrashHandler {

	void init(Context context);
	
	void sendException(Exception e);
	
	void leaveBreadcrumb(String breadcrumb);
	
}
