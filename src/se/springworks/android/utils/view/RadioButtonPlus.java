package se.springworks.android.utils.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RadioButton;

public class RadioButtonPlus extends RadioButton {

	public RadioButtonPlus(Context context) {
		super(context);
	}

	public RadioButtonPlus(Context context, AttributeSet attrs) {
		super(context, attrs);
		CustomFontHelper.setCustomFont(this, context, attrs);
	}

	public RadioButtonPlus(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		CustomFontHelper.setCustomFont(this, context, attrs);
	}
}
