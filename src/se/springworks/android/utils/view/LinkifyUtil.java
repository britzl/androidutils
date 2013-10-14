package se.springworks.android.utils.view;

import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

public class LinkifyUtil {

	/**
	 * Linkify a view.
	 * If the view is a view group all children in the display tree will be linkified.
	 * If the view is a single TextView it will be linkified.
	 * If the view is an EditText nothing will happen since there's a bug in Linkify
	 * on some platforms (tested on Samsung S3 but works on HTC One V)
	 * @param v
	 * @param mask
	 */
	public static void linkify(View v, int mask) {
		if(v instanceof EditText) {
			// do nothing
			// linkifying an edittext causes a crash: https://groups.google.com/forum/#!msg/android-developers/fPW5KPasNtw/ZsAvk0Md5MgJ
		}
		else if(v instanceof TextView) {
			linkify((TextView)v, mask);
		}
		else if(v instanceof ViewGroup) {
			ViewGroup vg = (ViewGroup)v;
			int childCount = vg.getChildCount();
			for(int i = 0; i < childCount; i++) {
				linkify(vg.getChildAt(i), mask);
			}
		}
	}

	public static void linkify(TextView tv) {
		LinkifyUtil.linkify(tv, Linkify.ALL);
	}
	
	public static void linkify(TextView tv, int mask) {
		Linkify.addLinks(tv, mask);
		tv.setMovementMethod(LinkMovementMethod.getInstance());
	}
	
	public static SpannableString linkifyAll(String text) {
		return linkify(text, Linkify.ALL);
	}
	
	public static SpannableString linkify(String text, int mask) {
		if(text == null || text.isEmpty()) {
			return new SpannableString("");
		}
		SpannableString s = new SpannableString(text);
		Linkify.addLinks(s, mask);
		return s;
	}
}
