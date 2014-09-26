package se.springworks.android.utils.application;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;

public class ApplicationUtils {

	private static PackageInfo getPackageInfo(Context context) {
		PackageInfo info = null;
		try {
			info = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
		}
		catch (NameNotFoundException e) {
		}
		return info;
	}
	
	public static String getVersionName(Context context) {
		PackageInfo info = getPackageInfo(context);
		if(info == null) {
			return null;
		}
		return info.versionName;
	}
	
	public static int getVersionCode(Context context) {
		PackageInfo info = getPackageInfo(context);
		if(info == null) {
			return -1;
		}
		return info.versionCode;
	}
	
	
	public static String getApplicationName(Context context) {
		PackageInfo info = getPackageInfo(context);
		if(info == null || info.applicationInfo == null) {
			return null;
		}
		CharSequence label = context.getPackageManager().getApplicationLabel(info.applicationInfo);
		if(label == null) {
			return null;
		}
		return label.toString();
//		return info.applicationInfo.name;
	}
	
	/**
	 * Check if android:debuggable is true 
	 * @param context
	 * @return
	 */
	public static boolean isDebuggable(Context context) {
		ApplicationInfo ai = context.getApplicationInfo();
		if(ai == null) {
			return false;
		}
		return (ai.flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;
	}

}
