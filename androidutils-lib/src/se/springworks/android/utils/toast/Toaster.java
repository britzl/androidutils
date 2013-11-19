package se.springworks.android.utils.toast;

import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

public class Toaster {
	
	public static void showShortToast(Context context, String text) {
		showToast(context, text, Toast.LENGTH_SHORT, Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM);
	}
	
	public static void showLongToast(Context context, String text) {
		showToast(context, text, Toast.LENGTH_LONG, Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM);
	}
	
	public static void showShortToast(Context context, int textId) {
		showToast(context, context.getResources().getString(textId), Toast.LENGTH_SHORT, Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM);
	}
	
	public static void showLongToast(Context context, int textId) {
		showToast(context, context.getResources().getString(textId), Toast.LENGTH_LONG, Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM);
	}
	
	public static void showShortToast(Context context, int textId, int gravity) {
		showToast(context, context.getResources().getString(textId), Toast.LENGTH_SHORT, gravity);
	}
	
	public static void showLongToast(Context context, int textId, int gravity) {
		showToast(context, context.getResources().getString(textId), Toast.LENGTH_LONG, gravity);
	}
	
	private static void showToast(Context context, String text, int duration, int gravity) {
		Toast t = Toast.makeText(context, text, duration);
		t.setGravity(gravity, 0, 0);
		t.show();
	}
}
