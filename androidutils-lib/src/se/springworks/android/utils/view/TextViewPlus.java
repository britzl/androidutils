package se.springworks.android.utils.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

// http://stackoverflow.com/questions/2376250/custom-fonts-and-xml-layouts-android
public class TextViewPlus extends TextView {

	public TextViewPlus(Context context) {
		super(context);
	}

	public TextViewPlus(Context context, AttributeSet attrs) {
		super(context, attrs);
		CustomFontHelper.setCustomFont(this, context, attrs);
	}

	public TextViewPlus(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		CustomFontHelper.setCustomFont(this, context, attrs);
	}
}