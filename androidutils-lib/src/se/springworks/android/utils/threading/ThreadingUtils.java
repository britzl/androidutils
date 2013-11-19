package se.springworks.android.utils.threading;

import android.os.Handler;

public class ThreadingUtils {

	public static void callAfterDelay(long delay, Runnable runnable) {
		final Handler handler = new Handler();
		handler.postDelayed(runnable, delay);		
	}
}
