package se.springworks.android.utils.error;

import se.springworks.android.utils.logging.Logger;
import se.springworks.android.utils.logging.LoggerFactory;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;

/**
 * Simple error handler that can be used to display an error dialog
 * @author bjornritzl
 *
 */
public class ErrorHandler {

	private static Logger logger = LoggerFactory.getLogger(ErrorHandler.class);
	
	private static Dialog currentDialog = null;

	public static final void handleError(int titleId, int errorId, Activity activity) {
		if(activity == null) {
			return;
		}
		handleError(activity.getString(titleId), activity.getString(errorId), null, activity);
	}
	
	public static final void handleError(String title, String error, Activity activity) {
		handleError(title, error, null, activity);
	}
	
	public static final void handleError(int titleId, int errorId, Throwable t, Activity activity) {
		handleError(activity.getString(titleId), activity.getString(errorId), t, activity);
	}
	
	
	public static final void handleError(String title, String error, Throwable t, Activity activity) {
		String message = error;
		if(t != null) {
			message += " " + t.getLocalizedMessage();
		}
		logger.error(message);
		if(activity == null) {
			return;
		}
		
		dismiss();
		
		AlertDialog.Builder builder = new AlertDialog.Builder(activity);
		builder.setTitle(title);
		builder.setMessage(message);
		final AlertDialog dialog = builder.create();
		dialog.setCanceledOnTouchOutside(true);
		
		activity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				try {
					dialog.show();
					currentDialog = dialog;
				}
				catch(Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	public static final void dismiss() {
		if(currentDialog != null) {
			currentDialog.dismiss();
		}
	}
}
