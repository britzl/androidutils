package se.springworks.android.utils.logging;

import java.util.Hashtable;

import android.util.Log;

public class AndroidLogTarget implements ILogTarget {
	private static final int TAG_MAX_LENGTH = 23;

	private Hashtable<String, String> tagMap = new Hashtable<String, String>();

	private boolean convertDebugToInfo = false;
	
	public void setConvertDebugToInfo(boolean convert) {
		convertDebugToInfo = convert;
	}
	
	private final String enforceValidTagLength(final String originalName) {		
		String trimmedName = originalName;
		final int orginalLength = originalName.length();
		if (originalName != null && orginalLength > TAG_MAX_LENGTH) {

			if(tagMap.containsKey(originalName)) {
				return tagMap.get(originalName);
			}
			
			
			trimmedName = originalName.substring(orginalLength - TAG_MAX_LENGTH);
			tagMap.put(originalName, trimmedName);
		}
		return trimmedName;
	}

	@Override
	public void log(LogLevel level, String tag, String message) {
		tag = enforceValidTagLength(tag);
		if(convertDebugToInfo && level == LogLevel.DEBUG) {
			level = LogLevel.INFO;
		}
		
		switch (level) {
		default:
		case DEBUG:
			Log.d(tag, message);
			break;
		case INFO:
			Log.i(tag, message);
			break;
		case WARN:
			Log.w(tag, message);
			break;
		case ERROR:
			Log.e(tag, message);
			break;
		}
	}

}
