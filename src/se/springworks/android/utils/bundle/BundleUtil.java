package se.springworks.android.utils.bundle;

import android.os.Bundle;

public class BundleUtil {

	public static boolean containsNonEmpty(Bundle b, String key) {
		return b.containsKey(key) && b.get(key) != null;
	}
	
	public static int getAsInt(Bundle b, String key) {
		if(!b.containsKey(key)) {
			return 0;
		}
		String s = b.getString(key);
		if(s != null) {
			
			try {
				return Integer.parseInt(s);
			}
			catch(NumberFormatException e) {
				
			}
		}
		return b.getInt(key);
	}
}
