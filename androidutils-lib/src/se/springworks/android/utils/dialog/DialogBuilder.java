package se.springworks.android.utils.dialog;

import se.springworks.android.utils.view.LinkifyUtil;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.SpannableString;
import android.view.WindowManager.BadTokenException;
import android.widget.TextView;

/**
 * Helper, similar to the native Dialog.Builder class, but with added functionality to
 * linkify and set movement mode to the message
 * @author bjornritzl
 *
 */
public class DialogBuilder {
	
	private Builder builder;
	
	private Context context;
	
	public DialogBuilder(Context context) {
		this.context = context;
	}
	
	public static DialogBuilder create(Context context, int titleId, int messageId) {
		return create(context, context.getString(titleId), context.getString(messageId));
	}
	
	public static DialogBuilder create(Context context, int titleId, String message) {
		return create(context, context.getString(titleId), message);
	}
	
	public static DialogBuilder create(Context context, int titleId, SpannableString message) {
		return create(context, context.getString(titleId), message);
	}
	
	public static DialogBuilder create(Context context, String title, String message) {
		return create(context, title, new SpannableString(message));
	}
	
	public static DialogBuilder create(Context context, String title, SpannableString message) {
		DialogBuilder b = new DialogBuilder(context);
		b.builder = new AlertDialog.Builder(context);
		b.builder.setTitle(title);
		b.builder.setMessage(message);
		
//		TextView tv = new TextView(context);
//		tv.setText(message);
//		tv.setAutoLinkMask(Linkify.ALL);
//		tv.setMovementMethod(LinkMovementMethod.getInstance());
//		
//		b.builder.setView(tv);
		return b;
	}
	
	public DialogBuilder setCancelable(boolean cancelable) {
		builder.setCancelable(cancelable);
		return this;
	}
	
	public DialogBuilder addNegativeButton(int cancelId) {
		return addNegativeButton(context.getString(cancelId), null);
	}
	public DialogBuilder addNegativeButton(int cancelId, DialogInterface.OnClickListener listener) {
		return addNegativeButton(context.getString(cancelId), listener);
	}
	public DialogBuilder addNegativeButton(String cancel) {
		return addNegativeButton(cancel, null);
	}	
	public DialogBuilder addNegativeButton(String cancel, DialogInterface.OnClickListener listener) {
		builder.setNegativeButton(cancel, listener);
		return this;
	}
	
	public DialogBuilder addNeutralButton(int cancelId) {
		return addNeutralButton(context.getString(cancelId), null);
	}
	public DialogBuilder addNeutralButton(int cancelId, DialogInterface.OnClickListener listener) {
		return addNeutralButton(context.getString(cancelId), listener);
	}
	public DialogBuilder addNeutralButton(String cancel) {
		return addNeutralButton(cancel, null);
	}	
	public DialogBuilder addNeutralButton(String cancel, DialogInterface.OnClickListener listener) {
		builder.setNeutralButton(cancel, listener);
		return this;
	}
	
	
	
	public DialogBuilder addPositiveButton(int confirmId) {
		return addPositiveButton(context.getString(confirmId), null);
	}
	public DialogBuilder addPositiveButton(int confirmId, DialogInterface.OnClickListener listener) {
		return addPositiveButton(context.getString(confirmId), listener);
	}
	public DialogBuilder addPositiveButton(String confirm) {
		return addPositiveButton(confirm, null);
	}	
	public DialogBuilder addPositiveButton(String confirm, DialogInterface.OnClickListener listener) {
		builder.setPositiveButton(confirm, listener);
		return this;
	}
	
	
	public Dialog show() {
		AlertDialog dialog = builder.create();
		try {
			dialog.show();
			TextView message = (TextView)dialog.findViewById(android.R.id.message);
			if(message != null) {
				LinkifyUtil.linkify(message);
			}
		}
		catch(BadTokenException e) {
			// do nothing
			// reported on google play console
			// Unable to add window -- token android.os.BinderProxy@4223bb48 is not valid; is your activity running?
		}
		return dialog;
	}
	
	public static Dialog showConfirmCancel(Context context, int titleId, int messageId, int confirmId, int cancelId, DialogInterface.OnClickListener confirmListener) {
		return DialogBuilder.create(context, titleId, messageId)
				.addPositiveButton(confirmId, confirmListener)
				.addNegativeButton(cancelId)
				.setCancelable(true)
				.show();
	}

	public static Dialog showConfirm(Context context, int titleId, int messageId, int confirmId, DialogInterface.OnClickListener confirmListener) {
		return DialogBuilder
				.create(context, titleId, messageId)
				.addPositiveButton(confirmId, confirmListener)
				.setCancelable(false)
				.show();
	}

	public static Dialog show(Context context, int titleId, int messageId) {
		return show(context, context.getString(titleId), context.getString(messageId));
	}
	
	public static Dialog show(Context context, String title, String message) {
		return show(context, title, new SpannableString(message));
	}
	
	public static Dialog show(Context context, String title, SpannableString message) {
		return DialogBuilder
				.create(context, title, message)
				.setCancelable(false)
				.show();
	}
}
