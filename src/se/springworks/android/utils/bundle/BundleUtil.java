package se.springworks.android.utils.bundle;

import android.content.Intent;
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
	
	public static void clearExtras(Intent i) {
		i.putExtras(new Bundle());
	}
	
	public static Bundle copyExtras(Intent i) {
		Bundle copy = new Bundle();
		Bundle original = i.getExtras();
		if(original != null) {
			copy.putAll(original);
		}
		return copy;
		
	}
}
