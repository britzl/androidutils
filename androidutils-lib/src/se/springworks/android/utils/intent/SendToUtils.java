package se.springworks.android.utils.intent;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;

public class SendToUtils {

	public static void sendSMS(Activity a, String message, String number) {
		Uri uri = Uri.parse("smsto:" + number); 
        Intent sendIntent = new Intent(Intent.ACTION_SENDTO, uri); 
        sendIntent.putExtra("sms_body", message); 
        a.startActivity(sendIntent);
	}

	public static void sendSMS(Activity a, String message) {
		Intent sendIntent = new Intent(Intent.ACTION_SENDTO);
		sendIntent.putExtra("sms_body", message); 
		a.startActivity(sendIntent);
	}

	public static void sendMail(Activity a, String subject, String body, String to) {
		Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto",to, null));
		emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
		emailIntent.putExtra(Intent.EXTRA_TEXT, body);
		a.startActivity(emailIntent);
	}
}
