package se.springworks.android.utils.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

public class AlertDialogBuilder {

	public static AlertDialog create(Context context, int layoutId) {
		LayoutInflater inflater = LayoutInflater.from(context);
		final View layout = inflater.inflate(layoutId, null);
		final AlertDialog.Builder builder = new AlertDialog.Builder(context);
		AlertDialog dialog = builder.create();
		dialog.setView(layout);
		return dialog;
	}
}
