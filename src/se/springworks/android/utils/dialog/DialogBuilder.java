package se.springworks.android.utils.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;

public class DialogBuilder {

	public static Dialog showConfirmCancel(Context context, int titleId, int messageId, int confirmId, int cancelId, DialogInterface.OnClickListener confirmListener) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle(titleId);
		builder.setMessage(messageId);
		builder.setNegativeButton(cancelId, null);
		builder.setPositiveButton(confirmId, confirmListener);
		final AlertDialog dialog = builder.create();
		dialog.setCanceledOnTouchOutside(true);
		dialog.show();
		return dialog;
	}

	public static Dialog showConfirm(Context context, int titleId, int messageId, int confirmId, DialogInterface.OnClickListener confirmListener) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle(titleId);
		builder.setMessage(messageId);
		builder.setPositiveButton(confirmId, confirmListener);
		final AlertDialog dialog = builder.create();
		dialog.setCanceledOnTouchOutside(false);
		dialog.show();
		return dialog;
	}

	public static Dialog show(Context context, int titleId, int messageId) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle(titleId);
		builder.setMessage(messageId);
		final AlertDialog dialog = builder.create();
		dialog.setCanceledOnTouchOutside(false);
		dialog.show();
		return dialog;
	}
}
